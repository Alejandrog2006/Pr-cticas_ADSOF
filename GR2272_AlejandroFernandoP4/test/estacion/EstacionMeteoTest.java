/*
 * Pruebas de la estación meteorológica, sus sensores, procesadores y alertas.
 * Hecho por Alejandro González y Fernando Blanco.
 */
package estacion;

import java.time.*;
import java.lang.reflect.Field;

import estacion.alerta.Alerta;
import estacion.alerta.TipoAlerta;
import estacion.conversor.ConversorPresion;
import estacion.conversor.ConversorTemperatura;
import estacion.procesador.ProcesadorDatos;
import estacion.sensor.estrategia.Estrategia;
import estacion.sensor.estrategia.EstrategiaAleatoria;

/**
 * Pruebas funcionales de la estación meteorológica.
 */
public class EstacionMeteoTest {
    private static int testCount = 0;
    private static int passCount = 0;

    /**
     * Punto de entrada de las pruebas.
     *
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        System.out.println("=== Ejecutando EstacionMeteoTest ===\n");
        
        testAgregaYRecuperaSensores();
        testAgregaSensorDuplicadoLanzaExcepcion();
        testAgregaSensorPorTipo();
        testAgregaSensorPorTipoInvalido();
        testLeerDatos();
        testLecturaPeriodica();
        testProcesadorPorDefectoIdentidad();
        testConfiguraConversorYCalculaEstadisticas();
        testConfiguraConversorTemperaturaAFahrenheit();
        testConfiguraConversorPresionAPa();
        testConfiguraConversorIncompatibleLanzaExcepcion();
        testSensorSinCalibrarGeneraAlertaYSeDetiene();
        testCalibrarSensorLimpiaAlertasYReanuda();
        testCambioBruscoGeneraAlertaPeroNoDetiene();
        
        System.out.printf("\n=== Resultados: %d/%d pasaron ===\n", passCount, testCount);
    }

    /**
     * Comprueba que se agregan y recuperan sensores correctamente.
     */
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

    /**
     * Comprueba que agregar un sensor duplicado lanza excepción.
     */
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

    /**
     * Comprueba que agregar sensores por tipo funciona.
     */
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

    /**
     * Comprueba que agregar un sensor por tipo inválido lanza excepción.
     */
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

    /**
     * Comprueba que la estación lee datos correctamente.
     */
    private static void testLeerDatos() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            estacion.sensor.SensorHumedad.numSensores = 0;

            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(2.0);
            estacion.sensor.SensorHumedad hum = new estacion.sensor.SensorHumedad(5.0);
            setEstrategia(temp, new EstrategiaAleatoria(0.0));
            setEstrategia(hum, new EstrategiaAleatoria(0.0));
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

    /**
     * Comprueba la lectura periódica.
     */
    private static void testLecturaPeriodica() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);
            setEstrategia(temp, new EstrategiaAleatoria(0.0));
            temp.calibrar();
            
            assert estacion.agregarSensor(temp) : "No agregó sensor";
            assert estacion.lecturaPeriodica(Duration.ZERO, 2) : "No hizo lectura periódica";
            
            System.out.println("✓ testLecturaPeriodica PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testLecturaPeriodica FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que el procesador por defecto usa conversor identidad.
     */
    private static void testProcesadorPorDefectoIdentidad() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);
            setEstrategia(temp, new EstrategiaAleatoria(0.0));
            temp.calibrar();

            assert estacion.agregarSensor(temp) : "No agregó sensor";
            assert estacion.leerDatos() : "No leyó datos";

            ProcesadorDatos procesador = estacion.getProcesador(temp.getId());
            assert procesador != null : "No se creó el procesador por defecto";
            assert procesador.getHistorico().size() == 1 : "El procesador no almacenó la lectura";

            double almacenado = procesador.getHistorico().values().iterator().next();
            assert Math.abs(almacenado - temp.getUltimaLectura()) < 0.00001 : "El conversor por defecto no debería alterar la lectura";

            System.out.println("✓ testProcesadorPorDefectoIdentidad PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testProcesadorPorDefectoIdentidad FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba configuración de conversor y cálculo de estadísticas.
     */
    private static void testConfiguraConversorYCalculaEstadisticas() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);

            setEstrategia(temp, new Estrategia() {
                private int indice = 0;
                private final double[] valores = {20.0, 21.0, 22.0};

                @Override
                public double generarValor(double min, double max) {
                    return valores[indice++];
                }
            });

            temp.calibrar();
            assert estacion.agregarSensor(temp) : "No agregó sensor";
            estacion.configurarConversor(temp.getId(), ConversorTemperatura.CELSIUS_KELVIN);

            assert estacion.leerDatos() : "No realizó lectura 1";
            assert estacion.leerDatos() : "No realizó lectura 2";
            assert estacion.leerDatos() : "No realizó lectura 3";

            ProcesadorDatos procesador = estacion.getProcesador(temp.getId());
            assert procesador.getHistorico().size() == 3 : "Histórico procesado incompleto";

            assert Math.abs(procesador.minimo() - 293.15) < 0.00001 : "Mínimo incorrecto";
            assert Math.abs(procesador.maximo() - 295.15) < 0.00001 : "Máximo incorrecto";
            assert Math.abs(procesador.media() - 294.15) < 0.00001 : "Media incorrecta";

            System.out.println("✓ testConfiguraConversorYCalculaEstadisticas PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testConfiguraConversorYCalculaEstadisticas FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba conversión de temperatura a Fahrenheit.
     */
    private static void testConfiguraConversorTemperaturaAFahrenheit() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);

            setEstrategia(temp, new Estrategia() {
                private int indice = 0;
                private final double[] valores = {0.0, 100.0};

                @Override
                public double generarValor(double min, double max) {
                    return valores[indice++];
                }
            });

            temp.calibrar();
            assert estacion.agregarSensor(temp) : "No agregó sensor";
            estacion.configurarConversor(temp.getId(), ConversorTemperatura.CELSIUS_FAHRENHEIT);

            assert estacion.leerDatos() : "No realizó lectura 1";
            assert estacion.leerDatos() : "No realizó lectura 2";

            ProcesadorDatos procesador = estacion.getProcesador(temp.getId());
            assert Math.abs(procesador.minimo() - 32.0) < 0.00001 : "Conversión a Fahrenheit incorrecta (min)";
            assert Math.abs(procesador.maximo() - 212.0) < 0.00001 : "Conversión a Fahrenheit incorrecta (max)";

            System.out.println("✓ testConfiguraConversorTemperaturaAFahrenheit PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testConfiguraConversorTemperaturaAFahrenheit FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba conversión de presión a Pa.
     */
    private static void testConfiguraConversorPresionAPa() {
        testCount++;
        try {
            estacion.sensor.SensorPresion.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorPresion pres = new estacion.sensor.SensorPresion(0.0);

            setEstrategia(pres, new Estrategia() {
                private int indice = 0;
                private final double[] valores = {300.0, 301.5};

                @Override
                public double generarValor(double min, double max) {
                    return valores[indice++];
                }
            });

            pres.calibrar();
            assert estacion.agregarSensor(pres) : "No agregó sensor";
            estacion.configurarConversor(pres.getId(), ConversorPresion.HPA_PA);

            assert estacion.leerDatos() : "No realizó lectura 1";
            assert estacion.leerDatos() : "No realizó lectura 2";

            ProcesadorDatos procesador = estacion.getProcesador(pres.getId());
            assert Math.abs(procesador.minimo() - 30000.0) < 0.00001 : "Conversión hPa->Pa incorrecta (min)";
            assert Math.abs(procesador.maximo() - 30150.0) < 0.00001 : "Conversión hPa->Pa incorrecta (max)";

            System.out.println("✓ testConfiguraConversorPresionAPa PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testConfiguraConversorPresionAPa FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que un conversor incompatible lanza excepción.
     */
    private static void testConfiguraConversorIncompatibleLanzaExcepcion() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);
            setEstrategia(temp, new EstrategiaAleatoria(0.0));
            temp.calibrar();

            assert estacion.agregarSensor(temp) : "No agregó sensor";

            try {
                estacion.configurarConversor(temp.getId(), ConversorPresion.HPA_PA);
                System.out.println("✗ testConfiguraConversorIncompatibleLanzaExcepcion FALLÓ: No lanzó excepción");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ testConfiguraConversorIncompatibleLanzaExcepcion PASÓ");
                passCount++;
            }
        } catch (AssertionError e) {
            System.out.println("✗ testConfiguraConversorIncompatibleLanzaExcepcion FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que un sensor sin calibrar genera alerta y se detiene.
     */
    private static void testSensorSinCalibrarGeneraAlertaYSeDetiene() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);
            setEstrategia(temp, new EstrategiaAleatoria(0.0));

            assert estacion.agregarSensor(temp) : "No agregó sensor";
            assert estacion.leerDatos() : "No leyó datos";

            assert estacion.getAlertas().size() == 1 : "Debe registrar alerta por sensor sin calibrar";
            Alerta alerta = estacion.getAlertas().get(0);
            assert alerta.getTipo() == TipoAlerta.SENSOR_NO_CALIBRADO : "Tipo de alerta incorrecto";
            assert estacion.estaDetenido(temp.getId()) : "El sensor debería quedar detenido";

            System.out.println("✓ testSensorSinCalibrarGeneraAlertaYSeDetiene PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testSensorSinCalibrarGeneraAlertaYSeDetiene FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que calibrar limpia alertas y reanuda la lectura.
     */
    private static void testCalibrarSensorLimpiaAlertasYReanuda() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);
            setEstrategia(temp, new EstrategiaAleatoria(0.0));

            assert estacion.agregarSensor(temp) : "No agregó sensor";
            assert estacion.leerDatos() : "No leyó datos";

            assert estacion.estaDetenido(temp.getId()) : "El sensor debería quedar detenido";
            assert estacion.getAlertas().size() == 1 : "Debe haber una alerta previa";

            estacion.calibrarSensor(temp.getId(), 0.0);
            assert !estacion.estaDetenido(temp.getId()) : "El sensor debería reanudarse tras calibrar";
            assert estacion.getAlertas().isEmpty() : "La calibración debería limpiar alertas del sensor";

            assert estacion.leerDatos() : "No retomó lectura tras calibrar";
            assert estacion.getProcesador(temp.getId()).getHistorico().size() == 1 : "No se almacenó lectura tras reanudar";

            System.out.println("✓ testCalibrarSensorLimpiaAlertasYReanuda PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testCalibrarSensorLimpiaAlertasYReanuda FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Comprueba que un cambio brusco genera alerta pero no detiene la lectura.
     */
    private static void testCambioBruscoGeneraAlertaPeroNoDetiene() {
        testCount++;
        try {
            estacion.sensor.SensorTemperatura.numSensores = 0;
            EstacionPrueba estacion = new EstacionPrueba();
            estacion.sensor.SensorTemperatura temp = new estacion.sensor.SensorTemperatura(0.0);
            setEstrategia(temp, new Estrategia() {
                private int indice = 0;
                private final double[] valores = {20.0, 40.0, 41.0};

                @Override
                public double generarValor(double min, double max) {
                    return valores[indice++];
                }
            });

            temp.calibrar();
            estacion.setUmbralCambioBruscoPct(50.0);
            assert estacion.agregarSensor(temp) : "No agregó sensor";

            assert estacion.leerDatos() : "No realizó lectura 1";
            assert estacion.leerDatos() : "No realizó lectura 2";
            assert estacion.leerDatos() : "No realizó lectura 3";

            boolean hayAlertaCambioBrusco = false;
            for (Alerta alerta : estacion.getAlertas()) {
                if (alerta.getTipo() == TipoAlerta.CAMBIO_BRUSCO) {
                    hayAlertaCambioBrusco = true;
                    break;
                }
            }

            assert hayAlertaCambioBrusco : "Debe registrar alerta por cambio brusco";
            assert !estacion.estaDetenido(temp.getId()) : "No debería detenerse por cambio brusco";
            assert estacion.getProcesador(temp.getId()).getHistorico().size() == 3 : "Debe seguir almacenando lecturas";

            System.out.println("✓ testCambioBruscoGeneraAlertaPeroNoDetiene PASÓ");
            passCount++;
        } catch (AssertionError e) {
            System.out.println("✗ testCambioBruscoGeneraAlertaPeroNoDetiene FALLÓ: " + e.getMessage());
        }
    }

    /**
     * Clase auxiliar de estación para las pruebas.
     */
    private static class EstacionPrueba extends EstacionMeteo {
        /**
         * Crea una estación de prueba.
         */
        EstacionPrueba() {
            super("Estacion de prueba", 40.0, -3.0);
        }
    }

    /**
     * Sustituye la estrategia interna de un sensor usando reflexión.
     *
     * @param sensor sensor a modificar.
     * @param estrategia estrategia a inyectar.
     */
    private static void setEstrategia(estacion.sensor.Sensor sensor, Object estrategia) {
        try {
            Field field = estacion.sensor.Sensor.class.getDeclaredField("estrategia");
            field.setAccessible(true);
            field.set(sensor, estrategia);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo configurar estrategia en test", e);
        }
    }
}
