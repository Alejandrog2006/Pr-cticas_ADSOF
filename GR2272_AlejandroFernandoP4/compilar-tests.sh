#!/bin/bash

# Script para compilar y ejecutar tests manualmente

echo "=== Compilando código fuente ==="
javac -d bin src/estacion/*.java src/estacion/**/*.java 2>/dev/null

if [ $? -ne 0 ]; then
    echo "Error compilando código fuente"
    exit 1
fi

echo "✓ Código fuente compilado exitosamente\n"

echo "=== Compilando tests ==="
javac -cp bin -d bin $(find test -name '*.java' -type f) 2>/dev/null

if [ $? -ne 0 ]; then
    echo "Error compilando tests"
    exit 1
fi

echo "✓ Tests compilados exitosamente\n"

echo "=== Ejecutando tests ===\n"
cd bin
java -ea -cp . estacion.sensor.SensorTest
echo ""
java -ea -cp . estacion.sensor.SensorEstrategiasTest
echo ""
java -ea -cp . estacion.EstacionMeteoTest

echo ""
echo "=== Tests completados ==="
