import java.util.*;

public class Usuario {
    private String nombre;
    private int capacAmpl;
    private List<Enlace> enlaces;
 
    public Usuario(String nombre) {
        this.nombre = nombre;
        this.capacAmpl = 2;
        this.enlaces = new LinkedList<>();
    }
    
    public Usuario(String nombre, int capacAmpl) {
        this.nombre = nombre;
        this.capacAmpl = capacAmpl;
        this.enlaces = new LinkedList<>();
    }

    public boolean addEnlace(Enlace e) {
        
    }

    public String getNombre() {
        return "@" + this.nombre;
    }

    @Override
    public String toString() {
        return "@" + this.nombre + "(" + this.capacAmpl + ")" + "[" + toString(enlaces) + "]";
    }

    private String toString(List<Enlace> enlaces) {
        StringBuilder sb = new StringBuilder();
        for (Enlace e : enlaces) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.toString());
        }
        return sb.toString();
    }
}