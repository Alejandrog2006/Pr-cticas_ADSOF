import java.util.*;

public class BibliotecaApp {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca("Central");

        biblioteca.agregarLibro(new Libro("1", "El Quijote", "Miguel de Cervantes", 5, 1605, "Novela"));
        biblioteca.agregarLibro(new Libro("2", "El murciélago", "Jo Nesbo", 1, 1997, "Novela"));
        biblioteca.agregarLibro(new Libro("3", "Breve historia del tiempo", "Stephen Hawking", 2, 1988, "Ciencia"));
        biblioteca.agregarLibro(new Libro("4", "Con viento solano", "Ignacio Aldecoa", 1, 1956, "Historia"));

        System.out.println(biblioteca);
        System.out.println("\nLibros de género novela:");
        System.out.println(biblioteca.librosPorGenero("Novela"));

        System.out.println("\nLibros posteriores a 1990:");
        System.out.println(biblioteca.librosPosterioresA(1990));
    }
}
