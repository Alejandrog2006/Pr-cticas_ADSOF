package estacion.alerta;

import java.time.LocalDateTime;

/**
 * Pruebas unitarias de la clase Alerta.
 */
public class AlertaTest {
    private static int testCount = 0;
    private static int passCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Ejecutando AlertaTest ===\n");

        testAlertaGetters();
        testAlertaToString();

        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    private static void testAlertaGetters() {
        testCount++;
        try {
            LocalDateTime fecha = LocalDateTime.of(2026, 1, 12, 8, 3);
            Alerta alerta = new Alerta(
                "TEMP-0002",
                TipoAlerta.CAMBIO_BRUSCO,
                "Cambio brusco en TEMP-0002: 27.0°C (anterior: 10.8°C)",
                fecha
            );

            assert "TEMP-0002".equals(alerta.getSensorId()) : "sensorId incorrecto";
            assert TipoAlerta.CAMBIO_BRUSCO == alerta.getTipo() : "tipo incorrecto";
            assert "Cambio brusco en TEMP-0002: 27.0°C (anterior: 10.8°C)".equals(alerta.getMensaje()) : "mensaje incorrecto";
            assert fecha.equals(alerta.getFecha()) : "fecha incorrecta";

            System.out.println("testAlertaGetters PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testAlertaGetters FALLÓ: " + e.getMessage());
        }
    }

    private static void testAlertaToString() {
        testCount++;
        try {
            LocalDateTime fecha = LocalDateTime.of(2026, 1, 12, 8, 3);
            Alerta alerta = new Alerta(
                "TEMP-0002",
                TipoAlerta.CAMBIO_BRUSCO,
                "Cambio brusco en TEMP-0002: 27.0°C (anterior: 10.8°C)",
                fecha
            );

            String esperado = "[2026-01-12T08:03] Cambio brusco en TEMP-0002: 27.0°C (anterior: 10.8°C)";
            assert esperado.equals(alerta.toString()) : "toString incorrecto";

            System.out.println("testAlertaToString PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("testAlertaToString FALLÓ: " + e.getMessage());
        }
    }
}