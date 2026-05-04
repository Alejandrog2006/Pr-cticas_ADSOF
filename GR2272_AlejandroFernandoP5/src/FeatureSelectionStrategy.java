package GR2272_AlejandroFernandoP5.src;

import java.util.function.Predicate;

public interface FeatureSelectionStrategy<T> {
    /**
     * Selecciona la mejor característica (feature) para dividir el dataset.
     * Retorna null si no hay mejora significativa.
     */
    BestSplit<T> selectBestSplit(LabeledDataset<T> dataset);
    
    class BestSplit<T> {
        public final String featureName;
        public final Predicate<T> predicate;
        public final double score;

        public BestSplit(String featureName, Predicate<T> predicate, double score) {
            this.featureName = featureName;
            this.predicate = predicate;
            this.score = score;
        }
    }
}
