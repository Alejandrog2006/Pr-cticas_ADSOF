package featurizers;

import models.Weather;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Extrae las features relevantes de un objeto Weather.
 */
public class WeatherFeaturizer implements Featurizer<Weather> {
    /**
     * Obtiene las condiciones meteorologicas del objeto.
     *
     * @param weather condicion meteorologica de entrada.
     * @return mapa con las features extraidas.
     */
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
