package modelo.facturacion;

import excepciones.HotelException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cuenta final de una reservación. Se genera una sola vez (RN-46) y compone
 * sus líneas de detalle.
 */
public class Factura {
    // CONSTANTE: impuesto que se aplica sobre el monto ya con descuento (RN-47)
    public static final double IMPUESTO = 0.16;

    protected String folio;
    protected LocalDate fechaEmision;
    protected double descuento;
    protected boolean pagada;
    // COMPOSICIÓN: Factura crea y controla sus propios DetalleFactura; nacen y mueren con ella
    protected List<DetalleFactura> detalles;

    /**
     * Crea una factura sin detalles y sin pagar.
     *
     * @param folio        folio único generado por el sistema (RN-52), no vacío
     * @param fechaEmision fecha en que se genera, no nula
     * @param descuento    porcentaje de descuento aplicado según el tipo de huésped (RN-26),
     *                     entre 0 y 1; 0 si es REGULAR
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public Factura(String folio, LocalDate fechaEmision, double descuento) {
        if (folio == null || folio.isBlank()) {
            throw new IllegalArgumentException("El folio no puede estar vacío.");
        }
        if (fechaEmision == null) {
            throw new IllegalArgumentException("La fecha de emisión no puede ser nula.");
        }
        if (descuento < 0 || descuento > 1) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 1.");
        }
        this.folio = folio;
        this.fechaEmision = fechaEmision;
        this.descuento = descuento;
        this.pagada = false;
        this.detalles = new ArrayList<>();
    }

    /**
     * Agrega una línea a la factura con el costo del concepto.
     *
     * @param f        habitación o servicio a facturar, no nulo
     * @param cantidad noches, platillos, sesiones o prendas; positiva
     * @throws IllegalArgumentException si el concepto es nulo o la cantidad no es positiva
     */
    // DEPENDENCIA: Factura usa un Facturable solo como parámetro, sin guardarlo
    // POLIMORFISMO: calcularCosto() responde distinto según sea habitación o servicio
    public void agregarDetalle(Facturable f, int cantidad) {
        if (f == null) {
            throw new IllegalArgumentException("El concepto a facturar no puede ser nulo.");
        }
        detalles.add(new DetalleFactura(f.getConcepto(), cantidad, f.calcularCosto(cantidad)));
    }

    /**
     * Suma los montos de todos los detalles (RN-24).
     *
     * @return subtotal sin descuento ni impuesto
     */
    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleFactura d : detalles) {
            subtotal += d.getMonto();
        }
        return subtotal;
    }

    /**
     * Calcula el total: {@code (subtotal - subtotal × descuento) × (1 + IMPUESTO)}.
     * Primero se aplica el descuento y después el impuesto (RN-47).
     *
     * @return total a pagar en pesos
     */
    public double calcularTotal() {
        double subtotal = calcularSubtotal();
        return (subtotal - subtotal * descuento) * (1 + IMPUESTO);
    }

    /**
     * Registra el pago de la factura.
     *
     * @throws HotelException si la factura ya estaba pagada (RN-46)
     */
    public void pagar() throws HotelException {
        if (pagada) {
            throw new HotelException("La factura " + folio + " ya fue pagada.");
        }
        pagada = true;
    }

    /** @return folio único de la factura */
    public String getFolio() {
        return folio;
    }

    /** @return fecha de emisión */
    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    /** @return porcentaje de descuento aplicado (0 a 1) */
    public double getDescuento() {
        return descuento;
    }

    /** @return {@code true} si la factura ya fue pagada */
    public boolean isPagada() {
        return pagada;
    }

    /**
     * Devuelve los detalles en una vista de solo lectura, para que solo la
     * factura pueda modificarlos.
     *
     * @return lista no modificable de detalles
     */
    public List<DetalleFactura> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }
}