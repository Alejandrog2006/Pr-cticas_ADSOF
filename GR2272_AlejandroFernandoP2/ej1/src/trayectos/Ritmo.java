package trayectos;

/**
 * Enumeración que define los diferentes ritmos o velocidades de marcha a pie.
 * Cada ritmo tiene una velocidad asociada medida en minutos por kilómetro.
 * Los ritmos disponibles son:
 *   SUAVE - 15 minutos por kilómetro (caminar lentamente)
 *   MODERADO - 10 minutos por kilómetro (ritmo normal)
 *   RAPIDO - 8 minutos por kilómetro (caminar rápidamente)
 */
public enum Ritmo {
    SUAVE(15),
    MODERADO(10),
    RAPIDO(8);
    
    private int velocidad;

    /**
     * Constructor privado de la enumeración Ritmo.
     * Inicializa un ritmo de marcha con su velocidad correspondiente.
     *
     * @param velocidad la velocidad en minutos por kilómetro
     */
    private Ritmo(int velocidad){
        this.velocidad = velocidad;
    }

    /**
     * Obtiene la velocidad del ritmo de marcha.
     *
     * @return la velocidad en minutos por kilómetro
     */
    public int getVelocidad() {
        return this.velocidad;
    }

    /**
     * Devuelve una representación en texto del ritmo.
     *
     * @return el nombre del ritmo (SUAVE, MODERADO o RAPIDO)
     */
	public String toString() {
		return this.name();
	}

}
