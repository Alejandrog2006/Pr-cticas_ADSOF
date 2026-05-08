package visitors;

import trees.DecisionTree;
import java.util.*;

/**
 * Visitante que genera una representacion DOT compatible con Graphviz.
 *
 * @param <T> tipo de los elementos del arbol.
 */
public class GraphVizVisitor<T> implements TreeVisitor<T> {

    /**
     * Imprime el arbol en formato DOT por la salida estandar.
     *
     * @param tree arbol a visualizar.
     */
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

    /**
     * Muestra una ayuda para guardar y renderizar el DOT generado.
     *
     * @param tree arbol a visualizar.
     * @param filename nombre sugerido del archivo DOT.
     */
    public void visualizeToFile(DecisionTree<T> tree, String filename) {
        System.out.println("// Save this DOT code to " + filename + "\n");
        visitDecisionTree(tree);
        System.out.println("\n// To visualize, run:");
        System.out.println("// dot -Tpng " + filename + " -o output.png");
    }

    /**
     * Genera recursivamente el texto DOT del arbol.
     *
     * @param tree arbol a representar.
     * @param nodeName nombre del nodo actual.
     * @param visited nodos ya visitados.
     * @param sb acumulador del DOT generado.
     */
    private void generateDot(DecisionTree<T> tree, String nodeName, 
                            Set<String> visited, StringBuilder sb) {
        if (visited.contains(nodeName)) {
            return;
        }
        visited.add(nodeName);

        String nodeLabel = nodeName.length() > 30 ? 
            nodeName.substring(0, 27) + "..." : nodeName;
        sb.append("    ").append(sanitize(nodeName))
          .append(" [label=\"").append(nodeLabel).append("\"];\n");

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

    /**
     * Convierte un nombre arbitrario en un identificador valido para DOT.
     *
     * @param name nombre original.
     * @return identificador sanitizado.
     */
    private String sanitize(String name) {
        return "node_" + name.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    /**
     * Limpia el texto de una condicion para usarlo como etiqueta de arista.
     *
     * @param condition texto de la condicion.
     * @return texto simplificado.
     */
    private String cleanCondition(String condition) {
        if (condition.contains("->")) {
            return condition.split("->")[0].trim();
        }
        return condition;
    }
}
