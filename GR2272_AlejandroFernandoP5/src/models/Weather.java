package models;

import java.util.Objects;

/**
 * Modelo inmutable de condiciones meteorologicas para la practica.
 */
public class Weather {
    private final String outlook;
    private final int temperature;
    private final int humidity;
    private final boolean windy;

    public Weather(String outlook, int temperature, int humidity, boolean windy) {
        this.outlook = Objects.requireNonNull(outlook);
        this.temperature = temperature;
        this.humidity = humidity;
        this.windy = windy;
    }

    /**
     * Devuelve el estado del cielo.
     *
     * @return outlook.
     */
    public String getOutlook() { return outlook; }

    /**
     * Devuelve la temperatura.
     *
     * @return temperatura.
     */
    public int getTemperature() { return temperature; }

    /**
     * Devuelve la humedad.
     *
     * @return humedad.
     */
    public int getHumidity() { return humidity; }

    /**
     * Indica si hay viento.
     *
     * @return true si hace viento, false en caso contrario.
     */
    public boolean isWindy() { return windy; }

    @Override
    public String toString() {
        return "Weather{outlook='" + outlook + "', temp=" + temperature +
               ", humidity=" + humidity + ", windy=" + windy + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Weather other)) return false;
        return temperature == other.temperature && humidity == other.humidity &&
               windy == other.windy && Objects.equals(outlook, other.outlook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outlook, temperature, humidity, windy);
    }
}
