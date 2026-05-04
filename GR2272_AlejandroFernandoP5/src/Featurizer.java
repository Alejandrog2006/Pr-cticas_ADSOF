package GR2272_AlejandroFernandoP5.src;

import java.util.Map;

public interface Featurizer<T> {
    Map<String, Object> features(T item);
}
             