package estacion;

import java.util.*;

import estacion.sensor.Sensor;

import java.time.LocalDateTime;

public class ProcesadorDatos {
    Map<LocalDateTime, Double> historico;
    Sensor sensor;
}