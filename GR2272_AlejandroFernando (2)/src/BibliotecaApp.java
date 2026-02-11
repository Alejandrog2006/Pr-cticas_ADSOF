/**
 * Esta clase ejecuta un ejemplo de uso de Libro
 * Autor: Alejandro González y Fernando Blanco
 * Version: 1.0
 * Nombre del fichero: BibliotecaApp.java
 */
import java.util.*;

public class BibliotecaApp {
	public static void main(String[] args) {
		List<Libro> libros = new ArrayList<>(List.of(
				new Libro("1", "El Quijote", "Miguel de Cervantes", 1605, "Novela", 5), // ISBN, titulo, autor, ano, genero, #ejemplares
				new Libro("2", "El murcielago", "Jo Nesbo", 1990, "Novela", 1),
				new Libro("3", "Learn Java", "David Hofman", 2030, "Ciencia", 0)));

		libros.get(1).prestar();
		for (Libro l : libros)
			System.out.println(l);

		libros.get(1).devolver();
		System.out.println(libros);

		libros.add(new Libro("4", "Con viento solano", "Ignacio Aldecoa", 1956, "Novela", 1));
		System.out.println(libros);
	}
}
