package GR2272_AlejandroFernandoP5.src.visitors;

import GR2272_AlejandroFernandoP5.src.trees.DecisionTree;
import java.util.*;

/**
 * Visitante que genera una representacion textual con indentacion.
 *
 * @param <T> tipo de los elementos del arbol.
 */
public class StringVisitor<T> implements TreeVisitor<T> {
    private StringBuilder result = new StringBuilder();
    private Set<String> visited = new HashSet<>();
    private DecisionTree<T> currentTree;

    /**
     * Imprime la estructura del arbol y guarda el resultado interno.
     *
     * @param tree arbol a visualizar.
     */
    @Override
    public void visitDecisionTree(DecisionTree<T> tree) {
        result = new StringBuilder();
        visited = new HashSet<>();
        currentTree = tree;
        
        System.out.println("=== Decision Tree Structure ===\n");
        
        if (tree.getRootName() != null) {
            visualizeNode(tree.getRootName(), 0);
        } else {
            System.out.println("(empty tree)");
        }
        
        System.out.println(result.toString());
    }

    /**
     * Devuelve el arbol como una cadena con formato de texto.
     *
     * @param tree arbol a representar.
     * @return representacion textual del arbol.
     */
    public String getTreeAsString(DecisionTree<T> tree) {
        result = new StringBuilder();
        visited = new HashSet<>();
        currentTree = tree;
        
        if (tree.getRootName() != null) {
            visualizeNode(tree.getRootName(), 0);
        }
        
        return result.toString();
    }

    /**
     * Recorre un nodo y lo anade al resultado textual.
     *
     * @param nodeName nombre del nodo actual.
     * @param depth profundidad de visualizacion.
     */
    private void visualizeNode(String nodeName, int depth) {
        if (visited.contains(nodeName)) {
            appendLine(depth, "└─ [CYCLE: " + nodeName + "]");
            return;
        }
        visited.add(nodeName);

        if (depth == 0) {
            appendLine(depth, "ROOT: " + nodeName);
        } else {
            appendLine(depth, "├─ " + nodeName);
        }

        DecisionTree.NodeInfo info = currentTree.getNodeInfo(nodeName);
        if (info != null) {
            for (String condition : info.getConditions()) {
                appendLine(depth + 1, condition);
            }

            for (String child : info.getChildren()) {
                visualizeNode(child, depth + 1);
            }
        }
    }

    /**
     * Anade una linea al resultado con el sangrado indicado.
     *
     * @param depth nivel de sangrado.
     * @param text texto a anadir.
     */
    private void appendLine(int depth, String text) {
        String indent = "  ".repeat(depth);
        result.append(indent).append(text).append("\n");
    }
}
