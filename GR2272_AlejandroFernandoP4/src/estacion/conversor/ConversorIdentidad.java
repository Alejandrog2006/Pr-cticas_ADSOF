/*
 * Conversor que devuelve el mismo valor sin cambios.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;

/**
 * Conversor que no altera el valor, útil como conversor por defecto.
 */
public class ConversorIdentidad implements Conversor {
    private final UnidadLectura unidad;

    /**
     * Crea un conversor identidad para una unidad concreta.
     *
     * @param unidad unidad de lectura asociada.
     */
    public ConversorIdentidad(UnidadLectura unidad) {
        this.unidad = unidad;
    }

    /**
     * Devuelve el mismo valor recibido.
     *
     * @param valor valor a convertir.
     * @return el mismo valor.
     */
    @Override
    public double convertir(double valor) {
        return valor;
    }

    /**
     * Devuelve la unidad de entrada.
     *
     * @return unidad inicial.
     */
    @Override
    public UnidadLectura unidadInicial() {
        return unidad;
    }

    /**
     * Devuelve la unidad de salida.
     *
     * @return unidad final.
     */
    @Override
    public UnidadLectura unidadFinal() {
        return unidad;
    }
}
