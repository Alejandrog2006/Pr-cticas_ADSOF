package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;
import estacion.unidadLectura.UnidadTemperatura;

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

    private ConversorTemperatura(UnidadLectura unidadInicial, UnidadLectura unidadFinal) {
        this.unidadInicial = unidadInicial;
        this.unidadFinal = unidadFinal;
    }

    public UnidadLectura unidadInicial() {
        return unidadInicial;
    }

    public UnidadLectura unidadFinal() {
        return unidadFinal;
    }
}
