package modelo.catalogo;

/** Servicio de restaurante: cobra el precio base por cada platillo. */
// HERENCIA: ServicioRestaurante extiende de Servicio
public class ServicioRestaurante extends Servicio {

    /**
     * Crea un servicio de restaurante.
     *
     * @param codigo     código único del servicio
     * @param nombre     nombre del servicio
     * @param precioBase precio por platillo
     */
    public ServicioRestaurante(String codigo, String nombre, double precioBase) {
        super(codigo, nombre, precioBase, CategoriaServicio.RESTAURANTE);
    }

    /** {@inheritDoc} Fórmula: {@code precioBase × platillos} (RN-19). */
    // POLIMORFISMO: sobreescritura de calcularCosto()
    @Override
    public double calcularCosto(int platillos) {
        validarCantidad(platillos);
        return precioBase * platillos;
    }
}