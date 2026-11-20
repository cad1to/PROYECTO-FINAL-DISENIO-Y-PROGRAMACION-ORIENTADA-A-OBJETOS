package modelo.catalogo;

/** Servicio de spa: cobra un precio fijo por sesión, sin importar su duración. */
// HERENCIA: ServicioSpa extiende de Servicio
public class ServicioSpa extends Servicio {

    /**
     * Crea un servicio de spa.
     *
     * @param codigo     código único del servicio
     * @param nombre     nombre del servicio
     * @param precioBase precio por sesión
     */
    public ServicioSpa(String codigo, String nombre, double precioBase) {
        super(codigo, nombre, precioBase, CategoriaServicio.SPA);
    }

    /** {@inheritDoc} Fórmula: {@code precioBase × sesiones} (RN-20). */
    // POLIMORFISMO: sobreescritura de calcularCosto()
    @Override
    public double calcularCosto(int sesiones) {
        validarCantidad(sesiones);
        return precioBase * sesiones;
    }
}