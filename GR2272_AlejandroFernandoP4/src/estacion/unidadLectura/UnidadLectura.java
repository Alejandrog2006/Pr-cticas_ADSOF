/*
 * Interfaz para las unidades de lectura soportadas por el sistema.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.unidadLectura;

/**
 * Representa una unidad de lectura imprimible.
 */
public interface UnidadLectura {
    /**
     * Devuelve el símbolo de la unidad.
     *
     * @return símbolo de la unidad.
     */
    String imprimirSimbolo(); 
}
