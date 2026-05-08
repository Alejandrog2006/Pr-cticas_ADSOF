package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;
import estacion.unidadLectura.UnidadPresion;

/**
 * Conversores disponibles entre unidades de presión.
 * @author Alejandro González
 * @author Fernando Blanco
 */
public enum ConversorPresion implements Conversor{
    /** Conversor de hectopascales a pascales. */
    HPA_PA(UnidadPresion.HPA, UnidadPresion.PA) {
        @Override
        public double convertir(double valor) {
            return valor * 100;
        }        
    },
    /** Conversor identidad de hectopascales a milibares. */
    HPA_MBAR(UnidadPresion.HPA, UnidadPresion.MBAR) {
        @Override
        public double convertir(double valor) {
            return valor;
        }        
    },
    /** Conversor de pascales a hectopascales. */
    PA_HPA(UnidadPresion.PA, UnidadPresion.HPA) {
        @Override
        public double convertir(double valor) {
            return valor / 100;
        }        
    },
    /** Conversor de pascales a milibares mediante encadenamiento. */
    PA_MBAR(UnidadPresion.PA, UnidadPresion.MBAR) {
        //No es óptimo, pero ejemplifican en el enunciado que se pueden encadenar las conversiones
        @Override
        public double convertir(double valor) {
            return ConversorPresion.HPA_MBAR.convertir(ConversorPresion.PA_HPA.convertir(valor));
        }        
    },
    /** Conversor identidad de milibares a hectopascales. */
    MBAR_HPA(UnidadPresion.MBAR, UnidadPresion.HPA) {
        @Override
        public double convertir(double valor) {
            return valor;
        }
    },
    /** Conversor de milibares a pascales mediante encadenamiento. */
    MBAR_PA(UnidadPresion.MBAR, UnidadPresion.PA) {
        @Override
        public double convertir(double valor) {
            return ConversorPresion.HPA_PA.convertir(ConversorPresion.MBAR_HPA.convertir(valor));
        }        
    };

    private UnidadLectura unidadInicial;
    private UnidadLectura unidadFinal;

    /**
     * Crea la configuración base del conversor de presión.
     *
     * @param unidadInicial unidad de origen.
     * @param unidadFinal unidad de destino.
     */
    private ConversorPresion(UnidadLectura unidadInicial, UnidadLectura unidadFinal) {
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
