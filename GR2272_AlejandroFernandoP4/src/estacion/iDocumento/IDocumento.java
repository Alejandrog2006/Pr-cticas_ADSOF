package estacion.iDocumento;

import java.util.List;

import estacion.aux.StringLista;

/**
 * Interfaz para documentos generados por la estación meteorológica.
 * @author Alejandro González
 * @author Fernando Blanco
 */
public interface IDocumento {
    String getTituloDocumento();
    String getTituloSeccionPrincipal();
    List<String> getParrafosSeccionPrincipal();
    List<StringLista> getSeccionesLista();
}
