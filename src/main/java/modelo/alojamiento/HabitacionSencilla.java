package modelo.alojamiento;

/** Habitación sencilla: cobra solo la tarifa base por noche. */
// HERENCIA: HabitacionSencilla extiende de Habitacion
public class HabitacionSencilla extends Habitacion {

    /**
     * Crea una habitación sencilla.
     *
     * @param numero     número de habitación
     * @param piso       piso donde se ubica
     * @param capacidad  capacidad máxima de huéspedes
     * @param tarifaBase tarifa base por noche
     */
    public HabitacionSencilla(int numero, int piso, int capacidad, double tarifaBase) {
        super(numero, piso, capacidad, tarifaBase);
    }

    /** {@inheritDoc} Fórmula: {@code tarifaBase × noches} (RN-16). */
    // POLIMORFISMO: sobreescritura de calcularTarifa()
    @Override
    public double calcularTarifa(int noches) {
        return tarifaBase * noches;
    }
}