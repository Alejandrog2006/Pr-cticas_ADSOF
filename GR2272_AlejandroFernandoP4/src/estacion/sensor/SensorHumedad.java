/*
 * Sensor de humedad relativa que genera porcentajes.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor;

import estacion.alerta.LecturaFueraDeRangoException;
import estacion.alerta.SensorNoCalibradoException;
import estacion.unidadLectura.UnidadHumedad;

/**
 * Sensor meteorológico de humedad relativa.
 */
public class SensorHumedad extends Sensor{
    public static int numSensores = 0;

    /**
     * Crea un sensor de humedad con el offset indicado.
     *
     * @param offset corrección fija de la lectura.
     */
    public SensorHumedad(double offset) {
        super(offset);
        this.unidadLectura = UnidadHumedad.PORCENTAJE;
        this.id = "HUM-" + String.format("%04d", SensorHumedad.numSensores);
        SensorHumedad.numSensores ++;
    }


    /**
     * Obtiene una lectura de humedad relativa.
     *
     * @return valor de humedad en porcentaje.
     */
    public double medir() {
        if (!this.puedeMedir()) {
            throw new SensorNoCalibradoException(this.id, "El sensor no esta calibrado o su calibracion caduco");
        }

        double medida = this.estrategia.generarValor(0.0, 100.0); // %
        if (medida < 0.0 || medida > 100.0) {
               throw new LecturaFueraDeRangoException(this.id, "La medida de humedad debe estar entre 0.0 y 100.0");
        }
        return medida;
    }
    
}
