/*
 * Clase principal de la estación meteorológica: gestiona sensores, procesadores,
 * alertas, calibración y lectura periódica.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import estacion.alerta.Alerta;
import estacion.alerta.AlertaSensorException;
import estacion.alerta.CambioBruscoException;
import estacion.conversor.Conversor;
import estacion.procesador.ProcesadorDatos;
import estacion.sensor.*;
import estacion.aux.Ubicacion;

/**
 * Estación meteorológica que coordina sensores, procesadores de datos y alertas.
 */
public abstract class EstacionMeteo {
    private Map<String, Sensor> sensores; // ID/Sensor
    private Map<String, ProcesadorDatos> procesadores; // ID/Procesador
    private Map<String, Double> ultimasLecturasPorSensor;
    private Set<String> sensoresDetenidos;
    private List<Alerta> alertas;
    private double umbralCambioBruscoPct;
    private String nombre;
    private Ubicacion ubicacion;
    //Agregado nueva variable
    private LocalDateTime ultimaLectura;

    /**
     * Crea una estación con nombre y ubicación geográfica.
     *
     * @param nombre nombre de la estación.
     * @param lat latitud de la estación.
     * @param lon longitud de la estación.
     */
    public EstacionMeteo(String nombre, double lat, double lon) {
        this.sensores = new HashMap<>();
        this.procesadores = new HashMap<>();
        this.ultimasLecturasPorSensor = new HashMap<>();
        this.sensoresDetenidos = new HashSet<>();
        this.alertas = new ArrayList<>();
        this.umbralCambioBruscoPct = 50.0;
        this.nombre = nombre;
        this.ubicacion = new Ubicacion(lat, lon);
        this.ultimaLectura = null;
    }

    /**
     * Agrega un sensor y crea su procesador con conversor identidad por defecto.
     *
     * @param sensor sensor a agregar.
     * @return {@code true} si se agrega correctamente.
     */
    public boolean agregarSensor(Sensor sensor) {

        String id = sensor.getId();
        
        if (this.sensores.containsKey(id)) {
            throw new IllegalArgumentException("Ya existe un sensor con el ID: " + id);
        }
        this.sensores.put(id, sensor);
        this.procesadores.put(id, new ProcesadorDatos(sensor));
        return true;
    }

    /**
     * Agrega un sensor y lo asocia a un conversor concreto.
     *
     * @param sensor sensor a agregar.
     * @param conversor conversor que usará el procesador del sensor.
     * @return {@code true} si se agrega correctamente.
     */
    public boolean agregarSensor(Sensor sensor, Conversor conversor) {
        String id = sensor.getId();
        
        if (this.sensores.containsKey(id)) {
            throw new IllegalArgumentException("Ya existe un sensor con el ID: " + id);
        }
        this.sensores.put(id, sensor);
        this.procesadores.put(id, new ProcesadorDatos(sensor, conversor));
        return true;
    }
    /**
     * Crea y agrega un sensor a partir de su tipo y offset.
     *
     * @param tipo tipo de sensor: TEMP, HUM o PRES.
     * @param offset corrección fija aplicada a la lectura.
     * @return {@code true} si se agrega correctamente.
     */
    public boolean agregarSensor(String tipo, double offset) {
        Sensor sensor;
        switch (tipo.toUpperCase()) {
            case "TEMP":
                sensor = new SensorTemperatura(offset);
                break;
            case "HUM":
                sensor = new SensorHumedad(offset);
                break;
            case "PRES":
                sensor = new SensorPresion(offset);
                break;
            default:
                throw new IllegalArgumentException("Tipo de sensor no reconocido: " + tipo);
        }
        return this.agregarSensor(sensor);
    }

    /**
     * Elimina un sensor por su identificador.
     *
     * @param id identificador del sensor.
     * @return {@code true} si existía y fue eliminado.
     */
    public boolean eliminarSensor(String id) {
        if (!this.sensores.containsKey(id)) {
            return false;
        }
        this.sensores.remove(id);
        this.procesadores.remove(id);
        this.ultimasLecturasPorSensor.remove(id);
        this.sensoresDetenidos.remove(id);
        this.alertas.removeIf(alerta -> alerta.getSensorId().equals(id));
        return true;
    }
    /**
     * Lee todos los sensores disponibles, registra las lecturas y procesa alertas.
     *
     * @return {@code true} si la operación se completa.
     */
    public boolean leerDatos(){
        for (Sensor sensor : this.sensores.values()) {
            String sensorId = sensor.getId();
            if (sensoresDetenidos.contains(sensorId)) {
                continue;
            }

            try {
                sensor.obtenerMedida();
                registrarCambioBruscoSiAplica(sensor);
                ProcesadorDatos procesador = this.procesadores.get(sensorId);
                procesador.almacenarLectura(sensor.getFechaUltimaLectura(), sensor.getUltimaLectura());
                ultimasLecturasPorSensor.put(sensorId, sensor.getUltimaLectura());
            } catch (CambioBruscoException e) {
                registrarAlerta(e);
                ProcesadorDatos procesador = this.procesadores.get(sensorId);
                procesador.almacenarLectura(sensor.getFechaUltimaLectura(), sensor.getUltimaLectura());
                ultimasLecturasPorSensor.put(sensorId, sensor.getUltimaLectura());
            } catch (AlertaSensorException e) {
                registrarAlerta(e);
                sensoresDetenidos.add(sensorId);
            }
        }
        this.ultimaLectura = LocalDateTime.now();
        return true;
    }

    /**
     * Calibra un sensor con un nuevo offset y reanuda su lectura.
     *
     * @param idSensor identificador del sensor.
     * @param offset nuevo offset de calibración.
     */
    public void calibrarSensor(String idSensor, double offset) {
        Sensor sensor = this.sensores.get(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID: " + idSensor);
        }

        sensor.setOffset(offset);
        sensor.calibrar();
        this.sensoresDetenidos.remove(idSensor);
        this.alertas.removeIf(alerta -> alerta.getSensorId().equals(idSensor)); 
        // la flecha es para no tener que crear un nuevo objeto alerta solo para eliminarlo, ya que el equals de alerta se basa en el ID del sensor, el tipo y el mensaje, entonces si el ID coincide con el del sensor que estamos calibrando, lo eliminamos sin importar el tipo o mensaje de la alerta
    }

    /**
     * Calibra un sensor con un nuevo offset y una duración de calibración.
     *
     * @param idSensor identificador del sensor.
     * @param offset nuevo offset de calibración.
     * @param duracionCalibracion duración de la calibración.
     */
    public void calibrarSensor(String idSensor, double offset, Duration duracionCalibracion) {
        Sensor sensor = this.sensores.get(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID: " + idSensor);
        }

        sensor.setOffset(offset);
        sensor.calibrar(duracionCalibracion);
        this.sensoresDetenidos.remove(idSensor);
        this.alertas.removeIf(alerta -> alerta.getSensorId().equals(idSensor));
    }

    /**
     * Calibra un sensor con un nuevo offset y una fecha de caducidad.
     *
     * @param idSensor identificador del sensor.
     * @param offset nuevo offset de calibración.
     * @param fechaCaducidad fecha a partir de la cual la calibración expira.
     */
    public void calibrarSensor(String idSensor, double offset, LocalDateTime fechaCaducidad) {
        Sensor sensor = this.sensores.get(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("No existe un sensor con ID: " + idSensor);
        }

        sensor.setOffset(offset);
        sensor.calibrar(fechaCaducidad);
        this.sensoresDetenidos.remove(idSensor);
        this.alertas.removeIf(alerta -> alerta.getSensorId().equals(idSensor));
    }

    /**
     * Establece el umbral de cambio brusco en porcentaje.
     *
     * @param umbralCambioBruscoPct porcentaje máximo permitido.
     */
    public void setUmbralCambioBruscoPct(double umbralCambioBruscoPct) {
        if (umbralCambioBruscoPct <= 0) {
            throw new IllegalArgumentException("El umbral de cambio brusco debe ser mayor que 0");
        }
        this.umbralCambioBruscoPct = umbralCambioBruscoPct;
    }

    /**
     * Obtiene el umbral de cambio brusco configurado.
     *
     * @return umbral en porcentaje.
     */
    public double getUmbralCambioBruscoPct() {
        return umbralCambioBruscoPct;
    }

    /**
     * Obtiene una copia del historial de alertas.
     *
     * @return lista de alertas.
     */
    public List<Alerta> getAlertas() {
        return new ArrayList<>(alertas);
    }

    /**
     * Indica si un sensor está detenido.
     *
     * @param idSensor identificador del sensor.
     * @return {@code true} si el sensor está detenido.
     */
    public boolean estaDetenido(String idSensor) {
        return sensoresDetenidos.contains(idSensor);
    }

    /**
     * Configura el conversor de un sensor concreto.
     *
     * @param idSensor identificador del sensor.
     * @param conversor conversor a asociar.
     */
    public void configurarConversor(String idSensor, Conversor conversor) {
        ProcesadorDatos procesador = this.procesadores.get(idSensor);
        if (procesador == null) {
            throw new IllegalArgumentException("No existe un sensor con ID: " + idSensor);
        }
        procesador.setConversor(conversor);
    }

    /**
     * Obtiene el procesador asociado a un sensor.
     *
     * @param idSensor identificador del sensor.
     * @return procesador asociado o {@code null} si no existe.
     */
    public ProcesadorDatos getProcesador(String idSensor) {
        return this.procesadores.get(idSensor);
    }

    /**
     * Realiza varias lecturas separadas por un intervalo de tiempo.
     *
     * @param intervalo tiempo entre lecturas.
     * @param numLecturas número de lecturas a realizar.
     * @return {@code true} si finaliza correctamente.
     */
    public boolean lecturaPeriodica(Duration intervalo, int numLecturas) {
        for (int i = 0; i < numLecturas; i++) {
            this.leerDatos();
            // Si no es la última lectura, esperamos el intervalo antes de la siguiente
            this.ultimaLectura = LocalDateTime.now();
            try {
                Thread.sleep(intervalo.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    /**
     * Busca un sensor por su identificador.
     *
     * @param id identificador del sensor.
     * @return sensor encontrado o {@code null}.
     */
    public Sensor encontrarSensorID(String id) {
        return this.sensores.get(id);
    }

    /**
     * Obtiene una copia de la lista de sensores.
     *
     * @return lista de sensores.
     */
    public List<Sensor> obtenerSensores() {
        return new ArrayList<Sensor>(this.sensores.values());
    }

    /**
     * Obtiene el nombre de la estación.
     *
     * @return nombre de la estación.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la ubicación de la estación.
     *
     * @return ubicación geográfica.
     */
    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public getUltimaLectura() {
        return ultimaLectura;
    }

    /**
     * Busca sensores por prefijo de tipo.
     *
     * @param tipo prefijo del tipo de sensor.
     * @return lista de sensores que coinciden.
     */
    public List<Sensor> encontrarSensor(String tipo) { // TEMP, HUM, PRES
        List<Sensor> sensoresTipo = new ArrayList<>();
        for (Sensor sensor : this.sensores.values()) {
            if (sensor.getId().startsWith(tipo)) {
                sensoresTipo.add(sensor);
            }
        }
        return sensoresTipo;
    }

    /**
     * Comprueba si hubo un cambio brusco respecto a la lectura anterior.
     *
     * @param sensor sensor evaluado.
     */
    private void registrarCambioBruscoSiAplica(Sensor sensor) {
        String sensorId = sensor.getId();
        Double lecturaAnterior = ultimasLecturasPorSensor.get(sensorId);
        if (lecturaAnterior == null) {
            return;
        }

        double lecturaActual = sensor.getUltimaLectura();
        double porcentaje;
        if (lecturaAnterior == 0.0) {
            porcentaje = lecturaActual == 0.0 ? 0.0 : Double.POSITIVE_INFINITY; // Si la lectura anterior es 0 cualquier cambio es un cambio brusco, excepto si el actual también es 0, entonces no hay cambio 
        } else {
            porcentaje = Math.abs((lecturaActual - lecturaAnterior) / lecturaAnterior) * 100.0;
        }

        if (porcentaje > umbralCambioBruscoPct) {
            throw new CambioBruscoException(sensorId, lecturaAnterior, lecturaActual, porcentaje);
        }
    }

    /**
     * Registra una alerta generada por una excepción de sensor.
     *
     * @param exception excepción capturada.
     */
    private void registrarAlerta(AlertaSensorException exception) {
        alertas.add(new Alerta(
            exception.getSensorId(),
            exception.getTipo(),
            exception.getMessage(),
            LocalDateTime.now()
        ));
    }
}
