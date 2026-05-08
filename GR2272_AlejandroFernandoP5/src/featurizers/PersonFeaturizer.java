package featurizers;

import models.Person;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Extrae las features relevantes de un objeto Person.
 */
public class PersonFeaturizer implements Featurizer<Person> {
    /**
     * Obtiene nombre, edad, peso, altura y genero de la persona.
     *
     * @param person persona de entrada.
     * @return mapa con las features extraidas.
     */
    @Override
    public Map<String, Object> features(Person person) {
        Map<String, Object> features = new LinkedHashMap<>();
        features.put("name", person.getName());
        features.put("age", person.getAge());
        features.put("weight", person.getWeight());
        features.put("height", person.getHeight());
        features.put("gender", person.isMale() ? "MALE" : "FEMALE");
        return features;
    }
}
