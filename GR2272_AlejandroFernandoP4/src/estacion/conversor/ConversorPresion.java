package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;
import estacion.unidadLectura.UnidadPresion;

public enum ConversorPresion implements Conversor{
    HPA_PA(UnidadPresion.HPA, UnidadPresion.PA) {
        @Override
        public double convertir(double valor) {
            return valor * 100;
        }        
    },
    HPA_MBAR(UnidadPresion.HPA, UnidadPresion.MBAR) {
        @Override
        public double convertir(double valor) {
            return valor;
        }        
    },
    PA_HPA(UnidadPresion.PA, UnidadPresion.HPA) {
        @Override
        public double convertir(double valor) {
            return valor / 100;
        }        
    },
    PA_MBAR(UnidadPresion.PA, UnidadPresion.MBAR) {
        //No es óptimo, pero ejemplifican en el enunciado que se pueden encadenar las conversiones
        @Override
        public double convertir(double valor) {
            return ConversorPresion.HPA_MBAR.convertir(ConversorPresion.PA_HPA.convertir(valor));
        }        
    },
    MBAR_HPA(UnidadPresion.MBAR, UnidadPresion.HPA) {
        @Override
        public double convertir(double valor) {
            return valor;
        }
    },
    MBAR_PA(UnidadPresion.MBAR, UnidadPresion.PA) {
        @Override
        public double convertir(double valor) {
            return ConversorPresion.HPA_PA.convertir(ConversorPresion.MBAR_HPA.convertir(valor));
        }        
    };

    private UnidadLectura unidadInicial;
    private UnidadLectura unidadFinal;

    private ConversorPresion(UnidadLectura unidadInicial, UnidadLectura unidadFinal) {
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
