package estacion.formateador;

public class FormateadorMarkdown {
    public String formatear(IDocumento documento) {
        StringBuilder markdown = new StringBuilder();
        markdown.append("# ").append(documento.getTituloDocumento()).append("\n\n");
        
        markdown.append("## ").append(documento.getTituloSeccionPrincipal()).append("\n\n");
        for (String parrafo : documento.getParrafosSeccionPrincipal()) {
            markdown.append(parrafo).append("\n\n");
        }
        
        for (StringLista seccion : documento.getSeccionesLista()) {
            markdown.append("### ").append(seccion.getTitulo()).append("\n\n");
            for (String item : seccion.getElementos()) {
                markdown.append("- ").append(item).append("\n");
            }
            markdown.append("\n");
        }
        
        return markdown.toString();
    }
}
