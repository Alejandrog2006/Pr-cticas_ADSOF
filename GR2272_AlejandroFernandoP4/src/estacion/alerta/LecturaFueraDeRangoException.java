/*
 * Excepción específica para lecturas fuera del rango válido.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.alerta;

/**
 * Indica que la lectura obtenida está fuera del rango permitido.
 */
public class LecturaFueraDeRangoException extends AlertaSensorException {
    /**
     * Crea la excepción para un sensor concreto.
     *
     * @param sensorId identificador del sensor.
     * @param message mensaje descriptivo.
     */
    public LecturaFueraDeRangoException(String sensorId, String message) {
        super(sensorId, TipoAlerta.LECTURA_FUERA_DE_RANGO, message);
    }
}
