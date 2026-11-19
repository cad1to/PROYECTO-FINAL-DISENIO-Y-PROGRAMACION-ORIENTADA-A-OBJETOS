package modelo.facturacion;

import java.time.LocalDate;
import java.util.List;

public class Factura {
    public static final double IMPUESTO = 0.16;

    protected String folio;
    protected LocalDate fechaEmision;
    protected double descuento;
    protected boolean pagada;
    protected List<DetalleFactura> detalles;

    public void agregarDetalle(Facturable f, int cantidad) {
    }

    public double calcularSubtotal() {
    }

    public double calcularTotal() {
    }

    public void pagar() {
    }
}
