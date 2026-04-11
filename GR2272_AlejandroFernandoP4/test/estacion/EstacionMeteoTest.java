package estacion;

import java.time.*;
import java.lang.reflect.Field;

import estacion.conversor.ConversorPresion;
import estacion.conversor.ConversorTemperatura;
import estacion.procesador.ProcesadorDatos;
import estacion.sensor.estrategia.Estrategia;
import estacion.sensor.estrategia.EstrategiaAleatoria;

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
        testProcesadorPorDefectoIdentidad();
        testConfiguraConversorYCalculaEstadisticas();
        testConfiguraConversorTemperaturaAFahrenheit();
        testConfiguraConversorPresionAPa();
        testConfiguraConversorIncompatibleLanzaExcepcion();
        
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

    private static class EstacionPrueba extends EstacionMeteo {
        EstacionPrueba() {
            super("Estacion de prueba", 40.0, -3.0);
        }
    }

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
