package estacion.sensor.estrategia;

import java.util.concurrent.ThreadLocalRandom;

public class EstrategiaAleatoria implements Estrategia {

    private double probabilidad = 0.15; // probabilidad de generar un valor fuera de rango

    public EstrategiaAleatoria() {
        // constructor por defecto con probabilidad de 0.15
    }
    
    public EstrategiaAleatoria(double probabilidad) {
        if (probabilidad < 0 || probabilidad > 1) {
            throw new IllegalArgumentException("La probabilidad de la estrategia aleatoria debe estar entre 0 y 1");
        }
        this.probabilidad = probabilidad;
    }

    @Override
    public double generarValor(double min, double max) {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double amplitud = max - min;

        // un único sorteo para decidir si sale fuera de rango
        if (random.nextDouble() < this.probabilidad) {
            // reparte fuera de rango 50% por arriba y por debajo
            if (random.nextBoolean()) {
                return min - random.nextDouble(0.0, amplitud); // por debajo de min
            } else {
                return max + random.nextDouble(0.0, amplitud); // por encima de max
            }
        }

        // dentro de rango
        return random.nextDouble(min, max);
    }
}
