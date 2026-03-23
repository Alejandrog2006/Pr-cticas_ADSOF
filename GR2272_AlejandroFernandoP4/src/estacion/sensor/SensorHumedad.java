package estacion.sensor;

import estacion.unidadLectura.UnidadHumedad;

public class SensorHumedad extends Sensor{
    public static int numSensores = 0;


    public SensorHumedad(double offset) {
        super(offset);
        this.unidadLectura = UnidadHumedad.PORCENTAJE;
        this.id = "HUM-" + String.format("%04d", this.numSensores);
        this.numSensores ++;
    }


    public double medir() {
        if (!this.puedeMedir()) {
            throw new IllegalStateException("El sensor no está calibrado o ha pasado el intervalo de calibración.");
        }
        return 0.0;
    }
    
}
