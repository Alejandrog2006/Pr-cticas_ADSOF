package GR2272_AlejandroFernandoP5.src;

import java.util.LinkedHashMap;
import java.util.Map;

public class WeatherFeaturizer implements Featurizer<Weather> {
    @Override
    public Map<String, Object> features(Weather weather) {
        Map<String, Object> features = new LinkedHashMap<>();
        features.put("outlook", weather.getOutlook());
        features.put("temperature", weather.getTemperature());
        features.put("humidity", weather.getHumidity());
        features.put("windy", weather.isWindy());
        return features;
    }
}
