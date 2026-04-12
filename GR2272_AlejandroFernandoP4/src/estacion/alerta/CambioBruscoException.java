package estacion.alerta;

public class CambioBruscoException extends AlertaSensorException {
    private final double lecturaAnterior;
    private final double lecturaActual;
    private final double porcentajeCambio;

    public CambioBruscoException(String sensorId, double lecturaAnterior, double lecturaActual, double porcentajeCambio) {
        super(sensorId, TipoAlerta.CAMBIO_BRUSCO,
            "Cambio brusco detectado: " + porcentajeCambio + "%");
        this.lecturaAnterior = lecturaAnterior;
        this.lecturaActual = lecturaActual;
        this.porcentajeCambio = porcentajeCambio;
    }

    public double getLecturaAnterior() {
        return lecturaAnterior;
    }

    public double getLecturaActual() {
        return lecturaActual;
    }

    public double getPorcentajeCambio() {
        return porcentajeCambio;
    }
}
