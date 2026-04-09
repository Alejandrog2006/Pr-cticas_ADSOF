package estacion.sensor;

import java.time.*;
import java.lang.reflect.*;

public class SensorTest {
    private static int testCount = 0;
    private static int passCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Ejecutando SensorTest ===\n");
        
        testTemperaturaTieneIdYUnidad();
        testTemperaturaPuedeMedirTrasCalibrar();
        testHumedadTieneIdYUnidad();
        testPresionLanzaExcepcion();
        testCalibracionCaduca();
        testCambiarDuracionCalibracion();
        
        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    private static void testTemperaturaTieneIdYUnidad() {
        testCount++;
        try {
            SensorTemperatura.numSensores = 0;
            SensorTemperatura sensor = new SensorTemperatura(2.5);
            
            assert sensor.getId().equals("TEMP-0000") : "ID incorrecto";
            assert sensor.unidadLectura.imprimirSimbolo().equals("Cº") : "Unidad incorrecta";
            assert !sensor.puedeMedir() : "No debería poder medir sin calibrar";
            
            System.out.println("✓ testTemperaturaTieneIdYUnidad PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testTemperaturaTieneIdYUnidad FALLÓ: " + e.getMessage());
        }
    }

    private static void testTemperaturaPuedeMedirTrasCalibrar() {
        testCount++;
        try {
            SensorTemperatura.numSensores = 0;
            SensorTemperatura sensor = new SensorTemperatura(2.5);
            sensor.calibrar();
            
            assert sensor.puedeMedir() : "Debería poder medir tras calibrar";
            
            double lectura = sensor.medir();
            sensor.obtenerMedida();
            
            assert Math.abs(sensor.ultimaLectura - (-2.5)) < 0.00001 : "Lectura con offset incorrecta";
            assert sensor.fechaUltimaLectura != null : "Fecha de lectura no registrada";
            
            System.out.println("✓ testTemperaturaPuedeMedirTrasCalibrar PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testTemperaturaPuedeMedirTrasCalibrar FALLÓ: " + e.getMessage());
        }
    }

    private static void testHumedadTieneIdYUnidad() {
        testCount++;
        try {
            SensorHumedad.numSensores = 0;
            SensorHumedad sensor = new SensorHumedad(1.0);
            
            assert sensor.getId().equals("HUM-0000") : "ID incorrecto";
            assert sensor.unidadLectura.imprimirSimbolo().equals("%") : "Unidad incorrecta";
            assert !sensor.puedeMedir() : "No debería poder medir sin calibrar";
            
            System.out.println("✓ testHumedadTieneIdYUnidad PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testHumedadTieneIdYUnidad FALLÓ: " + e.getMessage());
        }
    }

    private static void testPresionLanzaExcepcion() {
        testCount++;
        try {
            SensorPresion.numSensores = 0;
            SensorPresion sensor = new SensorPresion(0.0);
            sensor.calibrar();
            
            try {
                sensor.medir();
                System.out.println("✗ testPresionLanzaExcepcion FALLÓ: No lanzó excepción");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ testPresionLanzaExcepcion PASÓ");
                passCount++;
            }
        } catch (Exception e) {
            System.out.println("✗ testPresionLanzaExcepcion FALLÓ: " + e.getMessage());
        }
    }

    private static void testCalibracionCaduca() {
        testCount++;
        try {
            SensorTemperatura.numSensores = 0;
            SensorTemperatura sensor = new SensorTemperatura(0.0);
            sensor.calibrar();

                try {
                    // Simula que la calibración caducó (hace 31 días)
                    setFieldValue(sensor, "fechaUltimaCalibracion", LocalDateTime.now().minusDays(31));
                    setFieldValue(sensor, "estadoCalibracion", true);
                } catch (Exception e) {
                    System.out.println("✗ testCalibracionCaduca FALLÓ: " + e.getMessage());
                    return;
                }

                assert !sensor.puedeMedir() : "No debería poder medir con calibración caducada";
            
            System.out.println("✓ testCalibracionCaduca PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testCalibracionCaduca FALLÓ: " + e.getMessage());
        }
    }

    private static void testCambiarDuracionCalibracion() {
        testCount++;
        try {
            SensorTemperatura.numSensores = 0;
            SensorTemperatura sensor = new SensorTemperatura(0.0);
            
            assert sensor.setDuracionCalibracion(Duration.ofDays(10)) : "Debería aceptar duración válida";
            assert !sensor.setDuracionCalibracion(Duration.ZERO) : "Debería rechazar ZERO";
            assert !sensor.setDuracionCalibracion(Duration.ofDays(-1)) : "Debería rechazar negativo";
            
            System.out.println("✓ testCambiarDuracionCalibracion PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testCambiarDuracionCalibracion FALLÓ: " + e.getMessage());
        }
    }

    private static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
