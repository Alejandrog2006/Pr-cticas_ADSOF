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
        if(e.getUsuarioO() != this || e.getUsuarioD() == this) {
            return false;
        }

        for(Enlace u : this.enlaces) {
            if(u.getUsuarioD() == e.getUsuarioD()) {
                return false;
            }
        }

        enlaces.add(e);

        return true;
    }

    public boolean addEnlace(Usuario destino, int coste) {

        Enlace e = new Enlace(this, destino, coste);
        
        if(e.getUsuarioO() != this || e.getUsuarioD() == this) {
            return false;
        }

        for(Enlace u : this.enlaces) {
            if(u.getUsuarioD() == e.getUsuarioD()) {
                return false;
            }
        }

        enlaces.add(e);

        return true;
    }

    public Enlace getEnlace(int i) {
        return this.enlaces.get(i);
    }

    public int getNumEnlaces() {
        return this.enlaces.size();
    }

    public Enlace getEnlace(Usuario destino) {
        for(Enlace e : this.enlaces) {
            if(e.getUsuarioD() == destino) {
                return e;
            }
        }
        return null;
    }
    
    public String getNombre() {
        return "@" + this.nombre;
    }

    public int getCapacAmpl() {
        return this.capacAmpl;
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