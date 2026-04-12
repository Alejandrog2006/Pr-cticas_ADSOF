/*
 * Estrategias para generar valores de sensores.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor.estrategia;

/**
 * Define una estrategia para generar valores de lectura.
 */
public interface Estrategia {
    /**
     * Genera un valor de lectura dentro o fuera del rango indicado.
     *
     * @param min límite inferior recomendado.
     * @param max límite superior recomendado.
     * @return valor generado.
     */
    public double generarValor(double min, double max);
}