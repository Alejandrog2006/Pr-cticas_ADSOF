import java.util.*;

public class Mensaje {
    private final Usuario autor;
    private String mensaje;
    private int capac;
    private Usuario usuarioActual;

   /*public Mensaje(Usuario autor, int capac, Usuario usuarioActual) {
        this.autor = autor;
        this.capac = capac;
        this.usuarioActual = usuarioActual;
    } lo comento porque en el script que nos pasan reciben un string en vez de un autor*/

    public Mensaje(String mensaje, int capac, Usuario usuarioActual) {
        this.autor = usuarioActual;
        this.mensaje = mensaje;
        this.capac = capac;
        this.usuarioActual = usuarioActual;
    }

    public Usuario getAutor() {
        return autor;
    }  

    public int getCapac() {
        return capac;
    }       

    public String getTexto() {
        return mensaje;
    }

    public Usuario getUsarioActual() {
        return usuarioActual;
    }

    public boolean difunde(Enlace e) {
        if(this.usuarioActual != e.getUsuarioO() ||
         this.puedeDifundirPor(e) == false ||
         this.aceptadoPor(e.getUsuarioD()) == false) {
            return false;
        }

        this.usuarioActual = e.getUsuarioD();
        this.capac -= e.costeReal();
        this.capac += e.getUsuarioD().getCapacAmpl();
        return true;
    }   

    public boolean puedeDifundirPor(Enlace e) {
        if (this.capac >= e.costeReal()) {
            return true;
        }
        return false;
    }

    public boolean aceptadoPor(Usuario u) {
        return true;
    }

    public boolean difunde(List<Usuario> usuarios) { 
        boolean hayFallo = false;
        for (Usuario u : usuarios) {
            Enlace e = this.usuarioActual.getEnlace(u);
            if (e != null) {
                if(!this.difunde(e)) {
                    hayFallo = true;
                } else {
                    System.out.println(this.toString());
                }
            } else {
                hayFallo = true;
            }
        }

        return !hayFallo;
    }   

    @Override
    public String toString() {
        return "Mensaje(" + this.mensaje + ":" + this.capac + ") en " + this.usuarioActual.getNombre();
    }
}
