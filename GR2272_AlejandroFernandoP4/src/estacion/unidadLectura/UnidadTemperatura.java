/*
 * Unidades de temperatura soportadas por la estación.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.unidadLectura;

/**
 * Unidades de temperatura disponibles.
 */
public enum UnidadTemperatura implements UnidadLectura {
    CELSIUS("Cº"), 
    FAHRENHEIT("Fº"), 
    KELVIN("K");

    /**
     * Crea una unidad de temperatura con su símbolo.
     *
     * @param simbolo símbolo imprimible.
     */
    private UnidadTemperatura(String simbolo) {
        this.simbolo = simbolo;
    }

    private String simbolo;

    /**
     * Devuelve el símbolo de la unidad.
     *
     * @return símbolo de temperatura.
     */
    @Override
    public String imprimirSimbolo() {
        return this.simbolo;
    }
}