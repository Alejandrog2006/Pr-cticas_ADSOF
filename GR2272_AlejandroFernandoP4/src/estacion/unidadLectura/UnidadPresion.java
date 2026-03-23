package estacion.unidadLectura;

public enum UnidadPresion implements UnidadLectura {
    HPA("hPa"), PA("Pa"), MBAR("mbar");
    
    private String simbolo;
    
    private UnidadPresion(String simbolo) {
        this.simbolo = simbolo;
    }

    @Override
    public String imprimirSimbolo() {
        return this.simbolo;
    }
}
