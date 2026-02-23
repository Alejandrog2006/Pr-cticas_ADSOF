package trayectos;

/**
 * Enumeración que representa las líneas de tren disponibles en el sistema de transporte.
 * Cada línea tiene un nombre y un color asociado para su identificación visual.
 * Las líneas disponibles son:
 *   C1 - línea de color azul claro
 *   C4 - línea de color azul oscuro
 *   C5 - línea de color amarilla
 */
public enum Linea {
	C1("azul claro"),
	C4("azul oscuro"),
	C5("amarilla");

	private String color;

	/**
	 * Constructor privado de la enumeración Linea.
	 * Inicializa una línea de tren con su color asociado.
	 *
	 * @param color el color que identifica visualmente la línea de tren
	 */
	private Linea(String color) {
		this.color = color;
	}

	/**
	 * Devuelve una representación en texto de la línea con su color.
	 *
	 * @return una cadena con el nombre de la línea y su color entre paréntesis
	 */
	public String toString() {
		return this.name() + " (" + this.color + ")";
	}
}
