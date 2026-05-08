package strategies;

import models.LabeledDataset;
import java.util.*;
import java.util.function.Predicate;

/**
 * Estrategia basada en la ganancia de impureza Gini.
 *
 * @param <T> tipo de los elementos del dataset.
 */
public class GiniStrategy<T> implements FeatureSelectionStrategy<T> {
    private static final double MIN_GAIN = 0.001;

    /**
     * Busca el mejor corte maximizando la ganancia Gini.
     *
     * @param dataset dataset etiquetado de entrada.
     * @return mejor division candidata.
     */
    @Override
    public BestSplit<T> selectBestSplit(LabeledDataset<T> dataset) {
        BestSplit<T> bestSplit = null;
        double bestGain = MIN_GAIN;

        Map<String, Comparable<?>> sampleFeatures = getSampleFeatures(dataset);

        for (Map.Entry<String, Comparable<?>> feature : sampleFeatures.entrySet()) {
            String featureName = feature.getKey();
            Comparable<?> value = feature.getValue();

            Predicate<T> splitPredicate = createSplitPredicate(dataset, featureName, value);
            double gain = dataset.informationGain(splitPredicate);

            if (gain > bestGain) {
                bestGain = gain;
                bestSplit = new BestSplit<>(featureName, splitPredicate, gain);
            }
        }

        return bestSplit;
    }

    /**
     * Obtiene las features comparables del primer elemento del dataset.
     *
     * @param dataset dataset etiquetado de entrada.
     * @return mapa con las features comparables detectadas.
     */
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

    /**
     * Crea el predicado de division a partir de una feature y un valor de referencia.
     *
     * @param dataset dataset etiquetado de entrada.
     * @param featureName nombre de la feature a evaluar.
     * @param value valor de referencia.
     * @return predicado que separa los elementos segun esa feature.
     */
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
