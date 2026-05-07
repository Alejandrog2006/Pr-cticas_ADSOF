package GR2272_AlejandroFernandoP5.src.featurizers;

import java.util.Map;

/**
 * Extrae un conjunto de features a partir de un objeto de tipo parametrico.
 *
 * @param <T> tipo del objeto de entrada.
 */
public interface Featurizer<T> {
    /**
     * Obtiene las features de un elemento.
     *
     * @param item objeto a analizar.
     * @return mapa con el nombre de cada feature y su valor.
     */
    Map<String, Object> features(T item);
}
