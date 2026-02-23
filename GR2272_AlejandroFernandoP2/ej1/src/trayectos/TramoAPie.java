package trayectos;

/**
 * Representa un tramo de trayecto que se realiza a pie.
 * Define la distancia en kilómetros y el ritmo de marcha.
 * El tiempo total del tramo se calcula multiplicando la distancia por la velocidad del ritmo.
 * Si no se especifica ritmo, se usa MODERADO por defecto.
 */
public class TramoAPie extends TramoTrayecto {
    private double numKm;
    private Ritmo ritmo;

    /**
     * Constructor que crea un tramo a pie con ritmo moderado por defecto.
     *
     * @param origen el punto de partida del tramo
     * @param destino el punto de llegada del tramo
     * @param numKm la distancia en kilómetros
     */
    public TramoAPie(String origen, String destino, double numKm) {
        this(origen, destino, numKm, Ritmo.MODERADO);
    }

    /**
     * Constructor que crea un tramo a pie con ritmo especificado.
     *
     * @param origen el punto de partida del tramo
     * @param destino el punto de llegada del tramo
     * @param numKm la distancia en kilómetros
     * @param ritmo el ritmo de marcha (si es null, se usa MODERADO)
     */
    public TramoAPie(String origen, String destino, double numKm, Ritmo ritmo) {
        super(origen, destino);
        this.numKm = numKm;
        this.ritmo = ritmo == null ? Ritmo.MODERADO : ritmo;
    }

    /**
     * Obtiene la distancia del tramo en kilómetros.
     *
     * @return la distancia en kilómetros
     */
    public double getNumKm() {
        return this.numKm;
    }

    /**
     * Obtiene el ritmo de marcha del tramo.
     *
     * @return el ritmo de marcha
     */
    public Ritmo getRitmo() {
        return this.ritmo;
    }

    /**
     * Calcula el tiempo necesario para recorrer el tramo a pie.
     * El cálculo es: distancia × velocidad del ritmo.
     *
     * @return el tiempo en minutos
     */
    @Override
    public double tiempo() {
        return this.numKm * this.ritmo.getVelocidad();
    }

    /**
     * Devuelve una representación en texto del tramo a pie.
     *
     * @return una cadena con información del tramo y el ritmo utilizado
     */
    @Override
    public String toString() {
        return "A pie " + super.toString() + " (ritmo " + this.ritmo + ") ";
    }
}