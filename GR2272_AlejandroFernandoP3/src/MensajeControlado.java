public class MensajeControlado extends Mensaje {
    private int rigidez;

    public MensajeControlado(String mensaje, int capac, Usuario usuarioActual, int rigidez) {
        super(mensaje, capac, usuarioActual);
        this.rigidez = rigidez;
    }

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

    
}

