package GR2272_AlejandroFernandoP5.src.visitors;

import GR2272_AlejandroFernandoP5.src.trees.DecisionTree;
import java.util.*;

/**
 * Visitante que imprime el arbol en formato de consola.
 *
 * @param <T> tipo de los elementos del arbol.
 */
public class ConsoleVisitor<T> implements TreeVisitor<T> {

    /**
     * Muestra el arbol por la salida estandar.
     *
     * @param tree arbol a visualizar.
     */
    @Override
    public void visitDecisionTree(DecisionTree<T> tree) {
        System.out.println("=== Decision Tree (Console Visualization) ===\n");
        if (tree.getRootName() != null) {
            visualizeNode(tree.getRootName(), 0, new HashSet<>());
        }
    }

    /**
     * Recorre un nodo para pintarlo con sangrado en consola.
     *
     * @param nodeName nombre del nodo actual.
     * @param depth profundidad de visualizacion.
     * @param visited nodos ya visitados para evitar ciclos.
     */
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

    /**
     * Imprime un texto con el sangrado indicado.
     *
     * @param depth nivel de sangrado.
     * @param text texto a imprimir.
     */
    private void printIndented(int depth, String text) {
        System.out.println("  ".repeat(depth) + text);
    }
}
