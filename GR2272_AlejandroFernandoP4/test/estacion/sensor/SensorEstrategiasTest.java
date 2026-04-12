/*
 * Pruebas de las estrategias de generación de valores para sensores.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion.sensor;

import estacion.sensor.estrategia.EstrategiaAleatoria;
import estacion.sensor.estrategia.EstrategiaIncremental;
import estacion.sensor.estrategia.EstrategiaMedia;

/**
 * Pruebas de las estrategias de generación de lecturas.
 */
public class SensorEstrategiasTest {
    private static int testCount = 0;
    private static int passCount = 0;

    /**
     * Punto de entrada de las pruebas.
     *
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        System.out.println("=== Ejecutando SensorEstrategiasTest ===\n");

        testAleatoriaConProbabilidadCeroPermaneceEnRango();
        testAleatoriaConProbabilidadUnoTerminaLanzandoExcepcion();
        testIncrementalConParametroCeroMantieneValor();
        testMediaConParametroCeroProduceValoresValidos();
        testConstructoresEstrategiasValidanParametros();

        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    /**
     * Comprueba que la estrategia aleatoria con probabilidad cero se mantiene en rango.
     */
    private static void testAleatoriaConProbabilidadCeroPermaneceEnRango() {
        testCount++;
        try {
            SensorTemperatura sensor = new SensorTemperatura(0.0);
            sensor.estrategia = new EstrategiaAleatoria(0.0);
            sensor.calibrar();

            for (int i = 0; i < 100; i++) {
                double medida = sensor.medir();
                assert medida >= -273.15 && medida <= 1000.0 : "Valor fuera de rango con probabilidad 0";
            }

            System.out.println("✓ testAleatoriaConProbabilidadCeroPermaneceEnRango PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testAleatoriaConProbabilidadCeroPermaneceEnRango FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que la estrategia aleatoria con probabilidad uno acaba generando una excepción.
     */
    private static void testAleatoriaConProbabilidadUnoTerminaLanzandoExcepcion() {
        testCount++;
        try {
            SensorHumedad sensor = new SensorHumedad(0.0);
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

            assert lanzoExcepcion : "Con probabilidad 1 debía acabar lanzando excepción por fuera de rango";

            System.out.println("✓ testAleatoriaConProbabilidadUnoTerminaLanzandoExcepcion PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testAleatoriaConProbabilidadUnoTerminaLanzandoExcepcion FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que la estrategia incremental con parámetro cero mantiene el valor.
     */
    private static void testIncrementalConParametroCeroMantieneValor() {
        testCount++;
        try {
            SensorHumedad sensor = new SensorHumedad(0.0);
            sensor.estrategia = new EstrategiaIncremental(0);
            sensor.calibrar();

            double primera = sensor.medir();
            double segunda = sensor.medir();

            assert primera == 0.0 : "La primera medida esperada con parametro 0 es 0.0";
            assert segunda == primera : "Con parametro 0 debería mantenerse el valor anterior";

            System.out.println("✓ testIncrementalConParametroCeroMantieneValor PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testIncrementalConParametroCeroMantieneValor FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que la estrategia media con parámetro cero produce valores válidos.
     */
    private static void testMediaConParametroCeroProduceValoresValidos() {
        testCount++;
        try {
            SensorPresion sensor = new SensorPresion(0.0);
            sensor.estrategia = new EstrategiaMedia(0);
            sensor.calibrar();

            for (int i = 0; i < 100; i++) {
                double medida = sensor.medir();
                assert medida >= 300.0 && medida <= 1100.0 : "Valor fuera de rango con estrategia media parametro 0";
            }

            System.out.println("✓ testMediaConParametroCeroProduceValoresValidos PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testMediaConParametroCeroProduceValoresValidos FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que los constructores de estrategias validan sus parámetros.
     */
    private static void testConstructoresEstrategiasValidanParametros() {
        testCount++;
        try {
            boolean aleatoriaOk = false;
            boolean incrementalOk = false;
            boolean mediaOk = false;

            try {
                new EstrategiaAleatoria(-0.01);
            } catch (IllegalArgumentException e) {
                aleatoriaOk = true;
            }

            try {
                new EstrategiaIncremental(101);
            } catch (IllegalArgumentException e) {
                incrementalOk = true;
            }

            try {
                new EstrategiaMedia(-1);
            } catch (IllegalArgumentException e) {
                mediaOk = true;
            }

            assert aleatoriaOk : "EstrategiaAleatoria debía validar la probabilidad";
            assert incrementalOk : "EstrategiaIncremental debía validar el parámetro";
            assert mediaOk : "EstrategiaMedia debía validar el parámetro";

            System.out.println("✓ testConstructoresEstrategiasValidanParametros PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testConstructoresEstrategiasValidanParametros FALLÓ: " + e.getMessage());
        }
    }
}
