package GR2272_AlejandroFernandoP5.src.strategies;

import GR2272_AlejandroFernandoP5.src.models.LabeledDataset;
import java.util.*;
import java.util.function.Predicate;

/**
 * Estrategia basada en minimizar la tasa de error de clasificacion.
 *
 * @param <T> tipo de los elementos del dataset.
 */
public class ErrorRateStrategy<T> implements FeatureSelectionStrategy<T> {
    private static final double MIN_IMPROVEMENT = 0.001;

    /**
     * Busca el mejor corte minimizando el error esperado.
     *
     * @param dataset dataset etiquetado de entrada.
     * @return mejor division candidata.
     */
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

    /**
     * Calcula la tasa de error esperada de una particion.
     *
     * @param dataset dataset etiquetado de entrada.
     * @param predicate predicado de division.
     * @return tasa de error estimada.
     */
    private double calculateErrorRate(LabeledDataset<T> dataset, Predicate<T> predicate) {
        Map<Boolean, LabeledDataset<T>> split = dataset.split(predicate);
        LabeledDataset<T> trueSet = split.get(true);
        LabeledDataset<T> falseSet = split.get(false);

        if (trueSet.size() == 0 || falseSet.size() == 0) {
            return Double.MAX_VALUE;
        }

        int trueErrors = countErrors(trueSet);
        int falseErrors = countErrors(falseSet);

        double totalSize = dataset.size();
        return ((trueSet.size() / totalSize) * trueErrors + 
                (falseSet.size() / totalSize) * falseErrors);
    }

    /**
     * Cuenta cuantos elementos no coinciden con la etiqueta mayoritaria.
     *
     * @param dataset subconjunto etiquetado.
     * @return numero de errores respecto a la etiqueta mayoritaria.
     */
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
