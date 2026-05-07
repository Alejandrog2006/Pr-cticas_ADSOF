package GR2272_AlejandroFernandoP5.src.models;

import GR2272_AlejandroFernandoP5.src.featurizers.Featurizer;
import GR2272_AlejandroFernandoP5.src.labellers.Labeller;
import java.util.*;
import java.util.function.Predicate;

/**
 * Dataset etiquetado que conserva tanto los items como su etiqueta asociada.
 *
 * @param <T> tipo de los elementos almacenados.
 */
public class LabeledDataset<T> {
    private final List<T> items = new ArrayList<>();
    private final Map<T, String> labels = new LinkedHashMap<>();
    private final Featurizer<T> featurizer;
    private final Labeller<T> labeller;

    public LabeledDataset(Featurizer<T> featurizer, Labeller<T> labeller) {
        this.featurizer = Objects.requireNonNull(featurizer);
        this.labeller = Objects.requireNonNull(labeller);
    }

    /**
     * Anade un elemento y guarda su etiqueta calculada.
     *
     * @param item elemento a insertar.
     */
    public void add(T item) {
        items.add(item);
        labels.put(item, labeller.label(item));
    }

    /**
     * Anade todos los elementos de un array.
     *
     * @param values elementos a insertar.
     */
    public void addAll(T[] values) {
        for (T value : values) add(value);
    }

    /**
     * Anade todos los elementos de un iterable.
     *
     * @param values elementos a insertar.
     */
    public void addAll(Iterable<? extends T> values) {
        for (T value : values) add(value);
    }

    /**
     * Obtiene la etiqueta asociada a un item.
     *
     * @param item elemento consultado.
     * @return etiqueta del elemento.
     */
    public String getLabel(T item) { return labels.get(item); }

    /**
     * Devuelve el numero de elementos almacenados.
     *
     * @return tamano del dataset.
     */
    public int size() { return items.size(); }

    /**
     * Devuelve una copia de los elementos.
     *
     * @return lista con los elementos.
     */
    public List<T> items() { return new ArrayList<>(items); }

    /**
     * Devuelve el featurizer asociado.
     *
     * @return featurizer del dataset.
     */
    public Featurizer<T> getFeaturizer() { return featurizer; }

    /**
     * Devuelve el labeller asociado.
     *
     * @return labeller del dataset.
     */
    public Labeller<T> getLabeller() { return labeller; }

    /**
     * Calcula la distribucion de etiquetas.
     *
     * @return mapa etiqueta -> frecuencia.
     */
    public Map<String, Integer> getLabelDistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        for (String label : labels.values()) {
            distribution.merge(label, 1, Integer::sum);
        }
        return distribution;
    }

    /**
     * Devuelve la etiqueta mayoritaria del dataset.
     *
     * @return etiqueta con mas ocurrencias, o null si esta vacio.
     */
    public String getMajorityLabel() {
        Map<String, Integer> distribution = getLabelDistribution();
        return distribution.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);
    }

    /**
     * Divide el dataset en dos subconjuntos segun un predicado.
     *
     * @param predicate condicion de particion.
     * @return mapa con las particiones true y false.
     */
    public Map<Boolean, LabeledDataset<T>> split(Predicate<T> predicate) {
        LabeledDataset<T> trueSet = new LabeledDataset<>(featurizer, labeller);
        LabeledDataset<T> falseSet = new LabeledDataset<>(featurizer, labeller);

        for (T item : items) {
            if (predicate.test(item)) trueSet.add(item);
            else falseSet.add(item);
        }

        Map<Boolean, LabeledDataset<T>> result = new LinkedHashMap<>();
        result.put(true, trueSet);
        result.put(false, falseSet);
        return result;
    }

    /**
     * Calcula la impureza Gini del dataset.
     *
     * @return valor Gini.
     */
    public double gini() {
        if (items.isEmpty()) return 0.0;
        Map<String, Integer> distribution = getLabelDistribution();
        double gini = 1.0;
        for (int count : distribution.values()) {
            double proportion = (double) count / items.size();
            gini -= proportion * proportion;
        }
        return gini;
    }

    /**
     * Calcula la ganancia de informacion de una particion.
     *
     * @param predicate particion a evaluar.
     * @return ganancia obtenida.
     */
    public double informationGain(Predicate<T> predicate) {
        double parentGini = gini();
        Map<Boolean, LabeledDataset<T>> split = split(predicate);
        LabeledDataset<T> trueSet = split.get(true);
        LabeledDataset<T> falseSet = split.get(false);

        if (trueSet.size() == 0 || falseSet.size() == 0) return 0.0;

        double totalSize = items.size();
        double weightedChildGini = (trueSet.size() / totalSize) * trueSet.gini() +
                                    (falseSet.size() / totalSize) * falseSet.gini();
        return parentGini - weightedChildGini;
    }

    /**
     * Devuelve un resumen textual del dataset etiquetado.
     *
     * @return representacion en cadena.
     */
    @Override
    public String toString() {
        return "LabeledDataset{size=" + items.size() + ", distribution=" + 
               getLabelDistribution() + '}';
    }
}
