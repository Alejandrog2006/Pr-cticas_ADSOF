package estacion.alerta;

import java.time.LocalDateTime;

public class Alerta {
    private final String sensorId;
    private final TipoAlerta tipo;
    private final String mensaje;
    private final LocalDateTime fecha;

    public Alerta(String sensorId, TipoAlerta tipo, String mensaje, LocalDateTime fecha) {
        this.sensorId = sensorId;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getSensorId() {
        return sensorId;
    }

    public TipoAlerta getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
