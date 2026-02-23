/**
 * Esta clase gestiona una biblioteca y sus libros
 * Autor: Alejandro González y Fernando Blanco
 * Version: 1.0
 * Nombre del fichero: Biblioteca.java
 */
import java.util.*;

public class Biblioteca {
	private String nombre;
	private Map<String, List<Libro>> librosPorGenero;

	// Constructor que inicializa la biblioteca y sus libros
	public Biblioteca(String nombre, List<Libro> libros) {
		this.nombre = nombre;
		this.librosPorGenero = new HashMap<>();
		for (Libro libro : libros) {
			agregarLibro(libro);
		}
	}

	// Metodo para agregar un libro a la biblioteca
	public void agregarLibro(Libro libro) {
		this.librosPorGenero
				.computeIfAbsent(libro.getGenero(), genero -> new ArrayList<>())
				.add(libro);
	}

	// Metodo que devuelve los libros de un genero concreto
	public List<Libro> librosPorGenero(String genero) {
		List<Libro> libros = this.librosPorGenero.get(genero);
		return libros == null ? new ArrayList<>() : new ArrayList<>(libros);
	}

	// Metodo que devuelve los libros publicados despues de un ano
	public List<Libro> librosPosterioresA(int anoPublicacion) {
		List<Libro> resultado = new ArrayList<>();
		for (List<Libro> libros : this.librosPorGenero.values()) {
			for (Libro libro : libros) {
				if (libro.getAno() > anoPublicacion) {
					resultado.add(libro);
				}
			}
		}
		return resultado;
	}

	@Override
	// Metodo para imprimir la biblioteca agrupando por genero
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Biblioteca: ").append(this.nombre);

		if (this.librosPorGenero.isEmpty()) {
			sb.append(" (sin libros)");
			return sb.toString();
		}

		for (Map.Entry<String, List<Libro>> entry : this.librosPorGenero.entrySet()) {
			sb.append("\nGenero: ").append(entry.getKey());
			for (Libro libro : entry.getValue()) {
				sb.append("\n  ").append(libro);
			}
		}
		return sb.toString();
	}
}
