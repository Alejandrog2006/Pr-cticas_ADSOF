package estacion.alerta;

public class SensorNoCalibradoException extends AlertaSensorException {
    public SensorNoCalibradoException(String sensorId, String message) {
        super(sensorId, TipoAlerta.SENSOR_NO_CALIBRADO, message);
    }
}
