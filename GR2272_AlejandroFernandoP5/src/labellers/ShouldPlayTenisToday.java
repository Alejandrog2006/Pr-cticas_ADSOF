package labellers;

import models.Weather;

/**
 * Etiquetador de ejemplo para el clasico conjunto de datos meteorologicos.
 */
public class ShouldPlayTenisToday implements Labeller<Weather> {
    /**
     * Devuelve si debe jugarse al tenis segun las condiciones meteorologicas.
     *
     * @param weather objeto Weather a etiquetar.
     * @return "yes" o "no" segun las reglas definidas.
     */
    @Override
    public String label(Weather weather) {
        if ("rainy".equals(weather.getOutlook()) && weather.isWindy()) return "no";
        if ("sunny".equals(weather.getOutlook()) && weather.getTemperature() > 75) return "yes";
        if ("overcast".equals(weather.getOutlook())) return "yes";
        if (weather.getHumidity() > 80) return "no";
        return "yes";
    }
}
