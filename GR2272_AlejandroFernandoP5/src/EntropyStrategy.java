package GR2272_AlejandroFernandoP5.src;

import java.util.*;
import java.util.function.Predicate;

public class EntropyStrategy<T> implements FeatureSelectionStrategy<T> {
    private static final double MIN_GAIN = 0.001;

    @Override
    public BestSplit<T> selectBestSplit(LabeledDataset<T> dataset) {
        BestSplit<T> bestSplit = null;
        double bestGain = MIN_GAIN;

        Map<String, Comparable<?>> sampleFeatures = getSampleFeatures(dataset);

        for (Map.Entry<String, Comparable<?>> feature : sampleFeatures.entrySet()) {
            String featureName = feature.getKey();
            Comparable<?> value = feature.getValue();

            Predicate<T> splitPredicate = createSplitPredicate(dataset, featureName, value);
            double gain = calculateEntropicGain(dataset, splitPredicate);

            if (gain > bestGain) {
                bestGain = gain;
                bestSplit = new BestSplit<>(featureName, splitPredicate, gain);
            }
        }

        return bestSplit;
    }

    private double calculateEntropicGain(LabeledDataset<T> dataset, Predicate<T> predicate) {
        double parentEntropy = calculateEntropy(dataset);
        
        Map<Boolean, LabeledDataset<T>> split = dataset.split(predicate);
        LabeledDataset<T> trueSet = split.get(true);
        LabeledDataset<T> falseSet = split.get(false);

        if (trueSet.size() == 0 || falseSet.size() == 0) {
            return 0.0;
        }

        double totalSize = dataset.size();
        double weightedChildEntropy = (trueSet.size() / totalSize) * calculateEntropy(trueSet) +
                                      (falseSet.size() / totalSize) * calculateEntropy(falseSet);

        return parentEntropy - weightedChildEntropy;
    }

    private double calculateEntropy(LabeledDataset<T> dataset) {
        if (dataset.size() == 0) {
            return 0.0;
        }

        Map<String, Integer> distribution = dataset.getLabelDistribution();
        double entropy = 0.0;

        for (int count : distribution.values()) {
            if (count > 0) {
                double proportion = (double) count / dataset.size();
                entropy -= proportion * Math.log(proportion) / Math.log(2);
            }
        }

        return entropy;
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
