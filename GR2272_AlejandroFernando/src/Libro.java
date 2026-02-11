/**
 * Esta clase representa un libro en una biblioteca
 * Autor: Alejandro González y Fernando Blanco
 * Version: 1.0
 * Nombre del fichero: Libro.java
 */
public class Libro {
	private String isbn;
	private String titulo;
	private String autor;
	private int ano;
	private String genero;
	private int ejemplaresDisponibles;

	public Libro(String isbn, String titulo, String autor, int ano, String genero, int ejemplaresDisponibles) {
		this.isbn = isbn;
		this.titulo = titulo;
		this.autor = autor;
		this.ano = ano;
		this.genero = genero;
		this.ejemplaresDisponibles = ejemplaresDisponibles;
	}

	// Metodo para verificar si el libro esta disponible
	public boolean estaDisponible() {
		return this.ejemplaresDisponibles > 0;
	}

	// Metodo para prestar el libro
	public boolean prestar() {
		if (estaDisponible()) {
			this.ejemplaresDisponibles--;
			return true;
		}
		return false;
	}

	// Metodo para devolver el libro
	public void devolver() {
		this.ejemplaresDisponibles++;
	}

    // Metodo para obtener el año (para la clase Biblioteca)
	public int getAno() {
		return this.ano;
	}

    // Metodo para obtener el genero (para la clase Biblioteca)
	public String getGenero() {
		return this.genero;
	}

	// Metodo para obtener la descripcion del libro
	private String descripcion() {
		String estado = this.estaDisponible() ? "Disponible" : "No disponible";
		return "\"" + this.titulo + "\" de " + this.autor + " (" + this.ano + ", " + this.genero + ") [" + estado + "]";
	}

	@Override
	public String toString() {
		return "ISBN: " + this.isbn + ". " + this.descripcion() + " (" + this.ejemplaresDisponibles +
				" ejemplares disponibles)";
	}
}
