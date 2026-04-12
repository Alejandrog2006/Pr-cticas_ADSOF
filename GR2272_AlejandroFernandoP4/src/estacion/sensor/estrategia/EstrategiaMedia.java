/*
 * Estrategia que genera valores alrededor de la media de las lecturas previas.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor.estrategia;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Estrategia que mantiene los valores alrededor de su media acumulada.
 */
public class EstrategiaMedia implements Estrategia {

    private int parametro;
    private double media;
    private int contador = 0;
    
    /**
     * Crea la estrategia de media.
     *
     * @param parametro porcentaje máximo de variación.
     */
    public EstrategiaMedia(int parametro) {
        if (parametro < 0 || parametro > 100) {
            throw new IllegalArgumentException("El parámetro de la estrategia media debe estar entre 0 y 100");
        }
        this.parametro = parametro;
    }
    /**
     * Genera el siguiente valor con base en la media acumulada.
     *
     * @param min límite inferior.
     * @param max límite superior.
     * @return valor generado.
     */
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