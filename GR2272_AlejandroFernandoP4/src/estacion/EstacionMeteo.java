package estacion;

import java.time.Duration;
import java.util.*;

import estacion.conversor.Conversor;
import estacion.procesador.ProcesadorDatos;
import estacion.sensor.*;
import estacion.aux.Ubicacion;

public abstract class EstacionMeteo {
    private Map<String, Sensor> sensores; // ID/Sensor
    private Map<String, ProcesadorDatos> procesadores; // ID/Procesador
    private String nombre;
    private Ubicacion ubicacion;
    //Agregado nueva variable
    private LocalDateTime ultimaLectura;

    public EstacionMeteo(String nombre, double lat, double lon) {
        this.sensores = new HashMap<>();
        this.procesadores = new HashMap<>();
        this.nombre = nombre;
        this.ubicacion = new Ubicacion(lat, lon);
        this.ultimaLectura = null;
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
        return true;
    }
    
    public boolean leerDatos(){
        for (Sensor sensor : this.sensores.values()) {
            sensor.obtenerMedida();
            ProcesadorDatos procesador = this.procesadores.get(sensor.getId());
            procesador.almacenarLectura(sensor.getFechaUltimaLectura(), sensor.getUltimaLectura());
        }
        this.ultimaLectura = LocalDateTime.now();
        return true;
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

    public getUltimaLectura() {
        return ultimaLectura;
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
}
