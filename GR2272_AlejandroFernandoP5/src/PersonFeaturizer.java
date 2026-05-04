package GR2272_AlejandroFernandoP5.src;

import java.util.LinkedHashMap;
import java.util.Map;

public class PersonFeaturizer implements Featurizer<Person> {
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