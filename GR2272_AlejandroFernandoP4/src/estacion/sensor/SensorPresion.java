package estacion.sensor;

import estacion.unidadLectura.UnidadPresion;

public class SensorPresion extends Sensor{
    public static int numSensores = 0;
    
    public SensorPresion(double offset) {
        super(offset);
        this.unidadLectura = UnidadPresion.HPA;
        this.id = "PRES-" + String.format("%04d", this.numSensores);
        this.numSensores ++;
    }


    public double medir() {
        if (!this.puedeMedir()) {
            throw new IllegalStateException("El sensor no está calibrado o ha pasado el intervalo de calibración.");
        }
        return 0.0;
    }
}
