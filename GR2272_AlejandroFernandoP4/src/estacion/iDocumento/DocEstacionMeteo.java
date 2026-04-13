package estacion.iDocumento;

import java.util.ArrayList;
import java.util.List;

import estacion.EstacionMeteo;
import estacion.alerta.Alerta;
import estacion.aux.StringLista;
import estacion.sensor.Sensor;

/**
 * Implementación de IDocumento para generar documentación de la estación meteorológica.
 * @author Alejandro González
 * @author Fernando Blanco
 */
public class DocEstacionMeteo implements IDocumento {
    private final EstacionMeteo estacion;

    public DocEstacionMeteo(EstacionMeteo estacion) {
        this.estacion = estacion;
    }

    @Override
    public String getTituloDocumento() {
        return "Estación Meteorológica: " + this.estacion.getNombre();
    }

    @Override
    public String getTituloSeccionPrincipal() {
        return this.estacion.getNombre();
    }

    @Override
    public List<String> getParrafosSeccionPrincipal() {
        List<String> parrafos = new ArrayList<>();
        parrafos.add("Ubicación: " + estacion.getUbicacion().getLatitud() + ", " + estacion.getUbicacion().getLongitud());
        parrafos.add("Sensores instalados: " + estacion.obtenerSensores().size());
        parrafos.add("Última lectura: " + estacion.getUltimaLectura());
        return parrafos;
    }

    @Override
    public List<StringLista> getSeccionesLista() {
        List<StringLista> secciones = new ArrayList<>();
        List<String> sensoresInfo = new ArrayList<>();
        List<String> alertasInfo = new ArrayList<>();
        for (Sensor sensor : estacion.obtenerSensores()) {
            sensoresInfo.add(sensor.toString());
        }
        for (Alerta alerta : estacion.getAlertas()) {
            alertasInfo.add(alerta.toString());
        }

        secciones.add(new StringLista("Sensores activos", sensoresInfo));
        secciones.add(new StringLista("Alertas activas: " + alertasInfo.size(), alertasInfo));

        return secciones;
    }
}
