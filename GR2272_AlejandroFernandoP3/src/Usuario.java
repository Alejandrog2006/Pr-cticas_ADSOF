import java.util.*;

/**
 * Representa a un usuario en la red social.
 * Un usuario tiene un nombre, capacidad de amplificación, lista de enlaces, nivel de exposición, historial de mensajes y alcance promedio.
 */
public class Usuario {
    protected String nombre;
    protected int capacAmpl;
    protected List<Enlace> enlaces;
    protected Exposicion exposicion;
    private List<Mensaje> historial;
    private double alcancePromedio;


    public enum Exposicion {
        OCULTA(0), 
        BAJA(1), 
        MEDIA(2), 
        ALTA(3), 
        VIRAL(4);

        private int nivel;

        /**
         * Construye una Exposicion con el nivel dado.
         * @param nivel el valor del nivel
         */
        private Exposicion(int nivel) {
            this.nivel = nivel;
        }

        /**
         * Obtiene el valor del nivel de la exposición.
         * @return el nivel
         */
        public int nivel() {
            return this.nivel;
        }   
    }

    /*Constructores*/
 
    /**
     * Construye un usuario con el nombre dado, capacidad de amplificación por defecto de 2, 
     * lista vacía de enlaces, exposición alta, historial vacío de mensajes y alcance promedio de 0.0.
     * @param nombre el nombre del usuario
     */
    public Usuario(String nombre) {
        this.nombre = nombre;
        this.capacAmpl = 2;
        this.enlaces = new LinkedList<>();
        this.exposicion = Exposicion.ALTA;
        this.historial = new LinkedList<>();
        this.alcancePromedio = 0.0;
    }
    
    /**
     * Construye un usuario con el nombre dado y capacidad de amplificación, 
     * lista vacía de enlaces, exposición alta, historial vacío de mensajes y alcance promedio de 0.0.
     * @param nombre el nombre del usuario
     * @param capacAmpl la capacidad de amplificación del usuario
     */
    public Usuario(String nombre, int capacAmpl) {
        this(nombre);
        this.capacAmpl = capacAmpl;
    }

    /**
     * Construye un usuario con el nombre dado, capacidad de amplificación por defecto de 2, 
     * lista vacía de enlaces, exposición dada, historial vacío de mensajes y alcance promedio de 0.0.
     * @param nombre el nombre del usuario
     * @param exposicion el nivel de exposición del usuario
     */
    public Usuario(String nombre, Exposicion exposicion) {
        this(nombre);
        this.exposicion = exposicion;
    }

    /**
     * Construye un usuario con el nombre dado, capacidad de amplificación, exposición, 
     * lista vacía de enlaces, historial vacío de mensajes y alcance promedio de 0.0.
     * @param nombre el nombre del usuario
     * @param capacAmpl la capacidad de amplificación del usuario
     * @param exposicion el nivel de exposición del usuario
     */
    public Usuario(String nombre, int capacAmpl, Exposicion exposicion) {
        this(nombre);
        this.capacAmpl = capacAmpl;
        this.exposicion = exposicion;
    }

    /*Métodos para gestionar enlaces*/

    /**
     * Agrega un enlace a la lista de enlaces del usuario.
     * El enlace debe tener a este usuario como origen y a un usuario diferente como destino.
     * No se permiten enlaces duplicados al mismo destino.
     * @param e el enlace a agregar
     * @return true si el enlace se agregó exitosamente, false en caso contrario
     */
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

    /**
     * Crea y agrega un enlace al destino dado con el costo especificado.
     * El enlace debe tener a este usuario como origen y a un usuario diferente como destino.
     * No se permiten enlaces duplicados al mismo destino.
     * @param destino el usuario destino
     * @param coste el costo del enlace
     * @return true si el enlace se agregó exitosamente, false en caso contrario
     */
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

    /**
     * Obtiene el enlace en el índice especificado.
     * @param i el índice del enlace
     * @return el enlace en el índice, o lanza IndexOutOfBoundsException si es inválido
     */
    public Enlace getEnlace(int i) {
        return this.enlaces.get(i);
    }

    /**
     * Obtiene el número de enlaces que tiene el usuario.
     * @return el número de enlaces
     */
    public int getNumEnlaces() {
        return this.enlaces.size();
    }

    /**
     * Obtiene el enlace al usuario destino especificado.
     * @param destino el usuario destino
     * @return el enlace al destino, o null si no existe tal enlace
     */
    public Enlace getEnlace(Usuario destino) {
        for(Enlace e : this.enlaces) {
            if(e.getUsuarioD() == destino) {
                return e;
            }
        }
        return null;
    }

    /*Métodos para gestionar historial de mensajes*/

    /**
     * Agrega un mensaje al historial del usuario si no está presente ya.
     * Actualiza el alcance promedio basado en la capacidad del mensaje.
     * Ajusta el nivel de exposición del usuario basado en la capacidad del mensaje relativa al promedio actual.
     * @param m el mensaje a agregar
     * @return true si el mensaje se agregó exitosamente, false si ya está en el historial o es inválido
     */
    public boolean addMensaje(Mensaje m) {
        if (this.historial.contains(m)) {
            return false;
        }

        this.historial.add(m);

        double promedio = this.alcancePromedio;
        int alcance = m.getCapac();
        int nivel = this.exposicion.nivel();

        if (alcance > promedio) {
            nivel = Math.min(nivel + 1, Exposicion.VIRAL.nivel());
            for (Exposicion exp: Exposicion.values()) {
                if (nivel == exp.nivel()) {
                    this.exposicion = exp;
                    break;
                }
            }
        } else if (alcance < promedio) {
            nivel = Math.max(nivel - 1, Exposicion.OCULTA.nivel());
            for (Exposicion exp: Exposicion.values()) {
                if (nivel == exp.nivel()) {
                    this.exposicion = exp;
                    break;
                }
            }
        }

        int n = this.historial.size();
        this.alcancePromedio = (this.alcancePromedio * (n - 1) + m.getCapac()) / n;
        
        return true;
    }

    /**
     * Verifica si el mensaje está en el historial del usuario.
     * @param m el mensaje a verificar
     * @return true si el mensaje está en el historial, false en caso contrario
     */
    public boolean hasMensaje(Mensaje m) {
        return this.historial.contains(m);
    }

    /*Getters y setters*/
    
    /**
     * Obtiene el nombre del usuario con prefijo @.
     * @return el nombre formateado
     */
    public String getNombre() {
        return "@" + this.nombre;
    }

    /**
     * Obtiene el nombre plano del usuario.
     * @return el nombre
     */
    public String getNom() { // pal encontar usuario de la red social
        return this.nombre;
    }

    /**
     * Obtiene la capacidad de amplificación del usuario.
     * @return la capacidad de amplificación
     */
    public int getCapacAmpl() {
        return this.capacAmpl;
    }

    /**
     * Cambia el nivel de exposición del usuario.
     * @param e el nuevo nivel de exposición
     */
    public void cambiarExposicion(Exposicion e) {
        this.exposicion = e;
    }

    /**
     * Obtiene el nivel de exposición actual del usuario.
     * @return el nivel de exposición
     */
    public Exposicion getExposicion() {
        return this.exposicion;
    }

    /*Métods para imprimir*/

    /**
     * Devuelve una representación en cadena del usuario.
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return "@" + this.nombre + "(" + this.capacAmpl + ")" + "(" + this.exposicion + ")" + "[" + toString(enlaces) + "]";
    }

    /**
     * Devuelve una representación en cadena de la lista de enlaces.
     * @param enlaces la lista de enlaces
     * @return representación en cadena de los enlaces
     */
    protected String toString(List<Enlace> enlaces) {
        StringBuilder sb = new StringBuilder();
        for (Enlace e : enlaces) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.toString());
        }
        return sb.toString();
    }
}