package modelo.facturacion;

/**
 * Concepto que puede cobrarse en una factura (RN-25). Permite a {@link Factura}
 * tratar igual a habitaciones y servicios sin conocer su tipo concreto.
 */
// INTERFAZ: Facturable la implementan Habitacion y Servicio
public interface Facturable {

    /**
     * Descripción del concepto tal como aparece en el detalle de la factura.
     *
     * @return texto del concepto
     */
    String getConcepto();

    /**
     * Calcula el costo del concepto.
     *
     * @param cantidad noches (habitación) o platillos, sesiones o prendas (servicio)
     * @return costo total en pesos
     */
    double calcularCosto(int cantidad);
}