public class DocEstacionMeteo implements IDocumento {
    private EstacionMeteo estacion;

    public DocEstacionMeteo(EstacionMeteo estacion) {
        this.estacion = estacion;
    }

    @Override
    public String getTituloDocumento() {
        return "Estación Metereológica: " + this.estacion.getNombre();
    }

    @Override
    public String getTituloSeccionPrincipal() {
        return this.estacion.getNombre();
    }

    @Override
    public List<String> getParrafosSeccionPrincipal() {
        List<String> parrafos = new ArrayList<>();
        parrafos.add("Ubicación: " + estacion.getUbicacion().getLatitud() + ", " + estacion.getUbicacion().getLongitud());
        parrafos.add("Sensores instalados: " + estacion.getSensores().size());
        parrafos.add("Última lectura: " + estacion.getUltimaLectura());
        return parrafos;
    }

    @Override
    public List<StringLista> getSeccionesLista() {
        List<StringLista> secciones = new ArrayList<>();
        List<String> sensoresInfo = new ArrayList<>();
        List<Alerta> alertasInfo = new ArrayList<>();
        for (Sensor sensor : estacion.obtenerSensores()) {
            sensoresInfo.add(sensor.toString());
        }
        for (Alerta alerta : estacion.getAlertas()) {
            alertasInfo.add(alerta.toString());
        }
        
        secciones.add(new StringLista("Sensores activos", sensoresInfo));
        secciones.add(new StringLista(("Alertas activas:" + alertasInfo.size()), alertasInfo));

        return secciones;
    }
}