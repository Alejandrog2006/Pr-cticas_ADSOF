package estacion.conversor;

import estacion.unidadLectura.UnidadLectura;

public class ConversorIdentidad implements Conversor {
    private final UnidadLectura unidad;

    public ConversorIdentidad(UnidadLectura unidad) {
        this.unidad = unidad;
    }

    @Override
    public double convertir(double valor) {
        return valor;
    }

    @Override
    public UnidadLectura unidadInicial() {
        return unidad;
    }

    @Override
    public UnidadLectura unidadFinal() {
        return unidad;
    }
}
