package estacion;

import java.time.*;

public class EstacionMeteoTest {
    private static int testCount = 0;
    private static int passCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Ejecutando EstacionMeteoTest ===\n");
        
        testAgregaYRecuperaSensores();
        testAgregaSensorDuplicadoLanzaExcepcion();
        testAgregaSensorPorTipo();
        testAgregaSensorPorTipoInvalido();
        testLeerDatos();
        testLecturaPeriodica();
        
        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    private static void testAgregaYRecuperaSensores() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            estacion.sensor.SensorHumedad.numSensores = 0;
            estacion.sensor.SensorPresion.numSensores = 0;
            
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(2.0);
            estacion.sensor.SensorHumedad hum = new estacion.sensor.SensorHumedad(5.0);
            
            assert estacion.agregarSensor(temp) : "No agregó sensor temperatura";
            assert estacion.agregarSensor(hum) : "No agregó sensor humedad";
            
            assert estacion.encontrarSensorID("TEMP-0000") == temp : "No recuperó temp";
            assert estacion.encontrarSensorID("HUM-0000") == hum : "No recuperó hum";
            
            assert estacion.obtenerSensores().size() == 2 : "Lista de sensores incorrecta";
            
            System.out.println("✓ testAgregaYRecuperaSensores PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testAgregaYRecuperaSensores FALLÓ: " + e.getMessage());
        }
    }

    private static void testAgregaSensorDuplicadoLanzaExcepcion() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(1.0);
            
            assert estacion.agregarSensor(temp) : "No agregó sensor";
            
            try {
                estacion.agregarSensor(temp);
                System.out.println("✗ testAgregaSensorDuplicadoLanzaExcepcion FALLÓ: No lanzó excepción");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ testAgregaSensorDuplicadoLanzaExcepcion PASÓ");
                passCount++;
            }
        } catch (AssertionError e) {
            System.out.println("✗ testAgregaSensorDuplicadoLanzaExcepcion FALLÓ: " + e.getMessage());
        }
    }

    private static void testAgregaSensorPorTipo() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            estacion.sensor.SensorHumedad.numSensores = 0;
            estacion.sensor.SensorPresion.numSensores = 0;

            EstacionPrueba estacion = new EstacionPrueba();
            
            assert estacion.agregarSensor("TEMP", 1.5) : "No agregó TEMP";
            assert estacion.agregarSensor("HUM", 0.5) : "No agregó HUM";
            assert estacion.agregarSensor("PRES", 0.0) : "No agregó PRES";
            
            assert estacion.obtenerSensores().size() == 3 : "Tamaño de sensores incorrecto";
            
            System.out.println("✓ testAgregaSensorPorTipo PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testAgregaSensorPorTipo FALLÓ: " + e.getMessage());
        }
    }

    private static void testAgregaSensorPorTipoInvalido() {
        testCount++;
        try {
            EstacionPrueba estacion = new EstacionPrueba();
            
            try {
                estacion.agregarSensor("OTRO", 0.0);
                System.out.println("✗ testAgregaSensorPorTipoInvalido FALLÓ: No lanzó excepción");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ testAgregaSensorPorTipoInvalido PASÓ");
                passCount++;
            }
        } catch (Exception e) {
            System.out.println("✗ testAgregaSensorPorTipoInvalido FALLÓ: " + e.getMessage());
        }
    }

    private static void testLeerDatos() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            estacion.sensor.SensorHumedad.numSensores = 0;

            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(2.0);
            estacion.sensor.SensorHumedad hum = new estacion.sensor.SensorHumedad(5.0);
            temp.calibrar();
            hum.calibrar();
            
            assert estacion.agregarSensor(temp) : "No agregó temp";
            assert estacion.agregarSensor(hum) : "No agregó hum";
            
            assert estacion.leerDatos() : "No leyó datos";
            
            System.out.println("✓ testLeerDatos PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testLeerDatos FALLÓ: " + e.getMessage());
        }
    }

    private static void testLecturaPeriodica() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);
            temp.calibrar();
            
            assert estacion.agregarSensor(temp) : "No agregó sensor";
            assert estacion.lecturaPeriodica(Duration.ZERO, 2) : "No hizo lectura periódica";
            
            System.out.println("✓ testLecturaPeriodica PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testLecturaPeriodica FALLÓ: " + e.getMessage());
        }
    }

    private static class EstacionPrueba extends EstacionMeteo {
        EstacionPrueba() {
            super("Estacion de prueba", 40.0, -3.0);
        }
    }
}
