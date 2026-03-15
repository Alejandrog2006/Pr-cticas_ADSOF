import java.util.*;

/**
 * Representa un mensaje en la red social.
 * Un mensaje tiene un autor, texto, capacidad y usuario actual.
 */
public class Mensaje {
    private final Usuario autor;
    private String mensaje;
    protected int capac;
    private Usuario usuarioActual;

   /*public Mensaje(Usuario autor, int capac, Usuario usuarioActual) {
        this.autor = autor;
        this.capac = capac;
        this.usuarioActual = usuarioActual;
    } lo comento porque en el script que nos pasan reciben un string en vez de un autor*/

    /**
     * Construye un mensaje con el texto dado, capacidad y usuario inicial.
     * El autor se establece como el usuario inicial.
     * Agrega el mensaje al historial del usuario inicial.
     * @param mensaje el texto del mensaje
     * @param capac la capacidad del mensaje
     * @param usuarioActual el usuario inicial
     */
    public Mensaje(String mensaje, int capac, Usuario usuarioActual) {
        this.autor = usuarioActual;
        this.mensaje = mensaje;
        this.capac = capac;
        this.usuarioActual = usuarioActual;

        usuarioActual.addMensaje(this);
    }

    /**
     * Obtiene el autor del mensaje.
     * @return el autor
     */
    public Usuario getAutor() {
        return autor;
    }  

    /**
     * Obtiene la capacidad del mensaje.
     * @return la capacidad
     */
    public int getCapac() {
        return capac;
    }       

    /**
     * Obtiene el texto del mensaje.
     * @return el texto
     */
    public String getTexto() {
        return mensaje;
    }

    /**
     * Obtiene el usuario actual del mensaje.
     * @return el usuario actual
     */
    public Usuario getUsarioActual() {
        return usuarioActual;
    }

    /**
     * Intenta difundir el mensaje a través del enlace dado.
     * Verifica que el usuario actual coincida con el origen del enlace, si la difusión es posible y si es aceptado por el destino.
     * Actualiza el usuario actual y la capacidad si tiene éxito.
     * @param e el enlace a través del cual difundir
     * @return true si la difusión fue exitosa, false en caso contrario
     */
    public boolean difunde(Enlace e) {
        if(this.usuarioActual != e.getUsuarioO() ||
         this.puedeDifundirPor(e) == false ||
         this.aceptadoPor(e.getUsuarioD()) == false) {
            return false;
        }

        this.usuarioActual = e.getUsuarioD();
        this.capac -= e.costeReal();
        this.capac += e.getUsuarioD().getCapacAmpl();

        if (this.usuarioActual.hasMensaje(this)==false) {
            this.usuarioActual.addMensaje(this);
        }

        return true;
    }   

    /**
     * Verifica si el mensaje puede difundirse a través del enlace dado basado en la capacidad.
     * @param e el enlace a verificar
     * @return true si la capacidad es suficiente, false en caso contrario
     */
    public boolean puedeDifundirPor(Enlace e) {
        if (this.capac >= e.costeReal()) {
            return true;
        }
        return false;
    }

    /**
     * Verifica si el mensaje es aceptado por el usuario dado.
     * Implementación por defecto siempre devuelve true.
     * @param u el usuario a verificar
     * @return true si es aceptado, false en caso contrario
     */
    public boolean aceptadoPor(Usuario u) {
        return true;
    }

    /**
     * Intenta difundir el mensaje a una lista de usuarios.
     * Difunde a cada usuario en secuencia si es posible.
     * @param usuarios la lista de usuarios a los que difundir
     * @return true si todas las difusiones fueron exitosas, false si alguna falló
     */
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

    /**
     * Devuelve una representación en cadena del mensaje.
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return "Mensaje(" + this.mensaje + ":" + this.capac + ") en " + this.usuarioActual.getNombre();
    }
}
