package trayectos;

public class TramoTren extends TramoTrayecto {
	private Linea linea;
	private int numParadas;

	public TramoTren(String origen, String destino, Linea linea, int numParadas) {
		super(origen, destino);
		this.linea = linea;
		this.numParadas = numParadas;
	}

    public Linea getLinea() {
        return this.linea;
    } 

    public int getNumParadas() {
        return this.numParadas;
    }

	@Override
	public double tiempo() {

		double t = 0;

		switch (linea) {
			case C1:

				t = this.numParadas * 5;

				break;
		
			case C4:

				t = this.numParadas * 10;

				break;

			case C5:

				t = this.numParadas * 30;
				
				break;

			default:

				break;
		}

		return t;
	}
    
	@Override
	public String toString() {
		return "En tren de la linea " + this.linea + " " + super.toString();
	}
}
