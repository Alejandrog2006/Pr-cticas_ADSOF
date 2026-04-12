/*
 * Excepción específica para cambios bruscos entre lecturas consecutivas.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.alerta;

/**
 * Indica que se ha detectado un cambio brusco entre dos lecturas consecutivas.
 */
public class CambioBruscoException extends AlertaSensorException {
    private final double lecturaAnterior;
    private final double lecturaActual;
    private final double porcentajeCambio;

    /**
     * Crea la excepción con datos de la lectura anterior, la actual y el porcentaje.
     *
     * @param sensorId identificador del sensor.
     * @param lecturaAnterior lectura anterior registrada.
     * @param lecturaActual lectura actual registrada.
     * @param porcentajeCambio porcentaje de cambio calculado.
     */
    public CambioBruscoException(String sensorId, double lecturaAnterior, double lecturaActual, double porcentajeCambio) {
        super(sensorId, TipoAlerta.CAMBIO_BRUSCO,
            "Cambio brusco detectado: " + porcentajeCambio + "%");
        this.lecturaAnterior = lecturaAnterior;
        this.lecturaActual = lecturaActual;
        this.porcentajeCambio = porcentajeCambio;
    }

    /**
     * Obtiene la lectura anterior.
     *
     * @return lectura anterior.
     */
    public double getLecturaAnterior() {
        return lecturaAnterior;
    }

    /**
     * Obtiene la lectura actual.
     *
     * @return lectura actual.
     */
    public double getLecturaActual() {
        return lecturaActual;
    }

    /**
     * Obtiene el porcentaje de cambio calculado.
     *
     * @return porcentaje de cambio.
     */
    public double getPorcentajeCambio() {
        return porcentajeCambio;
    }
}
