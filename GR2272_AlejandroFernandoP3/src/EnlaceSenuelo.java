/**
 * Represents a decoy link that may redirect to the origin user with a certain probability.
 * Extends Enlace with additional behavior for special cost and destination.
 */
public class EnlaceSenuelo extends Enlace{
    private int factorExtra;
    private double probabilidad;

    /**
     * Constructs a decoy link.
     * @param usuarioOrigen the origin user
     * @param usuarioDestino the intended destination user
     * @param coste the base cost
     * @param factorExtra the extra factor for special cost
     * @param probabilidad the probability of redirecting to origin
     */
    public EnlaceSenuelo(Usuario usuarioOrigen, Usuario usuarioDestino, int coste, 
        int factorExtra, double probabilidad) {
            super(usuarioOrigen, usuarioDestino, coste);
            this.factorExtra = factorExtra;
            this.probabilidad = probabilidad;
    }

    /**
     * Calculates the special cost of the decoy link.
     * @return the special cost
     */
    @Override 
    public int costeEspecial() {
        return coste * factorExtra;
    }

    /**
     * Gets the destination user, possibly redirecting to origin based on probability.
     * @return the destination user or origin if redirected
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
