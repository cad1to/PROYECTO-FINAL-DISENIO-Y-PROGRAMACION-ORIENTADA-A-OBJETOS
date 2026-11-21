package modelo.reservas;

/**
 * Estados por los que pasa una reservación.
 */
public enum EstadoReservacion {
    /** Confirmada, sin check-in. */
    ACTIVA,
    /** Con check-in realizado. */
    EN_CURSO,
    /** Con check-out realizado. */
    FINALIZADA,
    /** Cancelada antes del check-in (RN-08, RN-37). */
    CANCELADA
}
