package estacion.iDocumento;
    
import estacion.aux.StringLista;

public interface IDocumento {
    String getTituloDocumento();
    String getTituloSeccionPrincipal();
    List<String> getParrafosSeccionPrincipal();
    List<StringLista> getSeccionesLista();
}
