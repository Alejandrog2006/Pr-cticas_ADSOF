package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;

public enum ConversorTemperatura implements Conversor{
    CELSIUS_KELVIN(UnidadLectura.CELSIUS, UnidadLectura.KELVIN) {
        @Override
        public double convertir(double valor) {
            return valor + 273.15;
        }        
    },
    CELSIUS_FAHRENHEIT(UnidadLectura.CELSIUS, UnidadLectura.FAHRENHEIT) {
        @Override
        public double convertir(double valor) {
            return valor * 9/5 + 32;
        }
    },
    KELVIN_CELSIUS(UnidadLectura.KELVIN, UnidadLectura.CELSIUS) {
        @Override
        public double convertir(double valor) {
            return valor - 273.15;
        }
    },
    KELVIN_FAHRENHEIT(UnidadLectura.KELVIN, UnidadLectura.FAHRENHEIT) {
        //Se pueden encadenar las conversiones
        @Override
        public double convertir(double valor) {
            return ConversorTemperatura.CELSIUS_FAHRENHEIT.convertir(ConversorTemperatura.KELVIN_CELSIUS.convertir(valor));;
        }
    },
    FAHRENHEIT_CELSIUS(UnidadLectura.FAHRENHEIT, UnidadLectura.CELSIUS) {
        @Override
        public double convertir(double valor) {
            return (valor - 32) * 5/9;
        }
    },
    FAHRENHEIT_KELVIN(UnidadLectura.FAHRENHEIT, UnidadLectura.KELVIN) {
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
