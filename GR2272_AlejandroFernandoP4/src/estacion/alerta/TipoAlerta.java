/*
 * Tipos de alerta gestionados por la estación meteorológica.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.alerta;

/**
 * Enumeración de los tipos de alerta soportados por el sistema.
 */
public enum TipoAlerta {
    SENSOR_NO_CALIBRADO,
    LECTURA_FUERA_DE_RANGO,
    CAMBIO_BRUSCO
}
