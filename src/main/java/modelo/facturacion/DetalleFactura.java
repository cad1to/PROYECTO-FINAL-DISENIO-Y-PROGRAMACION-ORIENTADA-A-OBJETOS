package modelo.facturacion;

/**
 * Línea de una factura. Guarda el concepto, la cantidad y el monto tal como
 * se calcularon al generar la factura, por lo que cambios posteriores de
 * tarifas o precios no la afectan (RN-45).
 */
public class DetalleFactura {
    // ENCAPSULACIÓN: atributos protected, solo lectura mediante getters
    protected String concepto;
    protected int cantidad;
    protected double monto;

    /**
     * Crea una línea de factura. Solo {@link Factura} la crea, por eso el
     * constructor es visible únicamente dentro del paquete.
     *
     * @param concepto descripción del concepto, no vacía
     * @param cantidad noches, platillos, sesiones o prendas; positiva
     * @param monto    monto calculado, positivo
     * @throws IllegalArgumentException si algún dato es inválido
     */
    DetalleFactura(String concepto, int cantidad, double monto) {
        if (concepto == null || concepto.isBlank()) {
            throw new IllegalArgumentException("El concepto no puede estar vacío.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo.");
        }
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.monto = monto;
    }

    /** @return descripción del concepto facturado */
    public String getConcepto() {
        return concepto;
    }

    /** @return cantidad facturada */
    public int getCantidad() {
        return cantidad;
    }

    /** @return monto de la línea en pesos */
    public double getMonto() {
        return monto;
    }
}