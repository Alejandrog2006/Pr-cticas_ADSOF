/**
 * Esta clase representa un enlace entre dos usuarios.
 * Un enlace tiene un usuario origen, un usuario destino y un coste.
 * Permite gestionar los datos del enlace y calcular el coste real.
 * Autor: Alejandro González y Fernando Blanco
 * Version: 1.0
 * Nombre del fichero: Enlace.java
 */
public class Enlace {
    private Usuario usuarioOrigen;
    private Usuario usuarioDestino;
    private int coste;

    /**
     * Constructor que inicializa un enlace con usuario origen, destino y coste.
     * @param usuarioOrigen Usuario de origen del enlace
     * @param usuarioDestino Usuario de destino del enlace
     * @param coste Coste del enlace
     */
    public Enlace(Usuario usuarioOrigen, Usuario usuarioDestino, int coste) {
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        this.coste = coste;
    }

    /**
     * Cambia el usuario destino y el coste del enlace.
     * Valida que el usuario no sea nulo y el coste sea positivo.
     * @param u Nuevo usuario destino
     * @param n Nuevo coste
     * @return true si se cambió correctamente, false si los parámetros son inválidos
     */
    public boolean cambiarDestino(Usuario u, int n) {
        if(u == null || n < 1) return false;
        this.usuarioDestino = u;
        this.coste = n;
        return true;
    }
    
    /**
     * Calcula el coste especial del enlace.
     * Por defecto retorna 0.
     * @return Coste especial del enlace
     */
    public int costeEspecial() {
        return 0;
    }

    public int costeReal() {
        return this.costeEspecial() + this.coste;
    }

    @Override 
    public String toString() {
        return "(" + this.usuarioOrigen.getNombre() + "--" + this.coste + "-->" + this.usuarioDestino.getNombre() + ")";
    }
    
}