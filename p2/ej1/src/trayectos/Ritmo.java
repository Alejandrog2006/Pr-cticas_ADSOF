package trayectos;

public enum Ritmo {
    SUAVE(15),
    MODERADO(10),
    RAPIDO(8);
    
    private int velocidad;

    private Ritmo(int velocidad){
        this.velocidad = velocidad;
    }

    public int getVelocidad() {
        return this.velocidad;
    }

	public String toString() {
		return this.name();
	}

}
