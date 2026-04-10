package estacion.procesador;

import java.util.*;

import estacion.sensor.Sensor;
import estacion.conversor.Conversor;

import java.time.LocalDateTime;

public class ProcesadorDatos {
    private Map<LocalDateTime, Double> historico;
    private Conversor conversor;
    private Sensor sensor;

    public ProcesadorDatos(Sensor sensor) {
        this.conversor = sensor.getConversor();
        //He creado un método getUnidad() en Sensor
        if (this.conversor.unidadInicial() != sensor.getUnidad()) {
            throw new IllegalArgumentException("El conversor del sensor no es compatible con la unidad del sensor");
            //Tenemos que hacer catch de esto en algún lado
        }
        this.sensor = sensor;
        this.historico = new LinkedHashMap<>();
    }

    public void almacenarLectura(LocalDateTime fecha, double valor) {
        this.historico.put(fecha, this.conversor.convertir(valor));
    }

    //Igual falta configurar conversores
}