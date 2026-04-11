package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;

public interface Conversor {
    double convertir(double valor);
    UnidadLectura unidadInicial();
    UnidadLectura unidadFinal();

    default Conversor encadenar(Conversor siguiente) {
        if (this.unidadFinal() != siguiente.unidadInicial()) {
            throw new IllegalArgumentException("No se puede encadenar conversores con unidades incompatibles");
        }

        Conversor actual = this;
        return new Conversor() {
            @Override
            public double convertir(double valor) {
                return siguiente.convertir(actual.convertir(valor));
            }

            @Override
            public UnidadLectura unidadInicial() {
                return actual.unidadInicial();
            }

            @Override
            public UnidadLectura unidadFinal() {
                return siguiente.unidadFinal();
            }
        };
    }
}
