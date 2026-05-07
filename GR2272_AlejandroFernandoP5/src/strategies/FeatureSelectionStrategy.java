package GR2272_AlejandroFernandoP5.src.strategies;

import GR2272_AlejandroFernandoP5.src.models.LabeledDataset;

/**
 * Estrategia para elegir la mejor feature al dividir un dataset etiquetado.
 *
 * @param <T> tipo de los elementos del dataset.
 */
public interface FeatureSelectionStrategy<T> {
	/**
	 * Selecciona el mejor corte posible para un dataset.
	 *
	 * @param dataset dataset etiquetado de entrada.
	 * @return el mejor corte encontrado, o null si no hay ninguno util.
	 */
    BestSplit<T> selectBestSplit(LabeledDataset<T> dataset);

    /**
     * Resultado de una division candidata.
     *
     * @param <T> tipo de los elementos evaluados.
     */
    class BestSplit<T> {
        /** Nombre de la feature candidata. */
        public final String featureName;
        /** Predicado que implementa el corte. */
        public final java.util.function.Predicate<T> predicate;
        /** Puntuacion asociada al corte. */
        public final double score;

        /**
         * Crea un resultado de division.
         *
         * @param featureName nombre de la feature.
         * @param predicate predicado de corte.
         * @param score puntuacion calculada.
         */
        public BestSplit(String featureName, java.util.function.Predicate<T> predicate, double score) {
            this.featureName = featureName;
            this.predicate = predicate;
            this.score = score;
        }
    }
}
