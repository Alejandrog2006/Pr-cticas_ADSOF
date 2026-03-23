package estacion.unidadLectura;

public enum UnidadHumedad implements UnidadLectura {
    PORCENTAJE("%");
    private String simbolo;

    private UnidadHumedad(String simbolo) {
        this.simbolo = simbolo;
    }
    
    @Override
    public String imprimirSimbolo() {
        return this.simbolo;
    }
}
