package estacion.aux;

import java.util.List;

public class StringLista {
    private String titulo;
    private List<String> elementos;

    public StringLista(String titulo, List<String> elementos) {
        this.titulo = titulo;
        this.elementos = elementos;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public List<String> getElementos() {
        return this.elementos;
    }
}
