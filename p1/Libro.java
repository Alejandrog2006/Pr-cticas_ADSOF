public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int ejemplaresDisponibles;
    private int año;
    private String género;

    public Libro(String isbn, String titulo, String autor) {
        this(isbn, titulo, autor, 1, -1, null);
    }

    public Libro(String isbn, String titulo, String autor, int ejemplaresDisponibles) {
        this(isbn, titulo, autor, ejemplaresDisponibles, -1, null);
    }

    public Libro(String isbn, String titulo, String autor, int ejemplaresDisponibles, int año, String género) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
        this.año = año;
        this.género = género;
    }

    // Método para verificar si el libro está disponible
    public boolean estáDisponible() {
        return this.ejemplaresDisponibles > 0;
    }

    // Método para prestar el libro
    public boolean prestar() {
        if (estáDisponible()) {
            this.ejemplaresDisponibles--;
            return true;
        }
        return false;
    }

    // Método para devolver el libro
    public void devolver() {
        this.ejemplaresDisponibles++;
    }

    // Getters para género y año
    public String getGenero() {
        return this.género;
    }

    public int getAño() {
        return this.año;
    }

    // Método para obtener la descripción del libro
    private String description() {
        String estado = this.estáDisponible() ? "Disponible" : "No disponible";
        return "\"" + this.titulo + "\" de " + this.autor + " [" + estado + "]";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ISBN: ").append(this.isbn)
                .append(", ")
                .append(this.description())
                .append(" (")
                .append(this.ejemplaresDisponibles)
                .append(" ejemplares disponibles)");

        if (this.año > 0) {
            sb.append(", Año: ").append(this.año);
        }
        if (this.género != null && !this.género.isBlank()) {
            sb.append(", Género: ").append(this.género);
        }

        return sb.toString();
    }
}
