package trayectos;

/**
 * Clase principal que demuestra el funcionamiento del sistema de cálculo de trayectos.
 * Crea un trayecto de ejemplo compuesto por tres tramos:
 *   A pie desde Hotel Puerta del Sol hasta Sol Renfe (1 km, ritmo moderado)
 *   En tren por la línea C4 desde Sol Renfe hasta Cantoblanco Renfe (4 paradas)
 *   A pie desde Cantoblanco Renfe hasta EPS (2.6 km, ritmo rápido)
 * Imprime la información de cada tramo incluyendo el tiempo estimado para completarlo.
 */
public class Main {
	/**
	 * Método principal que ejecuta el programa de demostración.
	 *
	 * @param args argumentos de línea de comandos (no utilizados)
	 */
	public static void main(String[] args) {
		TramoTrayecto[] trayecto = {
			new TramoAPie("Hotel Puerta del Sol", "Sol Renfe", 1),
			new TramoTren("Sol Renfe", "Cantoblanco Renfe", Linea.C4, 4),
			new TramoAPie("Cantoblanco Renfe", "EPS", 2.6, Ritmo.RAPIDO),
		};

		for (TramoTrayecto tramo : trayecto) {
			System.out.println(tramo);
		}
	}
}
