package estacion.sensor;

import java.time.*;

import estacion.sensor.estrategia.Estrategia;
import estacion.sensor.estrategia.EstrategiaAleatoria;
import estacion.unidadLectura.*;

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

    public Sensor(double offset) {
        this.offset = offset;
        this.fechaUltimaLectura = null;
        this.ultimaLectura = 0.0;
        this.fechaUltimaCalibracion = null;
        this.estadoCalibracion = false;
        this.fechaInstalacion = LocalDateTime.now();
    }

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

    public abstract double medir();

    // he puesto que sea boolean, y que si quieres ver el valor hagas sensor.ultimalectura
    public boolean obtenerMedida() {
        this.ultimaLectura = this.medir() - this.offset; // no se si hay que hacer cath de la excepcion de medir
        this.fechaUltimaLectura = LocalDateTime.now();
        return true;
    }

    /*Gestión de calibración*/
    public void calibrar() {
        this.fechaUltimaCalibracion = LocalDateTime.now();
        this.estadoCalibracion = true;
    }

    public void calibrar(Duration duracion) {
        
        if(setDuracionCalibracion(duracion)) {
            this.fechaUltimaCalibracion = LocalDateTime.now();
            this.estadoCalibracion = true;
        } else {
            throw new IllegalArgumentException("Duración de calibración no válida: " + duracion);
        }
    }

    public void calibrar(LocalDateTime fechaCaducidad) {
        if(setDuracionCalibracion(fechaCaducidad)) {
            this.fechaUltimaCalibracion = LocalDateTime.now();
            this.estadoCalibracion = true;
        } else {
            throw new IllegalArgumentException("Fecha de caducidad de calibración no válida: " + fechaCaducidad);
        }
    }

    public boolean setDuracionCalibracion(Duration duracion) {
        if (duracion.isNegative() || duracion.isZero()) {
            return false;
        }
        
        this.intervaloCalibracion = duracion;
        return true;
    }

    public boolean setDuracionCalibracion(LocalDateTime fechaCaducidad) {
        if (fechaCaducidad.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        this.intervaloCalibracion = Duration.between(LocalDateTime.now(), fechaCaducidad);
        return true;
    }
    
    public String getId() {
        return this.id;
    } 

    public UnidadLectura getUnidad() {
        return this.unidadLectura;
    }

    public LocalDateTime getFechaUltimaLectura() {
        return this.fechaUltimaLectura;
    }

    public double getUltimaLectura() {
        return this.ultimaLectura;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getOffset() {
        return this.offset;
    }
}