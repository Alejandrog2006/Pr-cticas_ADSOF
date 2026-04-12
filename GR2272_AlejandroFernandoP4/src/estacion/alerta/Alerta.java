/*
 * Registro persistente de una alerta generada por un sensor.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.alerta;

import java.time.LocalDateTime;

/**
 * Representa una alerta registrada por la estación meteorológica.
 */
public class Alerta {
    private final String sensorId;
    private final TipoAlerta tipo;
    private final String mensaje;
    private final LocalDateTime fecha;

    /**
     * Crea una alerta con el sensor, el tipo, el mensaje y la fecha.
     *
     * @param sensorId identificador del sensor.
     * @param tipo tipo de alerta.
     * @param mensaje mensaje descriptivo.
     * @param fecha fecha de generación.
     */
    public Alerta(String sensorId, TipoAlerta tipo, String mensaje, LocalDateTime fecha) {
        this.sensorId = sensorId;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    /**
     * Obtiene el identificador del sensor asociado.
     *
     * @return identificador del sensor.
     */
    public String getSensorId() {
        return sensorId;
    }

    /**
     * Obtiene el tipo de alerta.
     *
     * @return tipo de alerta.
     */
    public TipoAlerta getTipo() {
        return tipo;
    }

    /**
     * Obtiene el mensaje descriptivo.
     *
     * @return mensaje de la alerta.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Obtiene la fecha de creación.
     *
     * @return fecha de la alerta.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }
}
