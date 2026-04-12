/*
 * Sensor de temperatura.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor;

import estacion.alerta.LecturaFueraDeRangoException;
import estacion.alerta.SensorNoCalibradoException;
import estacion.unidadLectura.UnidadTemperatura;

/**
 * Sensor meteorológico de temperatura.
 */
public class SensorTemperatura extends Sensor {
    public static int numSensores = 0;
    
    /**
     * Crea un sensor de temperatura con el offset indicado.
     *
     * @param offset corrección fija de la lectura.
     */
    public SensorTemperatura(double offset) {
        super(offset);
        this.unidadLectura = UnidadTemperatura.CELSIUS;
        this.id = "TEMP-" + String.format("%04d", SensorTemperatura.numSensores);
        SensorTemperatura.numSensores ++;
    }

    /**
     * Obtiene una lectura de temperatura.
     *
     * @return valor de temperatura en Celsius.
     */
    public double medir() {
        if (!this.puedeMedir()) {
            throw new SensorNoCalibradoException(this.id, "El sensor no esta calibrado o su calibracion caduco");
        }

        double medida = this.estrategia.generarValor(-273.15, 1000.0);
        if (medida < -273.15 || medida > 1000.0) { // en celsius
               throw new LecturaFueraDeRangoException(this.id, "La medida de temperatura debe estar entre -273.15 C y 1000.0 C");
        }
        return medida;
    }
}