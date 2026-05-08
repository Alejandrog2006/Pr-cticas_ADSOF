package estacion.formateador;

import estacion.iDocumento.IDocumento;

public interface Formateador {
    String formatear(IDocumento documento);
}
