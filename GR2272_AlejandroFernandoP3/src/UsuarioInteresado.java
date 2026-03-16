/**
 * Representa a un usuario que prioriza enlaces hacia usuarios con alta exposición.
 * Si no encuentra uno de esos enlaces, devuelve el enlace directo al destino si existe.
 * Autor: Fernando Blanco
 */
public class UsuarioInteresado extends Usuario {
    /**
     * Construye un usuario interesado con el nombre indicado.
     * @param nombre el nombre del usuario
     */
    public UsuarioInteresado(String nombre) {
        super(nombre);
    }

    @Override
    /**
     * Devuelve preferentemente un enlace cuyo destino tenga exposición alta o viral.
     * Si no existe ninguno, busca el enlace directo al usuario destino.
     * @param destino el usuario al que se quiere llegar
     * @return el enlace prioritario encontrado o null si no hay enlace válido
     */
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
    /**
     * Devuelve una representación en cadena del usuario indicando que es interesado.
     * @return representación en cadena del usuario interesado
     */
    public String toString() {
        return super.toString() + "(Interesado)";
    }
}