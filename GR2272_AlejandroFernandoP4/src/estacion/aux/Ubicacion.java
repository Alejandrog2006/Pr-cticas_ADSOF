/*
 * Clase auxiliar para representar la ubicación geográfica de la estación meteorológica.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.aux;
/**
 * Representa una ubicación geográfica mediante latitud y longitud.
 */
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