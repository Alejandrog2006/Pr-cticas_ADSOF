public class EnlaceSenuelo extends Enlace{
    private int factorExtra;
    private double probabilidad;

    public EnlaceSenuelo(Usuario usuarioOrigen, Usuario usuarioDestino, int coste, 
        int factorExtra, double probabilidad) {
            super(usuarioOrigen, usuarioDestino, coste);
            this.factorExtra = factorExtra;
            this.probabilidad = probabilidad;
    }

    /**
     * Calcula el coste especial del enlace.
     * Por defecto retorna 0.
     * @return Coste especial del enlace
     */
    @Override 
    public int costeEspecial() {
        return coste * factorExtra;
    }

    @Override
    public Usuario getUsuarioD() {
        double chance = Math.random();

        if (chance <= probabilidad) {
            return this.getUsuarioO();
        } else {
            return super.getUsuarioD(); // El enlace no se establece
        }
    }


}
