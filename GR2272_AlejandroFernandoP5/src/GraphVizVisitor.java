package GR2272_AlejandroFernandoP5.src;

import java.util.*;

public class GraphVizVisitor<T> implements TreeVisitor<T> {

    @Override
    public void visitDecisionTree(DecisionTree<T> tree) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph DecisionTree {\n");
        sb.append("    rankdir=TB;\n");
        sb.append("    node [shape=box, style=rounded, fontname=\"Arial\"];\n");
        sb.append("    edge [fontname=\"Arial\"];\n\n");

        if (tree.getRootName() != null) {
            Set<String> visited = new HashSet<>();
            generateDot(tree, tree.getRootName(), visited, sb);
        }

        sb.append("}\n");
        System.out.println(sb.toString());
    }

    public void visualizeToFile(DecisionTree<T> tree, String filename) {
        System.out.println("// Save this DOT code to " + filename + "\n");
        visitDecisionTree(tree);
        System.out.println("\n// To visualize, run:");
        System.out.println("// dot -Tpng " + filename + " -o output.png");
    }

    private void generateDot(DecisionTree<T> tree, String nodeName, 
                            Set<String> visited, StringBuilder sb) {
        if (visited.contains(nodeName)) {
            return;
        }
        visited.add(nodeName);

        // Dibujar el nodo
        String nodeLabel = nodeName.length() > 30 ? 
            nodeName.substring(0, 27) + "..." : nodeName;
        sb.append("    ").append(sanitize(nodeName))
          .append(" [label=\"").append(nodeLabel).append("\"];\n");

        // Obtener información del nodo y dibujar conexiones
        DecisionTree.NodeInfo info = tree.getNodeInfo(nodeName);
        if (info != null) {
            int index = 0;
            for (String condition : info.getConditions()) {
                String childNode = info.getChildren().get(index);
                sb.append("    ").append(sanitize(nodeName))
                  .append(" -> ").append(sanitize(childNode))
                  .append(" [label=\"").append(cleanCondition(condition)).append("\"];\n");
                generateDot(tree, childNode, visited, sb);
                index++;
            }
        }
    }

    private String sanitize(String name) {
        return "node_" + name.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private String cleanCondition(String condition) {
        // Simplificar el texto de la condición
        if (condition.contains("->")) {
            return condition.split("->")[0].trim();
        }
        return condition;
    }
}

