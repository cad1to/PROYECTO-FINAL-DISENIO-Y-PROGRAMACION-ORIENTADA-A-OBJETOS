package modelo.catalogo;

/** Servicio de lavandería: cobra por prenda y aplica un recargo si es exprés. */
// HERENCIA: ServicioLavanderia hereda de Servicio
public class ServicioLavanderia extends Servicio {
    // CONSTANTE: recargo porcentual del servicio exprés (RN-21)
    public static final double RECARGO_EXPRES = 0.25;

    protected boolean expres;

    /**
     * Crea un servicio de lavandería.
     *
     * @param codigo     código único del servicio
     * @param nombre     nombre del servicio
     * @param precioBase precio por prenda
     * @param expres     {@code true} si el servicio es exprés
     */
    public ServicioLavanderia(String codigo, String nombre, double precioBase, boolean expres) {
        super(codigo, nombre, precioBase, CategoriaServicio.LAVANDERIA);
        this.expres = expres;
    }

    /**
     * {@inheritDoc} Fórmula: {@code precioBase × prendas}; si es exprés, ese
     * monto se multiplica por {@code (1 + RECARGO_EXPRES)} (RN-21).
     */
    // POLIMORFISMO: sobrescritura de calcularCosto(), con recargo si es exprés
    @Override
    public double calcularCosto(int prendas) {
        validarCantidad(prendas);
        double subtotal = precioBase * prendas;
        return expres ? subtotal * (1 + RECARGO_EXPRES) : subtotal;
    }

    /** @return {@code true} si el servicio es exprés */
    public boolean isExpres() {
        return expres;
    }
}