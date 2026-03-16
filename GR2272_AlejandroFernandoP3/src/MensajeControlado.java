/**
 * Clase que representa un mensaje controlado en la red social.
 * Autor: Fernando Blanco
 */
public class MensajeControlado extends Mensaje {
    private int rigidez;

    /**
    * Constructor que inicializa un mensaje controlado con texto, capacidad, usuario actual y rigidez.
    * @param mensaje el texto del mensaje
    * @param capac la capacidad del mensaje
    * @param usuarioActual el usuario actual del mensaje
    * @param rigidez la rigidez del mensaje
    */
    public MensajeControlado(String mensaje, int capac, Usuario usuarioActual, int rigidez) {
        super(mensaje, capac, usuarioActual);
        this.rigidez = rigidez;
    }

    /**
     * Verifica si el mensaje puede ser difundido por un enlace.
     * @param e el enlace
     * @return true si puede ser difundido, false en caso contrario
     */
    @Override
    public boolean puedeDifundirPor(Enlace e) {

        //Asume que el coste especial es 0 para enlaces normales, y mayor que 0 para enlaces señuelo (factorExtra > 0)
        if (e.costeEspecial() == 0) {
            if (this.capac >= e.costeReal()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Verifica si el mensaje es aceptado por un usuario.
     * @param u el usuario
     * @return true si es aceptado, false en caso contrario
     */
    @Override
    public boolean aceptadoPor(Usuario u) {

        switch (u.getExposicion().nivel()) {
            case 0: // OCULTA
                return true;
            case 1: // BAJA
                return rigidez >= 5;
            case 2: // MEDIA
                return rigidez >= 10;
            case 3: // ALTA
                return rigidez >= 20;
            case 4: // VIRAL
                return rigidez >= 50;
        }

        return true;
    }

    /**
     * Devuelve una representación en cadena del mensaje.
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return super.toString() + " con rigidez " + this.rigidez;
    }
    
}

