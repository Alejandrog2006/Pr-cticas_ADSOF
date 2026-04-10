package estacion.sensor;

import estacion.unidadLectura.UnidadHumedad;

public class SensorHumedad extends Sensor{
    public static int numSensores = 0;

    public SensorHumedad(double offset) {
        super(offset);
        this.unidadLectura = UnidadHumedad.PORCENTAJE;
        this.id = "HUM-" + String.format("%04d", SensorHumedad.numSensores);
        SensorHumedad.numSensores ++;
    }


    public double medir() {
        if (!this.puedeMedir()) {
            throw new IllegalStateException("El sensor no está calibrado o ha pasado el intervalo de calibración.");
        }

        double medida = this.estrategia.generarValor(0.0, 100.0); // %
        if (medida < 0.0 || medida > 100.0) {
             throw new IllegalArgumentException("La medida de humedad debe estar entre 0.0 y 100.0.");
        }
        return medida;
    }
    
}
