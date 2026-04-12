package estacion.formateador;

public class FormateadorHTML {
    public String formatear(IDocumento documento) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"es\">\n<head>\n");
        html.append(documento.getTituloDocumento());
        html.append("</head>\n<body>\n");
        
        html.append("<h1>").append(documento.getTituloSeccionPrincipal()).append("</h1>\n");
        for (String parrafo : documento.getParrafosSeccionPrincipal()) {
            html.append("<p>").append(parrafo).append("</p>\n");
        }
        
        for (StringLista seccion : documento.getSeccionesLista()) {
            html.append("<h2>").append(seccion.getTitulo()).append("</h2>\n<ul>\n");
            for (String item : seccion.getElementos()) {
                html.append("<li>").append(item).append("</li>\n");
            }
            html.append("</ul>\n");
        }
        
        html.append("</body>\n</html>\n");
        return html.toString();
    }
}
