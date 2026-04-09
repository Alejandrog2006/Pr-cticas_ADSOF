package estacion.sensor;

import java.time.*;
import estacion.unidadLectura.*;

public abstract class Sensor {
    private static Duration intervaloCalibracion = Duration.ofDays(30);

    public final LocalDateTime fechaInstalacion;
    protected String id;
    protected double offset;
    protected UnidadLectura unidadLectura;
    protected LocalDateTime fechaUltimaLectura;
    protected double ultimaLectura;
    protected LocalDateTime fechaUltimaCalibracion;
    protected boolean estadoCalibracion;

    public Sensor(double offset) {

        this.offset = offset;
        this.fechaUltimaLectura = null;
        this.ultimaLectura = 0.0;
        this.fechaUltimaCalibracion = null;
        this.estadoCalibracion = false;
        this.fechaInstalacion = LocalDateTime.now();
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

    public boolean setDuracionCalibracion(Duration duracion) {
        if (duracion.isNegative() || duracion.isZero()) {
            return false;
        }
        
        Sensor.intervaloCalibracion = duracion;
        return true;
    }
    
    public String getId() {
        return this.id;
    } 

}