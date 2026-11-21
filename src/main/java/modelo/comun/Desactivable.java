package modelo.comun;

/**
 * Contrato de baja lógica del hotel (RN-34): los elementos no se eliminan, se
 * marcan como inactivos para conservar el historial de reservaciones, facturas
 * y nómina. Permite a {@code Hotel} reutilizar el mismo código para listar
 * activos y dar de baja personas, habitaciones y servicios.
 */
// INTERFAZ: Desactivable la implementan Persona, Habitacion y Servicio
public interface Desactivable {

    /**
     * Indica si el elemento sigue activo.
     *
     * @return {@code true} si no ha sido dado de baja
     */
    boolean isActivo();

    /** Da de baja lógica el elemento ({@code activo = false}); no lo elimina. */
    void darDeBaja();
}