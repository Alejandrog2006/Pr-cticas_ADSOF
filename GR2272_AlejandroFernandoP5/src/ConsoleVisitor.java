package GR2272_AlejandroFernandoP5.src;

import java.util.*;

public class ConsoleVisitor<T> implements TreeVisitor<T> {

    @Override
    public void visitDecisionTree(DecisionTree<T> tree) {
        System.out.println("=== Decision Tree (Console Visualization) ===\n");
        if (tree.getRootName() != null) {
            visualizeNode(tree.getRootName(), 0, new HashSet<>());
        }
    }

    private void visualizeNode(String nodeName, int depth, Set<String> visited) {
        if (visited.contains(nodeName)) {
            printIndented(depth, "└─ [CYCLE: " + nodeName + "]");
            return;
        }
        visited.add(nodeName);

        if (depth == 0) {
            System.out.println("ROOT: " + nodeName);
        } else {
            System.out.println("  ".repeat(depth) + "├─ " + nodeName);
        }
    }

    private void printIndented(int depth, String text) {
        System.out.println("  ".repeat(depth) + text);
    }
}

