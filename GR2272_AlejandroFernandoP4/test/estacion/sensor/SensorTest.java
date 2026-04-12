/*
 * Pruebas unitarias de los sensores meteorológicos.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor;

import java.time.*;
import java.lang.reflect.*;

import estacion.sensor.estrategia.Estrategia;
import estacion.sensor.estrategia.EstrategiaAleatoria;

/**
 * Pruebas de comportamiento básico de los sensores.
 */
public class SensorTest {
    private static int testCount = 0;
    private static int passCount = 0;

    /**
     * Punto de entrada de las pruebas.
     *
     * @param args argumentos de línea de comandos.
     */
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

    /**
     * Comprueba que el sensor de temperatura tiene ID y unidad correctos.
     */
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

    /**
     * Comprueba que el sensor de temperatura puede medir tras calibrarse.
     */
    private static void testTemperaturaPuedeMedirTrasCalibrar() {
        testCount++;
        try {
            SensorTemperatura.numSensores = 0;
            SensorTemperatura sensor = new SensorTemperatura(2.5);
            sensor.estrategia = new Estrategia() {
                @Override
                public double generarValor(double min, double max) {
                    return 10.0;
                }
            };
            sensor.calibrar();
            
            assert sensor.puedeMedir() : "Debería poder medir tras calibrar";
            
            double lectura = sensor.medir();
            sensor.obtenerMedida();
            
            assert lectura == 10.0 : "Lectura de medir incorrecta";
            assert Math.abs(sensor.ultimaLectura - 7.5) < 0.00001 : "Lectura con offset incorrecta";
            assert sensor.fechaUltimaLectura != null : "Fecha de lectura no registrada";
            
            System.out.println("✓ testTemperaturaPuedeMedirTrasCalibrar PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testTemperaturaPuedeMedirTrasCalibrar FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que el sensor de humedad tiene ID y unidad correctos.
     */
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

    /**
     * Comprueba que el sensor de presión puede lanzar excepción por valor fuera de rango.
     */
    private static void testPresionLanzaExcepcion() {
        testCount++;
        try {
            SensorPresion.numSensores = 0;
            SensorPresion sensor = new SensorPresion(0.0);
            sensor.estrategia = new EstrategiaAleatoria(1.0);
            sensor.calibrar();

            boolean lanzoExcepcion = false;
            for (int i = 0; i < 200; i++) {
                try {
                    sensor.medir();
                } catch (RuntimeException e) {
                    lanzoExcepcion = true;
                    break;
                }
            }

            assert lanzoExcepcion : "No lanzó excepción";
            System.out.println("✓ testPresionLanzaExcepcion PASÓ");
            passCount++;
        } catch (Exception e) {
            System.out.println("✗ testPresionLanzaExcepcion FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que una calibración caducada impide medir.
     */
    private static void testCalibracionCaduca() {
        testCount++;
        try {
            SensorTemperatura.numSensores = 0;
            SensorTemperatura sensor = new SensorTemperatura(0.0);
            sensor.calibrar();

                try {
                    // Simula que la calibración caducó (hace 366 días)
                    setFieldValue(sensor, "fechaUltimaCalibracion", LocalDateTime.now().minusDays(366));
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

    /**
     * Comprueba el cambio de duración de calibración.
     */
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

    /**
     * Asigna un valor a un campo privado mediante reflexión.
     *
     * @param obj objeto objetivo.
     * @param fieldName nombre del campo.
     * @param value valor a asignar.
     * @throws Exception si falla el acceso reflejado.
     */
    private static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
