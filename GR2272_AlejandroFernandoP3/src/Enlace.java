/**
 * Esta clase representa un enlace entre dos usuarios.
 * Un enlace tiene un usuario origen, un usuario destino y un coste.
 * Permite gestionar los datos del enlace y calcular el coste real.
 * Autor: Alejandro González y Fernando Blanco
 * Version: 1.0
 * Nombre del fichero: Enlace.java
 */
public class Enlace {
    protected Usuario usuarioOrigen;
    protected Usuario usuarioDestino;
    protected int coste;
    private static int costeTotal=0;

    /**
     * Constructor que inicializa un enlace con usuario origen, destino y coste.
     * @param usuarioOrigen Usuario de origen del enlace
     * @param usuarioDestino Usuario de destino del enlace
     * @param coste Coste del enlace
     */
    public Enlace(Usuario usuarioOrigen, Usuario usuarioDestino, int coste) {
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        if(coste < 1) {
            this.coste = 1;
        } else {
            this.coste = coste;
        }
        costeTotal += this.coste;
    }


    /**
     * Obtiene el usuario origen del enlace.
     * @return el usuario origen
     */
    public Usuario getUsuarioO() {
        return this.usuarioOrigen;
    }

    /**
     * Obtiene el usuario destino del enlace.
     * @return el usuario destino
     */
    public Usuario getUsuarioD() {
        return this.usuarioDestino;
    }

    /**
     * Obtiene el costo del enlace.
     * @return el costo
     */
    public int getCoste() {
        return this.coste;
    }

    /**
     * Obtiene el costo total acumulado por todos los enlaces.
     * @return el costo total
     */
    public int getCosteTotal() {
        return costeTotal;
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

    /**
     * Calcula el costo real del enlace, incluyendo el costo especial.
     * @return el costo real
     */
    public int costeReal() {
        return this.costeEspecial() + this.coste;
    }

    /**
     * Devuelve una representación en cadena del enlace.
     * @return representación en cadena
     */
    @Override 
    public String toString() {
        return "(" + this.usuarioOrigen.getNombre() + "--" + this.coste + "-->" + this.usuarioDestino.getNombre() + ")";
    }
    
}