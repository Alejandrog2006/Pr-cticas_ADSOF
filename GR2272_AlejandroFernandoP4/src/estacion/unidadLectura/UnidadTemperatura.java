package estacion.unidadLectura;

public enum UnidadTemperatura implements UnidadLectura {
    CELSIUS("Cº"), 
    FAHRENHEIT("Fº"), 
    KELVIN("K");

    private UnidadTemperatura(String simbolo) {
        this.simbolo = simbolo;
    }

    private String simbolo;

    @Override
    public String imprimirSimbolo() {
        return this.simbolo;
    }
}