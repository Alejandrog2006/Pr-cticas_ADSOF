/*
 * Interfaz base para convertir valores entre unidades de lectura.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;

/**
 * Define un conversor entre dos unidades de lectura.
 */
public interface Conversor {
    /**
     * Convierte un valor entre unidades.
     *
     * @param valor valor a convertir.
     * @return valor convertido.
     */
    double convertir(double valor);
    /**
     * Obtiene la unidad de entrada del conversor.
     *
     * @return unidad inicial.
     */
    UnidadLectura unidadInicial();
    /**
     * Obtiene la unidad de salida del conversor.
     *
     * @return unidad final.
     */
    UnidadLectura unidadFinal();


    /**
     * Encadena este conversor con otro compatible. Repite la lógica de la enumeración de conversores, pero lo hace extensible.
     *
     * @param siguiente conversor a aplicar después.
     * @return nuevo conversor compuesto.
     */
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
