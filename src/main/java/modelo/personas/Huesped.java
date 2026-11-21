package modelo.personas;

/**
 * Huésped del hotel. Puede ser {@link TipoHuesped#REGULAR} o {@link TipoHuesped#VIP};
 * los VIP reciben un descuento en su factura (RN-26).
 */
public class Huesped extends Persona {
    /** Porcentaje de descuento que recibe un huésped VIP en su factura (10 %, RN-26). */
    public static final double DESCUENTO_VIP = 0.10;

    /** Tipo de huésped; es el único dato actualizable (RN-36). */
    protected TipoHuesped tipo;

    /**
     * Crea un huésped activo.
     *
     * @param id       identificador único del huésped
     * @param nombre   nombre(s)
     * @param apellido apellido(s)
     * @param telefono teléfono de contacto
     * @param correo   correo electrónico
     * @param tipo     tipo de huésped; no puede ser nulo
     * @throws IllegalArgumentException si algún dato es inválido (RN-53)
     */
    public Huesped(String id, String nombre, String apellido, String telefono, String correo,
                   TipoHuesped tipo) {
        super(id, nombre, apellido, telefono, correo);
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de huésped no puede ser nulo.");
        }
        this.tipo = tipo;
    }

    /**
     * Calcula el porcentaje de descuento que corresponde al huésped según su tipo (RN-26).
     *
     * @return {@link #DESCUENTO_VIP} si es VIP, o {@code 0} si es REGULAR
     */
    public double calcularDescuento() {
        return tipo == TipoHuesped.VIP ? DESCUENTO_VIP : 0.0;
    }

    /**
     * @return descripción con tipo, id, nombre completo y estado del huésped
     */
    @Override
    public String descripcion() {
        return String.format("Huésped %s | ID: %s | %s | Tel: %s | Correo: %s%s",
                tipo, id, getNombreCompleto(), telefono, correo, activo ? "" : " | (BAJA)");
    }

    /** @return tipo de huésped */
    public TipoHuesped getTipo() {
        return tipo;
    }

    /**
     * Actualiza el tipo de huésped (RN-36).
     *
     * @param tipo nuevo tipo; no puede ser nulo
     * @throws IllegalArgumentException si el tipo es nulo
     */
    public void setTipo(TipoHuesped tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de huésped no puede ser nulo.");
        }
        this.tipo = tipo;
    }
}
