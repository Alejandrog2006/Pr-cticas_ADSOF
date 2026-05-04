package GR2272_AlejandroFernandoP5.src;

import java.util.*;

public class GreedyTreeLearner<T> {
    private static final int MAX_DEPTH = 10;
    private static final double MIN_GAIN = 0.001;
    private final FeatureSelectionStrategy<T> strategy;

    public GreedyTreeLearner() {
        this(new GiniStrategy<>());
    }

    public GreedyTreeLearner(FeatureSelectionStrategy<T> strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy cannot be null");
    }

    public DecisionTree<T> learn(LabeledDataset<T> dataset) {
        Objects.requireNonNull(dataset, "dataset cannot be null");
        DecisionTree<T> tree = new DecisionTree<>();
        buildTree(tree, dataset, "root", 0);
        return tree;
    }

    private void buildTree(DecisionTree<T> tree, LabeledDataset<T> dataset, String nodeName, int depth) {
        // Crear el nodo
        tree.node(nodeName);

        // Criterios de parada
        if (dataset.size() == 0) {
            return;
        }

        if (isPure(dataset) || depth >= MAX_DEPTH) {
            return; // Es un nodo hoja (nodo terminal)
        }

        // Encontrar el mejor feature para dividir usando la estrategia
        FeatureSelectionStrategy.BestSplit<T> bestSplit = strategy.selectBestSplit(dataset);

        if (bestSplit == null) {
            return; // No hay mejora significativa
        }

        // Dividir el dataset
        Map<Boolean, LabeledDataset<T>> split = dataset.split(bestSplit.predicate);
        LabeledDataset<T> trueSet = split.get(true);
        LabeledDataset<T> falseSet = split.get(false);

        // Crear ramas
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
