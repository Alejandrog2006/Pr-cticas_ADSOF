/*
 * Sensor de presión atmosférica.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor;

import estacion.alerta.LecturaFueraDeRangoException;
import estacion.alerta.SensorNoCalibradoException;
import estacion.unidadLectura.UnidadPresion;

/**
 * Sensor meteorológico de presión atmosférica.
 */
public class SensorPresion extends Sensor{
    public static int numSensores = 0;
    
    /**
     * Crea un sensor de presión con el offset indicado.
     *
     * @param offset corrección fija de la lectura.
     */
    public SensorPresion(double offset) {
        super(offset);
        this.unidadLectura = UnidadPresion.HPA;
        this.id = "PRES-" + String.format("%04d", SensorPresion.numSensores);
        SensorPresion.numSensores ++;
    }


    /**
     * Obtiene una lectura de presión atmosférica.
     *
     * @return valor de presión en hPa.
     */
    public double medir() {
        if (!this.puedeMedir()) {
            throw new SensorNoCalibradoException(this.id, "El sensor no esta calibrado o su calibracion caduco");
        }

        double medida = this.estrategia.generarValor(300.0, 1100.0); // medida en hPa
        if (medida < 300.0 || medida > 1100.0) { // medida en hPa
            throw new LecturaFueraDeRangoException(this.id, "La medida de presion debe estar entre 300.0 hPa y 1100.0 hPa");
        }

        return medida;
    }
}
