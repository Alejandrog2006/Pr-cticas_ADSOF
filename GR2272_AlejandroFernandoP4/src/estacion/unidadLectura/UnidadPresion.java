/*
 * Unidades de presión soportadas por la estación.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.unidadLectura;

/**
 * Unidades de presión disponibles.
 */
public enum UnidadPresion implements UnidadLectura {
    HPA("hPa"), PA("Pa"), MBAR("mbar");
    
    private String simbolo;
    
    /**
     * Crea una unidad de presión con su símbolo.
     *
     * @param simbolo símbolo imprimible.
     */
    private UnidadPresion(String simbolo) {
        this.simbolo = simbolo;
    }

    /**
     * Devuelve el símbolo de la unidad.
     *
     * @return símbolo de presión.
     */
    @Override
    public String imprimirSimbolo() {
        return this.simbolo;
    }
}
