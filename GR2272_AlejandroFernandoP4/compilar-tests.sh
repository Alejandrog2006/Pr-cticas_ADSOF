#!/bin/bash

set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR"

BIN_DIR="bin"
SRC_DIR="src"
TEST_DIR="test"

echo "=== Limpiando binarios anteriores ==="
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

echo "=== Compilando código fuente ==="
find "$SRC_DIR" -name '*.java' -print0 | xargs -0 javac -d "$BIN_DIR"
echo "Código fuente compilado"
echo

echo "=== Compilando tests ==="
find "$TEST_DIR" -name '*.java' -print0 | xargs -0 javac -cp "$BIN_DIR" -d "$BIN_DIR"
echo "Tests compilados"
echo

echo "=== Ejecutando tests ==="
mapfile -t TEST_CLASSES < <(find "$TEST_DIR" -name '*Test.java' | sort | sed -E 's#^test/##; s#/#.#g; s#\.java$##')

for test_class in "${TEST_CLASSES[@]}"; do
    java -ea -cp "$BIN_DIR" "$test_class"
    echo
done

echo "=== Todos los tests finalizados ==="
