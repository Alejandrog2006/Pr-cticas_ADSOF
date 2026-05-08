package labellers;

/**
 * Asigna una etiqueta a un objeto de tipo parametrico.
 *
 * @param <T> tipo del objeto a etiquetar.
 */
public interface Labeller<T> {
    /**
     * Devuelve la etiqueta asociada a un objeto.
     *
     * @param item objeto a clasificar.
     * @return etiqueta resultante.
     */
    String label(T item);
}
