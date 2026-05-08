package estacion.formateador;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import estacion.aux.StringLista;
import estacion.iDocumento.IDocumento;

/**
 * Pruebas unitarias de los formateadores.
 * @author Alejandro González
 * @author Fernando Blanco
 */
public class FormateadorTest {
    private static int testCount = 0;
    private static int passCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Ejecutando FormateadorTest ===\n");

        testFormateadorHTMLConDocumentoSimple();
        testFormateadorMarkdownConDocumentoSimple();
        testFormateadoresConSeccionesVacias();

        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    private static void testFormateadorHTMLConDocumentoSimple() {
        testCount++;
        try {
            IDocumento documento = new DocumentoStub(
                "Estación Meteorológica: Madrid Centro",
                "Madrid Centro",
                Arrays.asList(
                    "Ubicación: 40.4168, -3.7038",
                    "Sensores instalados: 2"
                ),
                Arrays.asList(
                    new StringLista("Sensores activos", Arrays.asList("TEMP-0001 (Cº): 20.5", "HUM-0001 (%): 65.0")),
                    new StringLista("Alertas activas: 0", Collections.emptyList())
                )
            );

            Formateador formateador = new FormateadorHTML();
            String html = formateador.formatear(documento);

            assert html.contains("<!DOCTYPE html>") : "Debe incluir el doctype HTML";
            assert html.contains("<title>Estación Meteorológica: Madrid Centro</title>") : "Debe incluir el título del documento";
            assert html.contains("<h1>Madrid Centro</h1>") : "Debe incluir el título principal";
            assert html.contains("<p>Ubicación: 40.4168, -3.7038</p>") : "Debe incluir el primer párrafo";
            assert html.contains("<h2>Sensores activos</h2>") : "Debe incluir el título de la lista";
            assert html.contains("<li>TEMP-0001 (Cº): 20.5</li>") : "Debe incluir el primer elemento de lista";
            assert html.contains("</html>") : "Debe cerrar el documento HTML";

            System.out.println("testFormateadorHTMLConDocumentoSimple PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testFormateadorHTMLConDocumentoSimple FALLÓ: " + e.getMessage());
        }
    }

    private static void testFormateadorMarkdownConDocumentoSimple() {
        testCount++;
        try {
            IDocumento documento = new DocumentoStub(
                "Estación Meteorológica: Madrid Centro",
                "Madrid Centro",
                Arrays.asList(
                    "Ubicación: 40.4168, -3.7038",
                    "Última lectura: 2026-03-12T20:42:29"
                ),
                Arrays.asList(
                    new StringLista("Sensores activos", Arrays.asList("TEMP-0001 (Cº): 20.5"))
                )
            );

            Formateador formateador = new FormateadorMarkdown();
            String markdown = formateador.formatear(documento);

            assert markdown.startsWith("# Estación Meteorológica: Madrid Centro") : "Debe empezar con el título de documento";
            assert markdown.contains("## Madrid Centro") : "Debe incluir la sección principal";
            assert markdown.contains("Ubicación: 40.4168, -3.7038") : "Debe incluir los párrafos";
            assert markdown.contains("### Sensores activos") : "Debe incluir el título de subsección";
            assert markdown.contains("- TEMP-0001 (Cº): 20.5") : "Debe incluir el elemento de lista en Markdown";

            System.out.println("testFormateadorMarkdownConDocumentoSimple PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testFormateadorMarkdownConDocumentoSimple FALLÓ: " + e.getMessage());
        }
    }

    private static void testFormateadoresConSeccionesVacias() {
        testCount++;
        try {
            IDocumento documento = new DocumentoStub(
                "Documento vacío",
                "Sección principal",
                Collections.emptyList(),
                Collections.emptyList()
            );

            String html = new FormateadorHTML().formatear(documento);
            String markdown = new FormateadorMarkdown().formatear(documento);

            assert html.contains("<h1>Sección principal</h1>") : "HTML debe incluir la cabecera principal aunque no haya contenido";
            assert !html.contains("<h2>") : "HTML no debe crear subsecciones inexistentes";
            assert markdown.contains("## Sección principal") : "Markdown debe incluir la cabecera principal";
            assert !markdown.contains("### ") : "Markdown no debe crear subsecciones inexistentes";

            System.out.println("testFormateadoresConSeccionesVacias PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testFormateadoresConSeccionesVacias FALLÓ: " + e.getMessage());
        }
    }

    private static class DocumentoStub implements IDocumento {
        private final String tituloDocumento;
        private final String tituloSeccionPrincipal;
        private final List<String> parrafos;
        private final List<StringLista> secciones;

        DocumentoStub(String tituloDocumento, String tituloSeccionPrincipal,
                      List<String> parrafos, List<StringLista> secciones) {
            this.tituloDocumento = tituloDocumento;
            this.tituloSeccionPrincipal = tituloSeccionPrincipal;
            this.parrafos = parrafos;
            this.secciones = secciones;
        }

        @Override
        public String getTituloDocumento() {
            return tituloDocumento;
        }

        @Override
        public String getTituloSeccionPrincipal() {
            return tituloSeccionPrincipal;
        }

        @Override
        public List<String> getParrafosSeccionPrincipal() {
            return parrafos;
        }

        @Override
        public List<StringLista> getSeccionesLista() {
            return secciones;
        }
    }
}
