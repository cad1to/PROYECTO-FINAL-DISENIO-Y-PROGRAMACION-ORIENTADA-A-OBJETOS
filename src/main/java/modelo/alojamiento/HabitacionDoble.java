package modelo.alojamiento;

/** Habitación doble: suma un cargo por noche por cada cama adicional a la primera. */
// HERENCIA: HabitacionDoble hereda de Habitacion
public class HabitacionDoble extends Habitacion {
    // CONSTANTE: cargo por noche por cada cama adicional a la primera (RN-17)
    public static final double CARGO_CAMA_EXTRA = 300.00;

    protected int numCamas;

    /**
     * Crea una habitación doble.
     *
     * @param numero     número de habitación
     * @param piso       piso donde se ubica
     * @param capacidad  capacidad máxima de huéspedes
     * @param tarifaBase tarifa base por noche
     * @param numCamas   número de camas, positivo
     * @throws IllegalArgumentException si el número de camas no es positivo
     */
    public HabitacionDoble(int numero, int piso, int capacidad, double tarifaBase, int numCamas) {
        super(numero, piso, capacidad, tarifaBase);
        validarPositivo(numCamas, "El número de camas");
        this.numCamas = numCamas;
    }

    /** {@inheritDoc} Fórmula: {@code (tarifaBase + CARGO_CAMA_EXTRA × (numCamas - 1)) × noches} (RN-17). */
    // POLIMORFISMO: sobrescritura de calcularTarifa(), suma el cargo de camas extra
    @Override
    public double calcularTarifa(int noches) {
        return (tarifaBase + CARGO_CAMA_EXTRA * (numCamas - 1)) * noches;
    }

    /** @return número de camas */
    public int getNumCamas() {
        return numCamas;
    }
}