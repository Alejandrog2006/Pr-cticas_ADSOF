package estacion.alerta;

public class LecturaFueraDeRangoException extends AlertaSensorException {
    public LecturaFueraDeRangoException(String sensorId, String message) {
        super(sensorId, TipoAlerta.LECTURA_FUERA_DE_RANGO, message);
    }
}
