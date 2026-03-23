package estacion;

import java.util.*;

import estacion.sensor.Sensor;

import java.time.*;

public abstract class EstacionMeteo {
    private Map<String, Sensor> sensores;
    private String nombre;
    private Ubicacion ubicacion;

    public EstacionMeteo(String nombre, double lat, double lon) {
        this.sensores = new HashMap<>();
        this.nombre = nombre;
        this.ubicacion = new Ubicacion(lat, lon);
    }

    //Al agregar, le pasas el sensor, o lo creas y lea pasas el offset?
    public boolean agregarSensor(Sensor sensor) {

        String id = sensor.getId();
        
        if (this.sensores.containsKey(id)) {
            return false;
        }
        this.sensores.put(id, sensor);
        return true;
    }
    
    public double leerDatos(){
        return 0;
    }

    public Sensor encontrarSensor(String id) {
        return this.sensores.get(id);
    }

    public List<Sensor> encontrarSensor() {
        return null;
    }
}
