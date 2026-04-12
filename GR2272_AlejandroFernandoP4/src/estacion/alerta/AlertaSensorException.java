/*
 * Excepción base para alertas generadas por sensores.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.alerta;

/**
 * Excepción base para representar una alerta lanzada por un sensor.
 */
public abstract class AlertaSensorException extends RuntimeException {
    private final String sensorId;
    private final TipoAlerta tipo;

    /**
     * Crea una excepción de alerta con su tipo y mensaje.
     *
     * @param sensorId identificador del sensor.
     * @param tipo tipo de alerta.
     * @param message mensaje de error.
     */
    protected AlertaSensorException(String sensorId, TipoAlerta tipo, String message) {
        super(message);
        this.sensorId = sensorId;
        this.tipo = tipo;
    }

    /**
     * Obtiene el identificador del sensor.
     *
     * @return identificador del sensor.
     */
    public String getSensorId() {
        return sensorId;
    }

    /**
     * Obtiene el tipo de alerta asociado.
     *
     * @return tipo de alerta.
     */
    public TipoAlerta getTipo() {
        return tipo;
    }
}
