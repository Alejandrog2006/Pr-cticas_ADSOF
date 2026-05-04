package GR2272_AlejandroFernandoP5.src;

import java.util.*;
import java.util.function.Predicate;

public class ErrorRateStrategy<T> implements FeatureSelectionStrategy<T> {
    private static final double MIN_IMPROVEMENT = 0.001;

    @Override
    public BestSplit<T> selectBestSplit(LabeledDataset<T> dataset) {
        BestSplit<T> bestSplit = null;
        double bestScore = Double.MAX_VALUE;

        Map<String, Comparable<?>> sampleFeatures = getSampleFeatures(dataset);

        for (Map.Entry<String, Comparable<?>> feature : sampleFeatures.entrySet()) {
            String featureName = feature.getKey();
            Comparable<?> value = feature.getValue();

            Predicate<T> splitPredicate = createSplitPredicate(dataset, featureName, value);
            double score = calculateErrorRate(dataset, splitPredicate);

            if (score < bestScore) {
                bestScore = score;
                bestSplit = new BestSplit<>(featureName, splitPredicate, score);
            }
        }

        return bestSplit;
    }

    private double calculateErrorRate(LabeledDataset<T> dataset, Predicate<T> predicate) {
        Map<Boolean, LabeledDataset<T>> split = dataset.split(predicate);
        LabeledDataset<T> trueSet = split.get(true);
        LabeledDataset<T> falseSet = split.get(false);

        if (trueSet.size() == 0 || falseSet.size() == 0) {
            return Double.MAX_VALUE; // División no válida
        }

        // Contar errores en cada rama (elementos que no coinciden con la etiqueta mayoritaria)
        int trueErrors = countErrors(trueSet);
        int falseErrors = countErrors(falseSet);

        // Score es la suma de errores ponderada
        double totalSize = dataset.size();
        return ((trueSet.size() / totalSize) * trueErrors + 
                (falseSet.size() / totalSize) * falseErrors);
    }

    private int countErrors(LabeledDataset<T> dataset) {
        String majorityLabel = dataset.getMajorityLabel();
        int errors = 0;

        for (T item : dataset.items()) {
            if (!dataset.getLabel(item).equals(majorityLabel)) {
                errors++;
            }
        }

        return errors;
    }

    private Map<String, Comparable<?>> getSampleFeatures(LabeledDataset<T> dataset) {
        Map<String, Comparable<?>> features = new LinkedHashMap<>();

        if (dataset.size() == 0) {
            return features;
        }

        T sample = dataset.items().get(0);
        Map<String, Object> sampleFeatures = dataset.getFeaturizer().features(sample);

        for (Map.Entry<String, Object> entry : sampleFeatures.entrySet()) {
            if (entry.getValue() instanceof Comparable<?>) {
                features.put(entry.getKey(), (Comparable<?>) entry.getValue());
            }
        }

        return features;
    }

    private Predicate<T> createSplitPredicate(LabeledDataset<T> dataset, String featureName, Comparable<?> value) {
        return item -> {
            Map<String, Object> features = dataset.getFeaturizer().features(item);
            Object itemValue = features.get(featureName);
            if (itemValue == null) {
                return false;
            }
            if (!(itemValue instanceof Comparable<?>)) {
                return false;
            }
            @SuppressWarnings("unchecked")
            Comparable<Object> comparable = (Comparable<Object>) itemValue;
            return comparable.compareTo(value) >= 0;
        };
    }
}
