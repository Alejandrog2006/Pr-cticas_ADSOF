import java.util.*;

public class Biblioteca {
    private String nombre;
    private Map<String, List<Libro>> librosPorGenero;

    public Biblioteca(String nombre) {
        this.nombre = nombre;
        this.librosPorGenero = new LinkedHashMap<>();
    }

    // Método para agregar un libro a la biblioteca
    public void agregarLibro(Libro libro) {
        String genero = normalizarGenero(libro.getGenero());
        this.librosPorGenero
            .computeIfAbsent(genero, k -> new ArrayList<>())
            .add(libro);
    }

    // Recibe un género como parámetro y devuelve una lista con todos los libros que pertenecen a ese género
    public List<Libro> librosPorGenero(String genero) {
        String clave = normalizarGenero(genero);
        List<Libro> resultado = this.librosPorGenero.get(clave);
        return resultado == null ? new ArrayList<>() : new ArrayList<>(resultado);
    }

    // Recibe un año como parámetro y devuelve una lista con todos los libros publicados después de ese año
    public List<Libro> librosPosterioresA(int añoPublicacion) {
        List<Libro> resultado = new ArrayList<>();
        for (List<Libro> lista : this.librosPorGenero.values()) {
            for (Libro libro : lista) {
                if (libro.getAño() > añoPublicacion) {
                    resultado.add(libro);
                }
            }
        }
        return resultado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Biblioteca: ").append(this.nombre).append("\n");
        for (Map.Entry<String, List<Libro>> entry : this.librosPorGenero.entrySet()) {
            sb.append("Género: ").append(entry.getKey()).append("\n");
            for (Libro libro : entry.getValue()) {
                sb.append("  - ").append(libro).append("\n");
            }
        }
        return sb.toString().trim();
    }

    private String normalizarGenero(String genero) {
        if (genero == null || genero.isBlank()) {
            return "Sin género";
        }
        return genero.trim().toLowerCase();
    }
}
