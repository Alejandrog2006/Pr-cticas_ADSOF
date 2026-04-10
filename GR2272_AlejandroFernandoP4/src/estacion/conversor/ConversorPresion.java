package estacion.conversor;

public enum ConversorPresion implements Conversor{
    HPA_PA(UnidadLectura.HPA, UnidadLectura.PA) {
        @Override
        public double convertir(double valor) {
            return valor * 100;
        }        
    },
    HPA_MBAR(UnidadLectura.HPA, UnidadLectura.MBAR) {
        @Override
        public double convertir(double valor) {
            return valor;
        }        
    },
    PA_HPA(UnidadLectura.PA, UnidadLectura.HPA) {
        @Override
        public double convertir(double valor) {
            return valor / 100;
        }        
    },
    PA_MBAR(UnidadLectura.PA, UnidadLectura.MBAR) {
        //No es óptimo, pero ejemplifican en el enunciado que se pueden encadenar las conversiones
        @Override
        public double convertir(double valor) {
            return ConversorPresion.HPA_MBAR.convertir(ConversorPresion.PA_HPA.convertir(valor));
        }        
    },
    MBAR_HPA(UnidadLectura.MBAR, UnidadLectura.HPA) {
        @Override
        public double convertir(double valor) {
            return valor;
        }
    },
    MBAR_PA(UnidadLectura.MBAR, UnidadLectura.PA) {
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
