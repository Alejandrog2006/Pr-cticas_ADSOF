package trees;

import visitors.TreeVisitor;
import java.util.*;
import java.util.function.Predicate;

/**
 * Arbol de decision generico que permite construir nodos, predecir etiquetas
 * y recuperar predicados equivalentes a rutas del arbol.
 *
 * @param <T> tipo de los objetos clasificados por el arbol.
 */
public class DecisionTree<T> {
    private static final String UNRESOLVED_PREFIX = "UNRESOLVED@";

    /**
     * Rama interna que conecta un nodo con su sucesor.
     */
    private static final class Branch<T> {
        private final String nextNode;
        private final Predicate<T> condition;

        private Branch(String nextNode, Predicate<T> condition) {
            this.nextNode = nextNode;
            this.condition = condition;
        }
    }

    /**
     * Nodo interno del arbol con sus ramas y su caso otherwise.
     */
    private static final class Node<T> {
        private final List<Branch<T>> branches = new ArrayList<>();
        private String otherwiseNode;
    }

    /**
     * Resultado interno de una prediccion, incluyendo si quedo sin resolver.
     */
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

    /**
     * Selecciona o crea el nodo que se va a configurar a continuacion.
     *
     * @param nodeName nombre del nodo activo.
     * @return esta misma instancia para encadenar llamadas.
     */
    public DecisionTree<T> node(String nodeName) {
        if (nodeName==null || nodeName.isBlank()) {
            throw new IllegalArgumentException("nodeName cannot be null orblank");
        }

        nodes.computeIfAbsent(nodeName, ignored -> new Node<>());
        if (root == null) {
            root = nodeName;
        }
        editingNode = nodeName;
        return this;
    }

    /**
     * Anade una condicion al nodo activo.
     *
     * @param nextNode nombre del nodo destino.
     * @param condition condicion que debe cumplirse.
     * @return esta misma instancia para encadenar llamadas.
     */
    public DecisionTree<T> withCondition(String nextNode, Predicate<T> condition) {
        Objects.requireNonNull(condition, "condition cannot be null");
        Node<T> current = requireEditingNode();
        current.branches.add(new Branch<>(requireNodeName(nextNode), condition));
        nodes.computeIfAbsent(nextNode, ignored -> new Node<>());
        return this;
    }

    /**
     * Define el nodo destino cuando ninguna condicion anterior se cumple.
     *
     * @param nextNode nombre del nodo destino por defecto.
     * @return esta misma instancia para encadenar llamadas.
     */
    public DecisionTree<T> otherwise(String nextNode) {
        Node<T> current = requireEditingNode();
        current.otherwiseNode = requireNodeName(nextNode);
        nodes.computeIfAbsent(nextNode, ignored -> new Node<>());
        return this;
    }

    /**
     * Predice la etiqueta de un unico elemento.
     *
     * @param item elemento a clasificar.
     * @return etiqueta predicha.
     */
    public String predict(T item) {
        PredictionResult result = predictInternal(item, true);
        return result.label;
    }

    /**
     * Clasifica una coleccion de elementos y los agrupa por etiqueta.
     *
     * @param items elementos a clasificar.
     * @return mapa etiqueta -> elementos clasificados.
     */
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

    /**
     * Obtiene un predicado equivalente a la ruta del arbol que lleva a una etiqueta.
     *
     * @param label etiqueta destino.
     * @return predicado que reproduce la ruta hacia esa etiqueta.
     */
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

    /**
     * Determina el siguiente nodo a visitar segun las ramas del nodo actual.
     *
     * @param node nodo actual.
     * @param item elemento evaluado.
     * @return nombre del siguiente nodo, o null si ninguna rama coincide.
     */
    private String findNextNode(Node<T> node, T item) {
        for (Branch<T> branch : node.branches) {
            if (branch.condition.test(item)) {
                return branch.nextNode;
            }
        }
        return node.otherwiseNode;
    }

    /**
     * Busca un predicado recorriendo el arbol en profundidad.
     *
     * @param currentNodeName nodo actual.
     * @param label etiqueta objetivo.
     * @param pathPredicates predicados acumulados del camino.
     * @param recursionStack pila de deteccion de ciclos.
     * @return predicado equivalente a la ruta, o null si no existe.
     */
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

    /**
     * Crea un selector para una rama concreta, respetando el orden de prioridad.
     *
     * @param node nodo que contiene la rama.
     * @param branchIndex indice de la rama seleccionada.
     * @return predicado que representa esa rama.
     */
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

    /**
     * Crea el selector equivalente al caso otherwise del nodo.
     *
     * @param node nodo que contiene la clausula otherwise.
     * @return predicado que representa el caso otherwise.
     */
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

    /**
     * Combina varios predicados en un unico predicado conjunctivo.
     *
     * @param predicates predicados a combinar.
     * @return predicado compuesto.
     */
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

    /**
     * Devuelve el nodo actualmente seleccionado para edicion.
     *
     * @return nodo activo.
     */
    private Node<T> requireEditingNode() {
        if (editingNode == null) {
            throw new IllegalStateException("No active node. Call node(...) first");
        }
        return nodes.get(editingNode);
    }

    /**
     * Valida y devuelve un nombre de nodo.
     *
     * @param nodeName nombre a validar.
     * @return el mismo nombre si es valido.
     */
    private String requireNodeName(String nodeName) {
        Objects.requireNonNull(nodeName, "nodeName cannot be null");
        if (nodeName.isBlank()) {
            throw new IllegalArgumentException("nodeName cannot be blank");
        }
        return nodeName;
    }

    /**
     * Comprueba que el arbol no este vacio.
     */
    private void requireTree() {
        if (root == null) {
            throw new IllegalStateException("The decision tree is empty");
        }
    }

    /**
     * Acepta un visitante para recorrer o visualizar el arbol.
     *
     * @param visitor visitante a aplicar.
     * @param <V> tipo concreto del visitante.
     */
    public <V extends TreeVisitor<T>> void accept(V visitor) {
        visitor.visitDecisionTree(this);
    }

    /**
     * Devuelve una copia del mapa de nodos del arbol.
     *
     * @return copia de los nodos.
     */
    public Map<String, Node<T>> getNodes() {
        return new LinkedHashMap<>(nodes);
    }

    /**
     * Devuelve el nombre del nodo raiz.
     *
     * @return nombre del nodo raiz, o null si el arbol esta vacio.
     */
    public String getRootName() {
        return root;
    }

    /**
     * Informacion simplificada de un nodo para la visualizacion.
     */
    public static class NodeInfo {
        private final List<String> children;
        private final List<String> conditions;

        /**
         * Crea un resumen de nodo.
         *
         * @param children hijos del nodo.
         * @param conditions textos asociados a las ramas.
         */
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

    /**
     * Devuelve informacion resumida de un nodo por su nombre.
     *
     * @param nodeName nombre del nodo.
     * @return informacion de visualizacion del nodo.
     */
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
