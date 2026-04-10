package estacion.sensor;

import estacion.unidadLectura.UnidadPresion;

public class SensorPresion extends Sensor{
    public static int numSensores = 0;
    
    public SensorPresion(double offset) {
        super(offset);
        this.unidadLectura = UnidadPresion.HPA;
        this.id = "PRES-" + String.format("%04d", SensorPresion.numSensores);
        SensorPresion.numSensores ++;
    }


    public double medir() {
        if (!this.puedeMedir()) {
            throw new IllegalStateException("El sensor no está calibrado o ha pasado el intervalo de calibración.");
        }

        double medida = this.estrategia.generarValor(300.0, 1100.0); // medida en hPa
        if (medida < 300.0 || medida > 1100.0) { // medida en hPa
            throw new IllegalArgumentException("La medida de presión debe estar entre 300.0 hPa y 1100.0 hPa.");
        }

        return medida;
    }
}
