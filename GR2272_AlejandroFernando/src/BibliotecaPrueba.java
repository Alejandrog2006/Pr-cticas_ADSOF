/**
 * Esta clase prueba la funcionalidad de Biblioteca
 * Autor: Alejandro González y Fernando Blanco
 * Version: 1.0
 * Nombre del fichero: BibliotecaPrueba.java
 */
import java.util.*;

public class BibliotecaPrueba {
	public static void main(String[] args) {
		List<Libro> libros = new ArrayList<>(List.of(
				new Libro("1", "El Quijote", "Miguel de Cervantes", 1605, "Novela", 5),
				new Libro("2", "Fundacion", "Isaac Asimov", 1951, "Ciencia", 2),
				new Libro("3", "Historia de una escalera", "Antonio Buero Vallejo", 1949, "Historia", 1),
				new Libro("4", "El murcielago", "Jo Nesbo", 1990, "Novela", 1),
				new Libro("5", "Learn Java", "David Hofman", 2030, "Ciencia", 0)));

		Biblioteca biblioteca = new Biblioteca("Biblioteca Central", libros);
		System.out.println(biblioteca);

		System.out.println("\nLibros de genero Novela:");
		System.out.println(biblioteca.librosPorGenero("Novela"));

		System.out.println("\nLibros posteriores a 2000:");
		System.out.println(biblioteca.librosPosterioresA(2000));
	}
}
