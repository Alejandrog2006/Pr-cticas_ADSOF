package GR2272_AlejandroFernandoP5.src;

import java.util.*;

public class LabeledDataset<T> {
    private final List<T> items = new ArrayList<>();
    private final Map<T, String> labels = new LinkedHashMap<>();
    private final Featurizer<T> featurizer;
    private final Labeller<T> labeller;

    public LabeledDataset(Featurizer<T> featurizer, Labeller<T> labeller) {
        this.featurizer = Objects.requireNonNull(featurizer, "featurizer cannot be null");
        this.labeller = Objects.requireNonNull(labeller, "labeller cannot be null");
    }

    public void add(T item) {
        items.add(item);
        labels.put(item, labeller.label(item));
    }

    public void addAll(T[] values) {
        for (T value : values) {
            add(value);
        }
    }

    public void addAll(Iterable<? extends T> values) {
        for (T value : values) {
            add(value);
        }
    }

    public String getLabel(T item) {
        return labels.get(item);
    }

    public int size() {
        return items.size();
    }

    public List<T> items() {
        return new ArrayList<>(items);
    }

    public Featurizer<T> getFeaturizer() {
        return featurizer;
    }

    public Labeller<T> getLabeller() {
        return labeller;
    }

    // Obtiene un mapa de labels con sus frecuencias
    public Map<String, Integer> getLabelDistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        for (String label : labels.values()) {
            distribution.merge(label, 1, Integer::sum);
        }
        return distribution;
    }

    // Obtiene la etiqueta más frecuente
    public String getMajorityLabel() {
        Map<String, Integer> distribution = getLabelDistribution();
        return distribution.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Divide el dataset según un predicado
    public Map<Boolean, LabeledDataset<T>> split(java.util.function.Predicate<T> predicate) {
        LabeledDataset<T> trueSet = new LabeledDataset<>(featurizer, labeller);
        LabeledDataset<T> falseSet = new LabeledDataset<>(featurizer, labeller);

        for (T item : items) {
            if (predicate.test(item)) {
                trueSet.add(item);
            } else {
                falseSet.add(item);
            }
        }

        Map<Boolean, LabeledDataset<T>> result = new LinkedHashMap<>();
        result.put(true, trueSet);
        result.put(false, falseSet);
        return result;
    }

    // Calcula el gini de este dataset
    public double gini() {
        if (items.isEmpty()) {
            return 0.0;
        }
        Map<String, Integer> distribution = getLabelDistribution();
        double gini = 1.0;
        for (int count : distribution.values()) {
            double proportion = (double) count / items.size();
            gini -= proportion * proportion;
        }
        return gini;
    }

    // Calcula la ganancia de información de una división
    public double informationGain(java.util.function.Predicate<T> predicate) {
        double parentGini = gini();
        Map<Boolean, LabeledDataset<T>> split = split(predicate);
        LabeledDataset<T> trueSet = split.get(true);
        LabeledDataset<T> falseSet = split.get(false);

        if (trueSet.size() == 0 || falseSet.size() == 0) {
            return 0.0; // No hay división
        }

        double totalSize = items.size();
        double weightedChildGini = (trueSet.size() / totalSize) * trueSet.gini() +
                                    (falseSet.size() / totalSize) * falseSet.gini();

        return parentGini - weightedChildGini;
    }

    @Override
    public String toString() {
        return "LabeledDataset{" +
                "size=" + items.size() +
                ", distribution=" + getLabelDistribution() +
                '}';
    }
}
