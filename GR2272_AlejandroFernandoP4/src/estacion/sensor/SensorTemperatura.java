package estacion.sensor;

import estacion.unidadLectura.UnidadTemperatura;

public class SensorTemperatura extends Sensor {
    public static int numSensores = 0;
    public SensorTemperatura(double offset) {
        super(offset);
        this.unidadLectura = UnidadTemperatura.CELSIUS;
        this.id = "TEMP-" + String.format("%04d", this.numSensores);
        this.numSensores ++;
    }

    public double medir() {
        if (!this.puedeMedir()) {
            throw new IllegalStateException("El sensor no está calibrado o ha pasado el intervalo de calibración.");
        }
        return 0.0;
    }
}