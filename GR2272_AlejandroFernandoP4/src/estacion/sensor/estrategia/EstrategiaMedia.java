package estacion.sensor.estrategia;

import java.util.concurrent.ThreadLocalRandom;

public class EstrategiaMedia implements Estrategia {

    private int parametro;
    private double media;
    private int contador = 0;
    
    public EstrategiaMedia(int parametro) {
        if (parametro < 0 || parametro > 100) {
            throw new IllegalArgumentException("El parámetro de la estrategia media debe estar entre 0 y 100");
        }
        this.parametro = parametro;
    }
    public double generarValor(double min, double max) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (parametro == 0) {
            return random.nextDouble(min, max);
        }

        if (contador == 0) {
            media = random.nextDouble(min, max);
            contador++;
            return media;
        }

        double valor;
        if (media > 0) {
            valor = random.nextDouble(media * ((100.0 - this.parametro)/100.0), media * ((100.0 + this.parametro)/100.0));
        } else if (media < 0) {
            valor = random.nextDouble(media * ((100.0 + this.parametro)/100.0), media * ((100.0 - this.parametro)/100.0));
        } else {
            valor = random.nextDouble(min, max);
        }
        
        media = (media * contador + valor) / (contador + 1);
        contador++;

        return valor;
    }
}