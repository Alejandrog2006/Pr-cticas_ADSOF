package GR2272_AlejandroFernandoP5.src;

import java.util.*;
import java.util.function.Predicate;

public class DecisionTree<T> {
    private static final String UNRESOLVED_PREFIX = "UNRESOLVED@";

    private static final class Branch<T> {
        private final String nextNode;
        private final Predicate<T> condition;

        private Branch(String nextNode, Predicate<T> condition) {
            this.nextNode = nextNode;
            this.condition = condition;
        }
    }

    private static final class Node<T> {
        private final List<Branch<T>> branches = new ArrayList<>();
        // Si no se cumple la condición se va al nodo otherwise
        private String otherwiseNode;
    }

    private static final class PredictionResult {
        private final String label;
        private final boolean unresolved;

        private PredictionResult(String label, boolean unresolved) {
            this.label = label;
            this.unresolved = unresolved;
        }
    }

    private final Map<String, Node<T>> nodes = new LinkedHashMap<>();
    private String root;
    private String editingNode;

    public DecisionTree<T> node(String nodeName) {
        Objects.requireNonNull(nodeName, "nodeName cannot be null");
        if (nodeName.isBlank()) {
            throw new IllegalArgumentException("nodeName cannot be blank");
        }

        nodes.computeIfAbsent(nodeName, ignored -> new Node<>());
        if (root == null) {
            root = nodeName;
        }
        editingNode = nodeName;
        return this;
    }

    public DecisionTree<T> withCondition(String nextNode, Predicate<T> condition) {
        Objects.requireNonNull(condition, "condition cannot be null");
        Node<T> current = requireEditingNode();
        current.branches.add(new Branch<>(requireNodeName(nextNode), condition));
        nodes.computeIfAbsent(nextNode, ignored -> new Node<>());
        return this;
    }

    public DecisionTree<T> otherwise(String nextNode) {
        Node<T> current = requireEditingNode();
        current.otherwiseNode = requireNodeName(nextNode);
        nodes.computeIfAbsent(nextNode, ignored -> new Node<>());
        return this;
    }

    public String predict(T item) {
        PredictionResult result = predictInternal(item, true);
        return result.label;
    }

    public Map<String, List<T>> predict(Dataset<T> dataSet) {
        Objects.requireNonNull(dataSet, "dataSet cannot be null");
        return predict(dataSet.items());
    }

    public Map<String, List<T>> predict(Iterable<? extends T> items) {
        Objects.requireNonNull(items, "items cannot be null");

        Map<String, List<T>> result = new LinkedHashMap<>();
        for (T item : items) {
            PredictionResult prediction = predictInternal(item, false);
            String key = prediction.unresolved ? UNRESOLVED_PREFIX + prediction.label : prediction.label;
            result.computeIfAbsent(key, ignored -> new ArrayList<>()).add(item);
        }
        return result;
    }

    public Predicate<T> getPredicate(String label) {
        Objects.requireNonNull(label, "label cannot be null");
        if (label.isBlank()) {
            throw new IllegalArgumentException("label cannot be blank");
        }
        requireTree();

        Predicate<T> predicate = findPredicateDepthFirst(root, label, new ArrayList<>(), new HashSet<>());
        if (predicate == null) {
            throw new IllegalArgumentException("Label '" + label + "' not found from root node '" + root + "'");
        }
        return predicate;
    }

    private PredictionResult predictInternal(T item, boolean failOnUnresolved) {
        Objects.requireNonNull(item, "item cannot be null");
        requireTree();

        String currentNodeName = root;
        Set<String> visited = new LinkedHashSet<>();

        while (true) {
            if (!visited.add(currentNodeName)) {
                throw new IllegalStateException("Cycle detected while predicting at node '" + currentNodeName + "'");
            }

            Node<T> current = nodes.get(currentNodeName);
            if (current == null) {
                throw new IllegalStateException("Unknown node '" + currentNodeName + "'");
            }

            if (current.branches.isEmpty() && current.otherwiseNode == null) {
                return new PredictionResult(currentNodeName, false);
            }

            String nextNodeName = findNextNode(current, item);
            if (nextNodeName == null) {
                if (failOnUnresolved) {
                    throw new IllegalStateException("No condition matched at node '" + currentNodeName + "' for item: " + item);
                }
                return new PredictionResult(currentNodeName, true);
            }
            currentNodeName = nextNodeName;
        }
    }

    private String findNextNode(Node<T> node, T item) {
        for (Branch<T> branch : node.branches) {
            if (branch.condition.test(item)) {
                return branch.nextNode;
            }
        }
        return node.otherwiseNode;
    }

    private Predicate<T> findPredicateDepthFirst(
            String currentNodeName,
            String label,
            List<Predicate<T>> pathPredicates,
            Set<String> recursionStack) {
        if (!recursionStack.add(currentNodeName)) {
            throw new IllegalStateException("Cycle detected while generating predicate at node '" + currentNodeName + "'");
        }

        try {
            if (currentNodeName.equals(label)) {
                return compose(new ArrayList<>(pathPredicates));
            }

            Node<T> current = nodes.get(currentNodeName);
            if (current == null) {
                throw new IllegalStateException("Unknown node '" + currentNodeName + "'");
            }

            for (int branchIndex = 0; branchIndex < current.branches.size(); branchIndex++) {
                Branch<T> branch = current.branches.get(branchIndex);

                pathPredicates.add(buildBranchSelector(current, branchIndex));
                Predicate<T> result = findPredicateDepthFirst(branch.nextNode, label, pathPredicates, recursionStack);
                pathPredicates.remove(pathPredicates.size() - 1);

                if (result != null) {
                    return result;
                }
            }

            if (current.otherwiseNode != null) {
                pathPredicates.add(buildOtherwiseSelector(current));
                Predicate<T> result = findPredicateDepthFirst(current.otherwiseNode, label, pathPredicates, recursionStack);
                pathPredicates.remove(pathPredicates.size() - 1);

                if (result != null) {
                    return result;
                }
            }

            return null;
        } finally {
            recursionStack.remove(currentNodeName);
        }
    }

    private Predicate<T> buildBranchSelector(Node<T> node, int branchIndex) {
        return item -> {
            for (int i = 0; i < branchIndex; i++) {
                if (node.branches.get(i).condition.test(item)) {
                    return false;
                }
            }
            return node.branches.get(branchIndex).condition.test(item);
        };
    }

    private Predicate<T> buildOtherwiseSelector(Node<T> node) {
        return item -> {
            for (Branch<T> branch : node.branches) {
                if (branch.condition.test(item)) {
                    return false;
                }
            }
            return true;
        };
    }

    private Predicate<T> compose(List<Predicate<T>> predicates) {
        return item -> {
            for (Predicate<T> predicate : predicates) {
                if (!predicate.test(item)) {
                    return false;
                }
            }
            return true;
        };
    }

    private Node<T> requireEditingNode() {
        if (editingNode == null) {
            throw new IllegalStateException("No active node. Call node(...) first");
        }
        return nodes.get(editingNode);
    }

    private String requireNodeName(String nodeName) {
        Objects.requireNonNull(nodeName, "nodeName cannot be null");
        if (nodeName.isBlank()) {
            throw new IllegalArgumentException("nodeName cannot be blank");
        }
        return nodeName;
    }

    private void requireTree() {
        if (root == null) {
            throw new IllegalStateException("The decision tree is empty");
        }
    }

    public <V extends TreeVisitor<T>> void accept(V visitor) {
        visitor.visitDecisionTree(this);
    }

    public Map<String, Node<T>> getNodes() {
        return new LinkedHashMap<>(nodes);
    }

    public String getRootName() {
        return root;
    }

    // Información pública de un nodo
    public static class NodeInfo {
        private final List<String> children;
        private final List<String> conditions;

        public NodeInfo(List<String> children, List<String> conditions) {
            this.children = children;
            this.conditions = conditions;
        }

        public List<String> getChildren() {
            return children;
        }

        public List<String> getConditions() {
            return conditions;
        }
    }

    public NodeInfo getNodeInfo(String nodeName) {
        Node<T> node = nodes.get(nodeName);
        if (node == null) {
            return new NodeInfo(Collections.emptyList(), Collections.emptyList());
        }

        List<String> children = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        for (Branch<T> branch : node.branches) {
            children.add(branch.nextNode);
            conditions.add("condition -> " + branch.nextNode);
        }

        if (node.otherwiseNode != null) {
            children.add(node.otherwiseNode);
            conditions.add("otherwise -> " + node.otherwiseNode);
        }

        return new NodeInfo(children, conditions);
    }
}
