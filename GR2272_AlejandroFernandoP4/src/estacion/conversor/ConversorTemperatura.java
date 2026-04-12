/*
 * Conversores entre unidades de temperatura.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;
import estacion.unidadLectura.UnidadTemperatura;

/**
 * Conversores disponibles entre unidades de temperatura.
 */
public enum ConversorTemperatura implements Conversor{
    CELSIUS_KELVIN(UnidadTemperatura.CELSIUS, UnidadTemperatura.KELVIN) {
        @Override
        public double convertir(double valor) {
            return valor + 273.15;
        }        
    },
    CELSIUS_FAHRENHEIT(UnidadTemperatura.CELSIUS, UnidadTemperatura.FAHRENHEIT) {
        @Override
        public double convertir(double valor) {
            return valor * 9/5 + 32;
        }
    },
    KELVIN_CELSIUS(UnidadTemperatura.KELVIN, UnidadTemperatura.CELSIUS) {
        @Override
        public double convertir(double valor) {
            return valor - 273.15;
        }
    },
    KELVIN_FAHRENHEIT(UnidadTemperatura.KELVIN, UnidadTemperatura.FAHRENHEIT) {
        //Se pueden encadenar las conversiones
        @Override
        public double convertir(double valor) {
            return ConversorTemperatura.CELSIUS_FAHRENHEIT.convertir(ConversorTemperatura.KELVIN_CELSIUS.convertir(valor));
        }
    },
    FAHRENHEIT_CELSIUS(UnidadTemperatura.FAHRENHEIT, UnidadTemperatura.CELSIUS) {
        @Override
        public double convertir(double valor) {
            return (valor - 32) * 5/9;
        }
    },
    FAHRENHEIT_KELVIN(UnidadTemperatura.FAHRENHEIT, UnidadTemperatura.KELVIN) {
        @Override
        public double convertir(double valor) {
            return ConversorTemperatura.CELSIUS_KELVIN.convertir(ConversorTemperatura.FAHRENHEIT_CELSIUS.convertir(valor));
        }
    };

    private UnidadLectura unidadInicial;
    private UnidadLectura unidadFinal;

    /**
     * Crea la configuración base del conversor de temperatura.
     *
     * @param unidadInicial unidad de origen.
     * @param unidadFinal unidad de destino.
     */
    private ConversorTemperatura(UnidadLectura unidadInicial, UnidadLectura unidadFinal) {
        this.unidadInicial = unidadInicial;
        this.unidadFinal = unidadFinal;
    }

    /**
     * Obtiene la unidad inicial.
     *
     * @return unidad de entrada.
     */
    public UnidadLectura unidadInicial() {
        return unidadInicial;
    }

    /**
     * Obtiene la unidad final.
     *
     * @return unidad de salida.
     */
    public UnidadLectura unidadFinal() {
        return unidadFinal;
    }
}
