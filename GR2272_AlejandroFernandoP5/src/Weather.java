package GR2272_AlejandroFernandoP5.src;

import java.util.Objects;

public class Weather {
    private final String outlook;      // sunny, overcast, rainy
    private final int temperature;     // 0-100
    private final int humidity;        // 0-100
    private final boolean windy;       // true/false

    public Weather(String outlook, int temperature, int humidity, boolean windy) {
        this.outlook = Objects.requireNonNull(outlook);
        this.temperature = temperature;
        this.humidity = humidity;
        this.windy = windy;
    }

    public String getOutlook() {
        return outlook;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public boolean isWindy() {
        return windy;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "outlook='" + outlook + '\'' +
                ", temp=" + temperature +
                ", humidity=" + humidity +
                ", windy=" + windy +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Weather other)) return false;
        return temperature == other.temperature &&
                humidity == other.humidity &&
                windy == other.windy &&
                Objects.equals(outlook, other.outlook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outlook, temperature, humidity, windy);
    }
}
