package estacion.alerta;

public abstract class AlertaSensorException extends RuntimeException {
    private final String sensorId;
    private final TipoAlerta tipo;

    protected AlertaSensorException(String sensorId, TipoAlerta tipo, String message) {
        super(message);
        this.sensorId = sensorId;
        this.tipo = tipo;
    }

    public String getSensorId() {
        return sensorId;
    }

    public TipoAlerta getTipo() {
        return tipo;
    }
}
