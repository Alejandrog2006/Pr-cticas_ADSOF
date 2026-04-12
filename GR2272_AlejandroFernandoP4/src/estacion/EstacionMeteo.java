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

public abstract class EstacionMeteo {
    private Map<String, Sensor> sensores; // ID/Sensor
    private Map<String, ProcesadorDatos> procesadores; // ID/Procesador
    private Map<String, Double> ultimasLecturasPorSensor;
    private Set<String> sensoresDetenidos;
    private List<Alerta> alertas;
    private double umbralCambioBruscoPct;
    private String nombre;
    private Ubicacion ubicacion;

    public EstacionMeteo(String nombre, double lat, double lon) {
        this.sensores = new HashMap<>();
        this.procesadores = new HashMap<>();
        this.ultimasLecturasPorSensor = new HashMap<>();
        this.sensoresDetenidos = new HashSet<>();
        this.alertas = new ArrayList<>();
        this.umbralCambioBruscoPct = 50.0;
        this.nombre = nombre;
        this.ubicacion = new Ubicacion(lat, lon);
    }

    // Al agregar, le pasas el sensor, o lo creas y lea pasas el offset?
    public boolean agregarSensor(Sensor sensor) {

        String id = sensor.getId();
        
        if (this.sensores.containsKey(id)) {
            throw new IllegalArgumentException("Ya existe un sensor con el ID: " + id);
        }
        this.sensores.put(id, sensor);
        this.procesadores.put(id, new ProcesadorDatos(sensor));
        return true;
    }

    public boolean agregarSensor(Sensor sensor, Conversor conversor) {
        String id = sensor.getId();
        
        if (this.sensores.containsKey(id)) {
            throw new IllegalArgumentException("Ya existe un sensor con el ID: " + id);
        }
        this.sensores.put(id, sensor);
        this.procesadores.put(id, new ProcesadorDatos(sensor, conversor));
        return true;
    }
    

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

    // eliminar sensor por ID, no se pide pero por si acaso
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
    
        return true;
    }

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

    public void setUmbralCambioBruscoPct(double umbralCambioBruscoPct) {
        if (umbralCambioBruscoPct <= 0) {
            throw new IllegalArgumentException("El umbral de cambio brusco debe ser mayor que 0");
        }
        this.umbralCambioBruscoPct = umbralCambioBruscoPct;
    }

    public double getUmbralCambioBruscoPct() {
        return umbralCambioBruscoPct;
    }

    public List<Alerta> getAlertas() {
        return new ArrayList<>(alertas);
    }

    public boolean estaDetenido(String idSensor) {
        return sensoresDetenidos.contains(idSensor);
    }

    public void configurarConversor(String idSensor, Conversor conversor) {
        ProcesadorDatos procesador = this.procesadores.get(idSensor);
        if (procesador == null) {
            throw new IllegalArgumentException("No existe un sensor con ID: " + idSensor);
        }
        procesador.setConversor(conversor);
    }

    public ProcesadorDatos getProcesador(String idSensor) {
        return this.procesadores.get(idSensor);
    }

    public boolean lecturaPeriodica(Duration intervalo, int numLecturas) {
        for (int i = 0; i < numLecturas; i++) {
            this.leerDatos();
            try {
                Thread.sleep(intervalo.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    public Sensor encontrarSensorID(String id) {
        return this.sensores.get(id);
    }

    public List<Sensor> obtenerSensores() {
        return new ArrayList<Sensor>(this.sensores.values());
    }

    public String getNombre() {
        return nombre;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public List<Sensor> encontrarSensor(String tipo) { // TEMP, HUM, PRES
        List<Sensor> sensoresTipo = new ArrayList<>();
        for (Sensor sensor : this.sensores.values()) {
            if (sensor.getId().startsWith(tipo)) {
                sensoresTipo.add(sensor);
            }
        }
        return sensoresTipo;
    }

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

    private void registrarAlerta(AlertaSensorException exception) {
        alertas.add(new Alerta(
            exception.getSensorId(),
            exception.getTipo(),
            exception.getMessage(),
            LocalDateTime.now()
        ));
    }
}
