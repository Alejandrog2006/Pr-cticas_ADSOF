package trayectos;

/**
 * Representa un tramo de trayecto que se realiza en tren.
 * Define la línea de tren utilizada y el número de paradas.
 * El tiempo total del tramo se calcula según la línea:
 *   Línea C1: 5 minutos por parada
 *   Línea C4: 10 minutos por parada
 *   Línea C5: 30 minutos por parada
 */
public class TramoTren extends TramoTrayecto {
	private Linea linea;
	private int numParadas;

	/**
	 * Constructor que crea un tramo en tren.
	 *
	 * @param origen el punto de partida del tramo
	 * @param destino el punto de llegada del tramo
	 * @param linea la línea de tren utilizada (C1, C4 o C5)
	 * @param numParadas el número de paradas en este tramo
	 */
	public TramoTren(String origen, String destino, Linea linea, int numParadas) {
		super(origen, destino);
		this.linea = linea;
		this.numParadas = numParadas;
	}

    /**
     * Obtiene la línea de tren utilizada en este tramo.
     *
     * @return la línea de tren
     */
    public Linea getLinea() {
        return this.linea;
    } 

    /**
     * Obtiene el número de paradas en este tramo.
     *
     * @return el número de paradas
     */
    public int getNumParadas() {
        return this.numParadas;
    }

	/**
	 * Calcula el tiempo necesario para recorrer el tramo en tren.
	 * El cálculo depende de la línea:
	 * 
	 * C1: 5 minutos por parada
	 * C4: 10 minutos por parada
	 * C5: 30 minutos por parada
	 *
	 * @return el tiempo en minutos
	 */
	@Override
	public double tiempo() {

		double t = 0;

		switch (linea) {
			case C1:

				t = this.numParadas * 5;

				break;
		
			case C4:

				t = this.numParadas * 10;

				break;

			case C5:

				t = this.numParadas * 30;
				
				break;

			default:

				break;
		}

		return t;
	}
    
	/**
	 * Devuelve una representación en texto del tramo en tren.
	 *
	 * @return una cadena con información del tramo y la línea de tren utilizada
	 */
	@Override
	public String toString() {
		return "En tren de la linea " + this.linea + " " + super.toString();
	}
}
