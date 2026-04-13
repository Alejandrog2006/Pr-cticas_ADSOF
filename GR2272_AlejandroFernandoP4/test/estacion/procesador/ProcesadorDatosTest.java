package estacion.procesador;

import java.time.Duration;
import java.time.LocalDateTime;

import estacion.conversor.ConversorTemperatura;
import estacion.sensor.Sensor;
import estacion.sensor.estrategia.EstrategiaAleatoria;
import estacion.unidadLectura.UnidadTemperatura;

/**
 * Pruebas unitarias del procesador de datos.
 */
public class ProcesadorDatosTest {
    private static int testCount = 0;
    private static int passCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Ejecutando ProcesadorDatosTest ===\n");

        testProcesadorSinConversor();
        testProcesadorConConversorAKelvin();
        testProcesadorCalculaEstadisticas();

        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    private static void testProcesadorSinConversor() {
        testCount++;
        try {
            SensorStub sensor = new SensorStub("TEMP-0001", UnidadTemperatura.CELSIUS, 20.5);
            sensor.calibrar();

            ProcesadorDatos procesador = new ProcesadorDatos(sensor);
            procesador.almacenarLectura(LocalDateTime.now(), sensor.medir());

            assert procesador.getSensor() == sensor : "Debe conservar el sensor asociado";
            assert procesador.getHistorico().size() == 1 : "Debe almacenar una lectura";
            double valor = procesador.getHistorico().values().iterator().next();
            assert Math.abs(valor - 20.5) < 0.01 : "El conversor identidad no debe alterar el valor";
            assert procesador.getUnidadSalida() == UnidadTemperatura.CELSIUS : "La unidad de salida debe ser Celsius";

            System.out.println("testProcesadorSinConversor PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testProcesadorSinConversor FALLÓ: " + e.getMessage());
        }
    }

    private static void testProcesadorConConversorAKelvin() {
        testCount++;
        try {
            SensorStub sensor = new SensorStub("TEMP-0002", UnidadTemperatura.CELSIUS, 20.5);
            sensor.calibrar();

            ProcesadorDatos procesador = new ProcesadorDatos(sensor, ConversorTemperatura.CELSIUS_KELVIN);
            procesador.almacenarLectura(LocalDateTime.now(), sensor.medir());

            double valor = procesador.getHistorico().values().iterator().next();
            assert Math.abs(valor - 293.65) < 0.01 : "Debe convertir Celsius a Kelvin";
            assert procesador.getUnidadSalida() == UnidadTemperatura.KELVIN : "Debe mostrar la unidad de destino";

            System.out.println("testProcesadorConConversorAKelvin PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testProcesadorConConversorAKelvin FALLÓ: " + e.getMessage());
        }
    }

    private static void testProcesadorCalculaEstadisticas() {
        testCount++;
        try {
            SensorStub sensor = new SensorStub(
                "TEMP-0003",
                UnidadTemperatura.CELSIUS,
                20.0, 22.0, 18.5
            );
            sensor.calibrar();

            ProcesadorDatos procesador = new ProcesadorDatos(sensor);
            procesador.almacenarLectura(LocalDateTime.now(), sensor.medir());
            procesador.almacenarLectura(LocalDateTime.now().plusSeconds(1), sensor.medir());
            procesador.almacenarLectura(LocalDateTime.now().plusSeconds(2), sensor.medir());

            assert Math.abs(procesador.minimo() - 18.5) < 0.01 : "MIN debe ser 18.5";
            assert Math.abs(procesador.maximo() - 22.0) < 0.01 : "MAX debe ser 22.0";
            assert Math.abs(procesador.media() - 20.1666667) < 0.01 : "AVG debe aproximarse a 20.17";

            System.out.println("testProcesadorCalculaEstadisticas PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testProcesadorCalculaEstadisticas FALLÓ: " + e.getMessage());
        }
    }

    private static class SensorStub extends Sensor {
        private final double[] lecturas;
        private int indice = 0;

        SensorStub(String id, UnidadTemperatura unidad, double... lecturas) {
            super(0.0, new EstrategiaAleatoria(), Duration.ofDays(365));
            this.id = id;
            this.unidadLectura = unidad;
            this.lecturas = lecturas;
        }

        @Override
        public double medir() {
            if (lecturas.length == 0) {
                return 0.0;
            }
            double valor = lecturas[indice % lecturas.length];
            indice++;
            return valor;
        }
    }
}
