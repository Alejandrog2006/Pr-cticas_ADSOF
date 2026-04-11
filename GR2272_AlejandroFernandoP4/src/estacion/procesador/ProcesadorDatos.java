package estacion.procesador;

import java.util.*;

import estacion.sensor.Sensor;
import estacion.conversor.Conversor;
import estacion.conversor.ConversorIdentidad;
import estacion.unidadLectura.UnidadLectura;

import java.time.LocalDateTime;

public class ProcesadorDatos {
    private Map<LocalDateTime, Double> historico;
    private Conversor conversor;
    private final Sensor sensor;

    public ProcesadorDatos(Sensor sensor) {
        this(sensor, new ConversorIdentidad(sensor.getUnidad()));
    }

    public ProcesadorDatos(Sensor sensor, Conversor conversor) {
        this.sensor = sensor;
        this.conversor = conversor;
        if (this.conversor.unidadInicial() != sensor.getUnidad()) {
            throw new IllegalArgumentException("El conversor del sensor no es compatible con la unidad del sensor");
            //Tenemos que hacer catch de esto en algún lado
        }
        this.historico = new LinkedHashMap<>();
    }

    public void almacenarLectura(LocalDateTime fecha, double valor) {
        this.historico.put(fecha, this.conversor.convertir(valor));
    }

    // ya se que es un setter pero no se me ocurre otra forma
    public void setConversor(Conversor conversor) {
        if (conversor.unidadInicial() != sensor.getUnidad()) {
            throw new IllegalArgumentException("El conversor del sensor no es compatible con la unidad del sensor");
        }
        this.conversor = conversor;
    }

    public Conversor getConversor() {
        return conversor;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public UnidadLectura getUnidadSalida() {
        return conversor.unidadFinal();
    }

    public Map<LocalDateTime, Double> getHistorico() {
        return new LinkedHashMap<>(historico);
    }

    public double minimo() {
        asegurarHistoricoNoVacio();
        return Collections.min(historico.values());
    }

    public double maximo() {
        asegurarHistoricoNoVacio();
        return Collections.max(historico.values());
    }

    public double media() {
        asegurarHistoricoNoVacio();
        double suma = 0.0;
        for (double valor : historico.values()) {
            suma += valor;
        }
        return suma / historico.size();
    }

    private void asegurarHistoricoNoVacio() {
        if (historico.isEmpty()) {
            throw new IllegalStateException("No hay lecturas procesadas para calcular estadisticas");
        }
    }
}