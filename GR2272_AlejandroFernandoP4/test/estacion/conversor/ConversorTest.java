package estacion.conversor;

import estacion.conversor.*;
import estacion.unidadLectura.*;

/**
 * Pruebas unitarias de los conversores de temperatura.
 * @author Alejandro González
 * @author Fernando Blanco
 */
public class ConversorTest {
    private static int testCount = 0;
    private static int passCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Ejecutando ConversorTest ===\n");

        testCelsiusAKelvin();
        testFahrenheitACelsius();
        testKelvinAFahrenheitEncadenado();

        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    private static void testCelsiusAKelvin() {
        testCount++;
        try {
            double valor = 20.5;
            double esperado = 293.65;
            double resultado = ConversorTemperatura.CELSIUS_KELVIN.convertir(valor);

            assert Math.abs(resultado - esperado) < 0.01 : "Conversión Celsius a Kelvin incorrecta";
            assert ConversorTemperatura.CELSIUS_KELVIN.unidadInicial() == UnidadTemperatura.CELSIUS
                : "Unidad inicial incorrecta";
            assert ConversorTemperatura.CELSIUS_KELVIN.unidadFinal() == UnidadTemperatura.KELVIN
                : "Unidad final incorrecta";

            System.out.println("testCelsiusAKelvin PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testCelsiusAKelvin FALLÓ: " + e.getMessage());
        }
    }

    private static void testFahrenheitACelsius() {
        testCount++;
        try {
            double valor = 68.0;
            double esperado = 20.0;
            double resultado = ConversorTemperatura.FAHRENHEIT_CELSIUS.convertir(valor);

            assert Math.abs(resultado - esperado) < 0.01 : "Conversión Fahrenheit a Celsius incorrecta";
            assert ConversorTemperatura.FAHRENHEIT_CELSIUS.unidadInicial() == UnidadTemperatura.FAHRENHEIT
                : "Unidad inicial incorrecta";
            assert ConversorTemperatura.FAHRENHEIT_CELSIUS.unidadFinal() == UnidadTemperatura.CELSIUS
                : "Unidad final incorrecta";

            System.out.println("testFahrenheitACelsius PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testFahrenheitACelsius FALLÓ: " + e.getMessage());
        }
    }

    private static void testKelvinAFahrenheitEncadenado() {
        testCount++;
        try {
            double valor = 300.0;
            double resultado = ConversorTemperatura.KELVIN_FAHRENHEIT.convertir(valor);

            assert Math.abs(resultado - 80.33) < 0.1 : "Conversión Kelvin a Fahrenheit encadenada incorrecta";

            System.out.println("testKelvinAFahrenheitEncadenado PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testKelvinAFahrenheitEncadenado FALLÓ: " + e.getMessage());
        }
    }
}