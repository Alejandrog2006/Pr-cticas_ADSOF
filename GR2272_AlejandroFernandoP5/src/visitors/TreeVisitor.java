package GR2272_AlejandroFernandoP5.src.visitors;

import GR2272_AlejandroFernandoP5.src.trees.DecisionTree;

/**
 * Visitante generico para representar o recorrer un arbol de decision.
 *
 * @param <T> tipo de los elementos almacenados en el arbol.
 */
public interface TreeVisitor<T> {
    /**
     * Visita un arbol de decision completo.
     *
     * @param tree arbol a visitar.
     */
    void visitDecisionTree(DecisionTree<T> tree);
}
