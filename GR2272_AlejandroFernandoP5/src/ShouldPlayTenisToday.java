package GR2272_AlejandroFernandoP5.src;

public class ShouldPlayTenisToday implements Labeller<Weather> {
    @Override
    public String label(Weather weather) {
        // Aplicar una estrategia simple para determinar si jugar tenis
        // Reglas: No si está lluvioso y ventoso, sí si es soleado con temperatura moderada, etc.
        
        if ("rainy".equals(weather.getOutlook()) && weather.isWindy()) {
            return "no";
        }
        
        if ("sunny".equals(weather.getOutlook()) && weather.getTemperature() > 75) {
            return "yes";
        }
        
        if ("overcast".equals(weather.getOutlook())) {
            return "yes";
        }
        
        if (weather.getHumidity() > 80) {
            return "no";
        }
        
        return "yes";
    }
}
