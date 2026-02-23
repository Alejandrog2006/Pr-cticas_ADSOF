package trayectos;

/**
 * Clase abstracta que representa un tramo de un trayecto de transporte.
 * Define la estructura base para cualquier tipo de tramo con origen y destino,
 * e implementa el cálculo del tiempo necesario para recorrerlo.
 * Subclases concretas como TramoAPie y TramoTren especializan esta funcionalidad.
 */
public abstract class TramoTrayecto {
	private String origen;
	private String destino;

	/**
	 * Constructor que inicializa un tramo con origen y destino.
	 *
	 * @param origen el punto de partida del tramo
	 * @param destino el punto de llegada del tramo
	 */
	public TramoTrayecto(String origen, String destino) {
		this.origen = origen;
		this.destino = destino;
	}

	/**
	 * Calcula el tiempo necesario para recorrer el tramo.
	 * Este método debe ser sobrescrito por las subclases concretas.
	 *
	 * @return el tiempo en minutos (0.0 por defecto)
	 */
	public double tiempo() {
		return 0.0;
	}

	/**
	 * Devuelve una representación en texto del tramo.
	 *
	 * @return una cadena con origen, destino y tiempo en minutos
	 */
	@Override
	public String toString() {
		return "desde " + this.origen + " a " + this.destino + ": " + this.tiempo() + " minutos";
	}
}
