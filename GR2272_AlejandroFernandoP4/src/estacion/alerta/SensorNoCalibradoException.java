/*
 * Excepción específica para sensores sin calibrar o con calibración caducada.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.alerta;

/**
 * Indica que un sensor no está calibrado o que su calibración ha caducado.
 */
public class SensorNoCalibradoException extends AlertaSensorException {
    /**
     * Crea la excepción para un sensor concreto.
     *
     * @param sensorId identificador del sensor.
     * @param message mensaje descriptivo.
     */
    public SensorNoCalibradoException(String sensorId, String message) {
        super(sensorId, TipoAlerta.SENSOR_NO_CALIBRADO, message);
    }
}
