<<<<<<< HEAD:GR2272_AlejandroFernandoP4/src/estacion/aux/Ubicacion.java
package estacion.aux;

=======
/*
 * Clase auxiliar para representar la ubicación geográfica de la estación meteorológica.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion;
/**
 * Representa una ubicación geográfica mediante latitud y longitud.
 */
>>>>>>> 14e0ac48051c67056a7d177bb4a619525c86b4d6:GR2272_AlejandroFernandoP4/src/estacion/Ubicacion.java
public class Ubicacion {
    private double latitud;
    private double longitud;

    /**
     * Crea una nueva ubicación con la latitud y longitud indicadas.
     *
     * @param latitud latitud del punto.
     * @param longitud longitud del punto.
     */
    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
    
    /**
     * Obtiene la latitud.
     *
     * @return latitud de la ubicación.
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * Obtiene la longitud.
     *
     * @return longitud de la ubicación.
     */
    public double getLongitud() {
        return longitud;    
    }
}