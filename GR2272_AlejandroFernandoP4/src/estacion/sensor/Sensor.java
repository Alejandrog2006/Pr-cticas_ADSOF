/*
 * Clase base de todos los sensores meteorológicos del proyecto.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor;

import java.time.*;

import estacion.sensor.estrategia.Estrategia;
import estacion.sensor.estrategia.EstrategiaAleatoria;
import estacion.unidadLectura.*;

/**
 * Clase abstracta que encapsula el comportamiento común de los sensores.
 */
public abstract class Sensor {
    private Duration intervaloCalibracion = Duration.ofDays(365); // por defecto
    public final LocalDateTime fechaInstalacion;
    protected String id;
    protected double offset;
    protected UnidadLectura unidadLectura;
    protected LocalDateTime fechaUltimaLectura;
    protected double ultimaLectura;
    protected LocalDateTime fechaUltimaCalibracion;
    protected boolean estadoCalibracion;
    protected Estrategia estrategia = new EstrategiaAleatoria(); // por defecto, se puede cambiar con un setter si se quiere otra estrategia o con el otro constructor

    /**
     * Crea un sensor con el offset indicado y estrategia aleatoria por defecto.
     *
     * @param offset corrección fija de la lectura.
     */
    public Sensor(double offset) {
        this.offset = offset;
        this.fechaUltimaLectura = null;
        this.ultimaLectura = 0.0;
        this.fechaUltimaCalibracion = null;
        this.estadoCalibracion = false;
        this.fechaInstalacion = LocalDateTime.now();
    }

    /**
     * Crea un sensor con offset, estrategia e intervalo de calibración concretos.
     *
     * @param offset corrección fija de la lectura.
     * @param estrategia estrategia de generación de valores.
     * @param intervaloCalibracion duración de la calibración.
     */
    public Sensor(double offset, Estrategia estrategia, Duration intervaloCalibracion) {
        this.offset = offset;
        this.fechaUltimaLectura = null;
        this.ultimaLectura = 0.0;
        this.fechaUltimaCalibracion = null;
        this.estadoCalibracion = false;
        this.fechaInstalacion = LocalDateTime.now();
        this.estrategia = estrategia;
        this.intervaloCalibracion = intervaloCalibracion;
    }

    /**
     * Indica si el sensor puede medir en este instante.
     *
     * @return {@code true} si puede medir.
     */
    public boolean puedeMedir() {
        if (this.fechaUltimaCalibracion == null || !this.estadoCalibracion) {
            return false;
        }

        Duration tiempoDesdeUltimaCalibracion = Duration.between(this.fechaUltimaCalibracion, LocalDateTime.now());
        if (tiempoDesdeUltimaCalibracion.compareTo(intervaloCalibracion) >= 0) {
            this.estadoCalibracion = false;
            return false;
        }

        return true;
    }

    /**
     * Obtiene una lectura del sensor.
     *
     * @return valor bruto medido por el sensor.
     */
    public abstract double medir();

    /**
     * Toma una lectura, aplica el offset y la almacena como última lectura.
     *
     * @return {@code true} si la lectura se ha registrado.
     */
    public boolean obtenerMedida() {
        this.ultimaLectura = this.medir() - this.offset; // no se si hay que hacer cath de la excepcion de medir
        this.fechaUltimaLectura = LocalDateTime.now();
        return true;
    }

    /*Gestión de calibración*/
    /**
     * Calibra el sensor con la duración por defecto.
     */
    public void calibrar() {
        this.fechaUltimaCalibracion = LocalDateTime.now();
        this.estadoCalibracion = true;
    }

    /**
     * Calibra el sensor con una duración concreta.
     *
     * @param duracion duración de la calibración.
     */
    public void calibrar(Duration duracion) {
        
        if(setDuracionCalibracion(duracion)) {
            this.fechaUltimaCalibracion = LocalDateTime.now();
            this.estadoCalibracion = true;
        } else {
            throw new IllegalArgumentException("Duración de calibración no válida: " + duracion);
        }
    }

    /**
     * Calibra el sensor con una fecha de caducidad concreta.
     *
     * @param fechaCaducidad fecha de caducidad de la calibración.
     */
    public void calibrar(LocalDateTime fechaCaducidad) {
        if(setDuracionCalibracion(fechaCaducidad)) {
            this.fechaUltimaCalibracion = LocalDateTime.now();
            this.estadoCalibracion = true;
        } else {
            throw new IllegalArgumentException("Fecha de caducidad de calibración no válida: " + fechaCaducidad);
        }
    }

    /**
     * Cambia la duración de calibración del sensor.
     *
     * @param duracion nueva duración.
     * @return {@code true} si la duración es válida.
     */
    public boolean setDuracionCalibracion(Duration duracion) {
        if (duracion.isNegative() || duracion.isZero()) {
            return false;
        }
        
        this.intervaloCalibracion = duracion;
        return true;
    }

    /**
     * Cambia la fecha de caducidad de calibración del sensor.
     *
     * @param fechaCaducidad nueva fecha de caducidad.
     * @return {@code true} si la fecha es válida.
     */
    public boolean setDuracionCalibracion(LocalDateTime fechaCaducidad) {
        if (fechaCaducidad.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        this.intervaloCalibracion = Duration.between(LocalDateTime.now(), fechaCaducidad);
        return true;
    }
    
    /**
     * Obtiene el identificador del sensor.
     *
     * @return identificador del sensor.
     */
    public String getId() {
        return this.id;
    } 

    /**
     * Obtiene la unidad de lectura del sensor.
     *
     * @return unidad de lectura.
     */
    public UnidadLectura getUnidad() {
        return this.unidadLectura;
    }

    /**
     * Obtiene la fecha de la última lectura.
     *
     * @return fecha de la última lectura o {@code null} si no existe.
     */
    public LocalDateTime getFechaUltimaLectura() {
        return this.fechaUltimaLectura;
    }

    /**
     * Obtiene la última lectura almacenada.
     *
     * @return última lectura registrada.
     */
    public double getUltimaLectura() {
        return this.ultimaLectura;
    }

    /**
     * Establece el offset del sensor.
     *
     * @param offset nuevo offset.
     */
    public void setOffset(double offset) {
        this.offset = offset;
    }

    /**
     * Obtiene el offset del sensor.
     *
     * @return offset actual.
     */
    public double getOffset() {
        return this.offset;
    }
}