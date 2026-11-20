package modelo.alojamiento;

/** Suite: suma un cargo fijo por noche, propio de cada suite, a la tarifa base. */
// HERENCIA: Suite extiende de Habitacion
public class Suite extends Habitacion {
    protected double cargoExtra;

    /**
     * Crea una suite.
     *
     * @param numero     número de habitación
     * @param piso       piso donde se ubica
     * @param capacidad  capacidad máxima de huéspedes
     * @param tarifaBase tarifa base por noche
     * @param cargoExtra cargo fijo por noche de esta suite, positivo
     * @throws IllegalArgumentException si el cargo extra no es positivo
     */
    public Suite(int numero, int piso, int capacidad, double tarifaBase, double cargoExtra) {
        super(numero, piso, capacidad, tarifaBase);
        validarPositivo(cargoExtra, "El cargo extra");
        this.cargoExtra = cargoExtra;
    }

    /** {@inheritDoc} Fórmula: {@code (tarifaBase + cargoExtra) × noches} (RN-18). */
    // POLIMORFISMO: sobreescritura de calcularTarifa(), suma el cargo extra de la suite
    @Override
    public double calcularTarifa(int noches) {
        return (tarifaBase + cargoExtra) * noches;
    }

    /** @return cargo extra por noche */
    public double getCargoExtra() {
        return cargoExtra;
    }
}