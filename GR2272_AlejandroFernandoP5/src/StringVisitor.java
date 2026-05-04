package GR2272_AlejandroFernandoP5.src;

import java.util.*;

public class StringVisitor<T> implements TreeVisitor<T> {
    private StringBuilder result = new StringBuilder();
    private Set<String> visited = new HashSet<>();
    private DecisionTree<T> currentTree;

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

    public String getTreeAsString(DecisionTree<T> tree) {
        result = new StringBuilder();
        visited = new HashSet<>();
        currentTree = tree;
        
        if (tree.getRootName() != null) {
            visualizeNode(tree.getRootName(), 0);
        }
        
        return result.toString();
    }

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

        // Obtener información de los nodos hijos
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

    private void appendLine(int depth, String text) {
        String indent = "  ".repeat(depth);
        result.append(indent).append(text).append("\n");
    }
}
