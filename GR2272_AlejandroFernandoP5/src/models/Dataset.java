package models;

import featurizers.Featurizer;
import features.Feature;
import java.util.*;

/**
 * Coleccion generica de objetos con capacidad para extraer features por nombre.
 *
 * @param <T> tipo de los elementos almacenados.
 */
public class Dataset<T> {
    protected final Collection<T> items = new ArrayList<>();
    protected final Featurizer<T> featurizer;

    public Dataset() {
        this.featurizer = null;
    }

    public Dataset(Featurizer<T> featurizer) {
        this.featurizer = featurizer;
    }

    /**
     * Anade un elemento al dataset.
     *
     * @param item elemento a insertar.
     * @return siempre true, como en una coleccion de tipo lista.
     */
    public boolean add(T item) {
        items.add(item);
        return true;
    }

    /**
     * Anade todos los elementos de un array al dataset.
     *
     * @param values elementos a insertar.
     */
    public void addAll(T[] values) {
        for (T value : values) {
            items.add(value);
        }
    }

    /**
     * Anade todos los elementos de una coleccion iterable al dataset.
     *
     * @param values elementos a insertar.
     */
    public void addAll(Iterable<? extends T> values) {
        for (T value : values) {
            items.add(value);
        }
    }

    /**
     * Elimina los elementos duplicados preservando el orden de insercion.
     */
    public void removeDuplicates() {
        List<T> uniqueItems = new ArrayList<>(new LinkedHashSet<>(items));
        items.clear();
        items.addAll(uniqueItems);
    }

    /**
     * Extrae una feature concreta por su nombre.
     *
     * @param name nombre de la feature.
     * @return la feature con los valores encontrados.
     */
    @SuppressWarnings("unchecked")
    public <R extends Comparable<? super R>> Feature<R> feature(String name) {
        if (featurizer == null) {
            throw new IllegalStateException("Dataset has no featurizer");
        }

        Feature<R> result = new Feature<>();
        for (T item : items) {
            Map<String, Object> features = featurizer.features(item);
            Object value = features.get(name);
            if (value != null) {
                result.add((R) value);
            }
        }
        return result;
    }

    /**
     * Devuelve los elementos del dataset en una lista independiente.
     *
     * @return copia de los elementos.
     */
    public List<T> items() {
        return new ArrayList<>(items);
    }

    @Override
    public String toString() {
        return items.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Dataset<?> other)) {
            return false;
        }
        return Objects.equals(items, other.items);
    }
}
