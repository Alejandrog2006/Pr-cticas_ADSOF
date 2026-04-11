package estacion.conversor;

import estacion.unidadLectura.UnidadHumedad;
import estacion.unidadLectura.UnidadLectura;

public enum ConversorHumedad implements Conversor {
    PORCENTAJE(UnidadHumedad.PORCENTAJE, UnidadHumedad.PORCENTAJE) {
        @Override
        public double convertir(double valor) {
            return valor;
        }
    };
    //Habría que considerar poner otra unidad de humedad, no sólo el porcentaje de 0 a 100 (igual de 0 a 1)
    /*
    PORCENTAJE_DECIMAL {
        @Override
        public double convertir(double valor) {
            return valor / 100.0;
        }
    },
    DECIMAL_PORCENTAJE {
        @Override
        public double convertir(double valor) {
            return valor * 100.0;
        }
    };
    */

    //Todo esto está para que sea extensible, ahora es redundante
    private UnidadLectura unidadInicial;
    private UnidadLectura unidadFinal;

    private ConversorHumedad(UnidadLectura unidadInicial, UnidadLectura unidadFinal) {
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
