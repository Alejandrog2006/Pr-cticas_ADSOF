/*
 * Unidad de humedad relativa soportada por la estación.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.unidadLectura;

/**
 * Unidad de humedad relativa disponible.
 */
public enum UnidadHumedad implements UnidadLectura {
    PORCENTAJE("%");
    private String simbolo;

    /**
     * Crea la unidad de humedad con su símbolo.
     *
     * @param simbolo símbolo imprimible.
     */
    private UnidadHumedad(String simbolo) {
        this.simbolo = simbolo;
    }
    
    /**
     * Devuelve el símbolo de la unidad.
     *
     * @return símbolo de humedad.
     */
    @Override
    public String imprimirSimbolo() {
        return this.simbolo;
    }
}
