package estacion.sensor.estrategia;

import java.util.concurrent.ThreadLocalRandom;

public class EstrategiaIncremental implements Estrategia {

    private double valorAnterior = 0.0;
    private int parametro;

    public EstrategiaIncremental(int parametro) {

        if (parametro < 0 || parametro > 100) {
            throw new IllegalArgumentException("El parámetro de la estrategia incremental debe estar entre 0 y 100");
        }
        this.parametro = parametro;
    }

    @Override
    public double generarValor(double min, double max) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (parametro == 0) {
            return valorAnterior;
        }

        // Se da por hecho que puede sacar valores fuera de rango
        if (valorAnterior > 0) {
            valorAnterior = random.nextDouble(valorAnterior * ((100.0 - this.parametro)/100.0), valorAnterior * ((100.0 + this.parametro)/100.0));
        } else if (valorAnterior < 0) {
            valorAnterior = random.nextDouble(valorAnterior * ((100.0 + this.parametro)/100.0), valorAnterior * ((100.0 - this.parametro)/100.0));
        } else {
            valorAnterior = random.nextDouble(min, max);
        }

        return valorAnterior;
    }
}
