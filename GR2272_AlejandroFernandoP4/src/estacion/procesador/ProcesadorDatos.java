/*
 * Procesador de datos asociado a un sensor; almacena lecturas procesadas y calcula estadísticas.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.procesador;

import java.util.*;

import estacion.sensor.Sensor;
import estacion.conversor.Conversor;
import estacion.conversor.ConversorIdentidad;
import estacion.unidadLectura.UnidadLectura;

import java.time.LocalDateTime;

/**
 * Gestiona el histórico de lecturas de un sensor y su conversión de unidades.
 */
public class ProcesadorDatos {
    private Map<LocalDateTime, Double> historico;
    private Conversor conversor;
    private final Sensor sensor;

    /**
     * Crea un procesador con conversor identidad basado en la unidad del sensor.
     *
     * @param sensor sensor asociado.
     */
    public ProcesadorDatos(Sensor sensor) {
        this(sensor, new ConversorIdentidad(sensor.getUnidad()));
    }

    /**
     * Crea un procesador para un sensor con un conversor concreto.
     *
     * @param sensor sensor asociado.
     * @param conversor conversor usado para almacenar las lecturas.
     */
    public ProcesadorDatos(Sensor sensor, Conversor conversor) {
        this.sensor = sensor;
        this.conversor = conversor;
        if (this.conversor.unidadInicial() != sensor.getUnidad()) {
            throw new IllegalArgumentException("El conversor del sensor no es compatible con la unidad del sensor");
            //Tenemos que hacer catch de esto en algún lado
        }
        this.historico = new LinkedHashMap<>();
    }

    /**
     * Almacena una lectura convirtiéndola previamente.
     *
     * @param fecha fecha de la lectura.
     * @param valor valor original leído.
     */
    public void almacenarLectura(LocalDateTime fecha, double valor) {
        this.historico.put(fecha, this.conversor.convertir(valor));
    }

    /**
     * Cambia el conversor asociado al procesador.
     *
     * @param conversor nuevo conversor.
     */
    public void setConversor(Conversor conversor) {
        if (conversor.unidadInicial() != sensor.getUnidad()) {
            throw new IllegalArgumentException("El conversor del sensor no es compatible con la unidad del sensor");
        }
        this.conversor = conversor;
    }

    /**
     * Obtiene el conversor actual.
     *
     * @return conversor asociado.
     */
    public Conversor getConversor() {
        return conversor;
    }

    /**
     * Obtiene el sensor asociado al procesador.
     *
     * @return sensor asociado.
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Obtiene la unidad de salida del procesador.
     *
     * @return unidad final del conversor.
     */
    public UnidadLectura getUnidadSalida() {
        return conversor.unidadFinal();
    }

    /**
     * Obtiene una copia del histórico de lecturas procesadas.
     *
     * @return mapa de fecha a valor procesado.
     */
    public Map<LocalDateTime, Double> getHistorico() {
        return new LinkedHashMap<>(historico);
    }

    /**
     * Obtiene el valor mínimo del histórico.
     *
     * @return mínimo de las lecturas procesadas.
     */
    public double minimo() {
        asegurarHistoricoNoVacio();
        return Collections.min(historico.values());
    }

    /**
     * Obtiene el valor máximo del histórico.
     *
     * @return máximo de las lecturas procesadas.
     */
    public double maximo() {
        asegurarHistoricoNoVacio();
        return Collections.max(historico.values());
    }

    /**
     * Calcula la media de las lecturas procesadas.
     *
     * @return media aritmética del histórico.
     */
    public double media() {
        asegurarHistoricoNoVacio();
        double suma = 0.0;
        for (double valor : historico.values()) {
            suma += valor;
        }
        return suma / historico.size();
    }

    /**
     * Verifica que el histórico contenga lecturas.
     */
    private void asegurarHistoricoNoVacio() {
        if (historico.isEmpty()) {
            throw new IllegalStateException("No hay lecturas procesadas para calcular estadisticas");
        }
    }
}