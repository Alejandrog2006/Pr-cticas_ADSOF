public class UsuarioInteresado extends Usuario {
    public UsuarioInteresado(String nombre) {
        super(nombre);
    }

    @Override
    public Enlace getEnlace(Usuario destino) {

        for(Enlace e : this.enlaces) {
            Exposicion exp = e.getUsuarioD().getExposicion();
            if(exp == Exposicion.ALTA || exp == Exposicion.VIRAL) {
                return e;
            }
        }

        for(Enlace e : this.enlaces) {
            if(e.getUsuarioD() == destino) {
                return e;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "(Interesado)";
    }
}