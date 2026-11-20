package modelo.personas;

/**
 * Tipo de huésped. Determina si recibe el descuento VIP en su factura (RN-26).
 */
public enum TipoHuesped {
    /** Huésped sin descuento. */
    REGULAR,
    /** Huésped que recibe {@link Huesped#DESCUENTO_VIP} en su factura. */
    VIP
}
