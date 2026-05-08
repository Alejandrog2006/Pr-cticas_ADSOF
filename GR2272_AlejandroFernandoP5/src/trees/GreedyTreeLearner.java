package trees;

import models.LabeledDataset;
import strategies.FeatureSelectionStrategy;
import strategies.GiniStrategy;
import java.util.*;

/**
 * Aprende un arbol de decision de forma recursiva a partir de un dataset etiquetado.
 *
 * @param <T> tipo de los elementos del dataset.
 */
public class GreedyTreeLearner<T> {
    private static final int MAX_DEPTH = 10;
    private static final double MIN_GAIN = 0.001;
    private final FeatureSelectionStrategy<T> strategy;

    /**
     * Crea un learner que usa Gini por defecto.
     */
    public GreedyTreeLearner() {
        this(new GiniStrategy<>());
    }

    /**
     * Crea un learner parametrizado por una estrategia concreta.
     *
     * @param strategy estrategia de seleccion de features.
     */
    public GreedyTreeLearner(FeatureSelectionStrategy<T> strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy cannot be null");
    }

    /**
     * Aprende un arbol de decision a partir del dataset dado.
     *
     * @param dataset dataset etiquetado de entrada.
     * @return arbol de decision aprendido.
     */
    public DecisionTree<T> learn(LabeledDataset<T> dataset) {
        Objects.requireNonNull(dataset, "dataset cannot be null");
        DecisionTree<T> tree = new DecisionTree<>();
        buildTree(tree, dataset, "root", 0);
        return tree;
    }

    /**
     * Construye recursivamente el arbol a partir de un subconjunto etiquetado.
     *
     * @param tree arbol que se esta construyendo.
     * @param dataset subconjunto actual.
     * @param nodeName nombre del nodo que se esta configurando.
     * @param depth profundidad actual de recursion.
     */
    private void buildTree(DecisionTree<T> tree, LabeledDataset<T> dataset, String nodeName, int depth) {
        tree.node(nodeName);

        if (dataset.size() == 0) {
            return;
        }

        if (isPure(dataset) || depth >= MAX_DEPTH) {
            return;
        }

        FeatureSelectionStrategy.BestSplit<T> bestSplit = strategy.selectBestSplit(dataset);

        if (bestSplit == null) {
            return;
        }

        Map<Boolean, LabeledDataset<T>> split = dataset.split(bestSplit.predicate);
        LabeledDataset<T> trueSet = split.get(true);
        LabeledDataset<T> falseSet = split.get(false);

        if (trueSet.size() > 0) {
            String trueBranchName = nodeName + "_true_" + bestSplit.featureName;
            tree.withCondition(trueBranchName, bestSplit.predicate);
            buildTree(tree, trueSet, trueBranchName, depth + 1);
        }

        if (falseSet.size() > 0) {
            String falseBranchName = nodeName + "_false_" + bestSplit.featureName;
            tree.otherwise(falseBranchName);
            buildTree(tree, falseSet, falseBranchName, depth + 1);
        }
    }

    /**
     * Comprueba si todos los elementos del dataset comparten la misma etiqueta.
     *
     * @param dataset subconjunto etiquetado.
     * @return true si el subconjunto es puro.
     */
    private boolean isPure(LabeledDataset<T> dataset) {
        if (dataset.size() == 0) {
            return true;
        }

        String firstLabel = dataset.getLabel(dataset.items().get(0));
        for (T item : dataset.items()) {
            if (!dataset.getLabel(item).equals(firstLabel)) {
                return false;
            }
        }
        return true;
    }
}
