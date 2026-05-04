package GR2272_AlejandroFernandoP5.src;

import java.util.*;

public class Dataset<T> {
    private final Collection<T> items = new ArrayList<>();
    private final Featurizer<T> featurizer;

    public Dataset() {
        this.featurizer = null;
    }

    public Dataset(Featurizer<T> featurizer) {
        this.featurizer = featurizer;
    }

    public boolean add(T item) {
        items.add(item);
        return true;
    }

    public void addAll(T[] values) {
        for (T value : values) {
            items.add(value);
        }
    }

    public void addAll(Iterable<? extends T> values) {
        for (T value : values) {
            items.add(value);
        }
    }

    public void removeDuplicates() {
        List<T> uniqueItems = new ArrayList<>(new LinkedHashSet<>(items));
        items.clear();
        items.addAll(uniqueItems);
    }

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
            return true; // el objeto es el mismo
        }
        if (!(obj instanceof Dataset<?> other)) {
            return false; // el objeto no es de clase dataset
        }
        return Objects.equals(items, other.items); // si los objetos de tu dataset son los mismos, son el mismo dataset
    }
}