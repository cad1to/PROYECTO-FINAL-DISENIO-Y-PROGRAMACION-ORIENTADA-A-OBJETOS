package modelo.alojamiento;

/** Estados posibles de una habitación (RN-12). */
public enum EstadoHabitacion {
    /** Lista para recibir huéspedes. */
    DISPONIBLE,
    /** Con huéspedes alojados; solo la asigna el check-in y solo la libera el check-out (RN-42). */
    OCUPADA,
    /** Pendiente de limpieza después del check-out. */
    LIMPIEZA,
    /** Fuera de servicio; no se ofrece ni se reserva (RN-14). */
    MANTENIMIENTO
}