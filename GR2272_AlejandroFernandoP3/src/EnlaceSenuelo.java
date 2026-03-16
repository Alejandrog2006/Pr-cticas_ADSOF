/**
 * Representa un enlace señuelo que puede redirigir al usuario de origen con una cierta probabilidad.
 * Amplia la clase Enlace con un coste especial y un comportamiento alternativo para el destino.
 */
public class EnlaceSenuelo extends Enlace{
    private int factorExtra;
    private double probabilidad;

    /**
     * Construye un enlace señuelo.
     * @param usuarioOrigen el usuario de origen
     * @param usuarioDestino el usuario destino previsto
     * @param coste el coste base del enlace
     * @param factorExtra el factor adicional para calcular el coste especial
     * @param probabilidad la probabilidad de redirigir al origen
     */
    public EnlaceSenuelo(Usuario usuarioOrigen, Usuario usuarioDestino, int coste, 
        int factorExtra, double probabilidad) {
            super(usuarioOrigen, usuarioDestino, coste);
            this.factorExtra = factorExtra;
            this.probabilidad = probabilidad;
    }

    /**
     * Calcula el coste especial del enlace señuelo.
     * @return el coste especial
     */
    @Override 
    public int costeEspecial() {
        return coste * factorExtra;
    }

    /**
     * Obtiene el usuario destino, con posibilidad de redirigir al origen según la probabilidad.
     * @return el usuario destino o el usuario de origen si se produce la redirección
     */
    @Override
    public Usuario getUsuarioD() {
        double chance = Math.random();

        if (chance <= probabilidad) {
            return this.getUsuarioO();
        } else {
            return super.getUsuarioD(); // El enlace no se establece
        }
    }

    /**
     * Devuelve una representación en cadena del enlace.
     * @return representación en cadena
     */
    @Override 
    public String toString() {
        return super.toString()+ "[FactorExtra: " + this.factorExtra + ", Probabilidad: " + this.probabilidad + "]";
    }

}
