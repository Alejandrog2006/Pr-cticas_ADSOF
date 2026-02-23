package trayectos;

public class TramoAPie extends TramoTrayecto {
    private double numKm;
    private Ritmo ritmo;

    public TramoAPie(String origen, String destino, double numKm) {
        this(origen, destino, numKm, Ritmo.MODERADO);
    }

    public TramoAPie(String origen, String destino, double numKm, Ritmo ritmo) {
        super(origen, destino);
        this.numKm = numKm;
        this.ritmo = ritmo == null ? Ritmo.MODERADO : ritmo;
    }

    public double getNumKm() {
        return this.numKm;
    }

    public Ritmo getRitmo() {
        return this.ritmo;
    }

    @Override
    public double tiempo() {
        return this.numKm * this.ritmo.getVelocidad();
    }

    @Override
    public String toString() {
        return "A pie " + super.toString() + " (ritmo " + this.ritmo + ") ";
    }
}