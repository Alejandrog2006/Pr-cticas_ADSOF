package estacion;

import java.time.Duration;
import java.util.*;

import estacion.sensor.*;

public abstract class EstacionMeteo {
    private Map<String, Sensor> sensores; // ID/Sensor
    private String nombre;
    private Ubicacion ubicacion;

    public EstacionMeteo(String nombre, double lat, double lon) {
        this.sensores = new HashMap<>();
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
        return true;
    }
    
    public boolean leerDatos(){
        for (Sensor sensor : this.sensores.values()) {
            sensor.obtenerMedida();
        }
    
        return true;
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
