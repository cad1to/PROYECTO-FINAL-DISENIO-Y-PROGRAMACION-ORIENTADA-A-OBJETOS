package modelo.catalogo;

import modelo.comun.Desactivable;
import modelo.facturacion.Facturable;

/**
 * Servicio del catálogo del hotel (restaurante, spa o lavandería). Clase
 * abstracta que concentra lo común; cada subclase define cómo calcula su costo.
 */
// ABSTRACCIÓN: Servicio es abstracta, con un método abstracto (calcularCosto) y métodos concretos
// INTERFAZ: Servicio implementa Facturable (se cobra en la factura) y Desactivable (baja lógica)
public abstract class Servicio implements Facturable, Desactivable {
    // ENCAPSULACIÓN: atributos protected, accedidos con getters/setters o métodos de negocio
    protected String codigo;
    protected String nombre;
    protected double precioBase;
    protected CategoriaServicio categoria;
    protected boolean activo;

    /**
     * Crea un servicio activo. La categoría la fija cada subclase y no tiene setter.
     *
     * @param codigo     código del servicio, no vacío (la unicidad la valida Hotel)
     * @param nombre     nombre del servicio, no vacío
     * @param precioBase precio base (por platillo, sesión o prenda), positivo
     * @param categoria  categoría del servicio, no nula
     * @throws IllegalArgumentException si algún dato es inválido
     */
    protected Servicio(String codigo, String nombre, double precioBase, CategoriaServicio categoria) {
        validarTexto(codigo, "El código");
        validarTexto(nombre, "El nombre");
        validarPositivo(precioBase, "El precio base");
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.categoria = categoria;
        this.activo = true;
    }

    /**
     * Calcula el costo del servicio según su tipo.
     *
     * @param cantidad platillos, sesiones o prendas, según la categoría; positiva
     * @return costo total en pesos
     */
    // POLIMORFISMO: cada subclase sobrescribe este método con su propia regla (RN-19 a RN-21)
    @Override
    public abstract double calcularCosto(int cantidad);

    /**
     * Concepto que aparece en el detalle de la factura.
     *
     * @return texto con el nombre y la categoría del servicio
     */
    @Override
    public String getConcepto() {
        return nombre + " (" + categoria + ")";
    }

    @Override
    public boolean isActivo() {
        return activo;
    }

    /** Da de baja lógica el servicio (RN-34); sus consumos anteriores se conservan (RN-38). */
    @Override
    public void darDeBaja() {
        this.activo = false;
    }

    /**
     * Valida que la cantidad consumida sea positiva.
     *
     * @param cantidad cantidad a revisar
     * @throws IllegalArgumentException si la cantidad no es positiva
     */
    protected static void validarCantidad(int cantidad) {
        validarPositivo(cantidad, "La cantidad");
    }

    private static void validarPositivo(double valor, String nombre) {
        if (valor <= 0) {
            throw new IllegalArgumentException(nombre + " debe ser positivo.");
        }
    }

    private static void validarTexto(String texto, String nombre) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(nombre + " no puede estar vacío.");
        }
    }

    /** @return código único del servicio */
    public String getCodigo() {
        return codigo;
    }

    /** @return nombre del servicio */
    public String getNombre() {
        return nombre;
    }

    /** @return precio base del servicio */
    public double getPrecioBase() {
        return precioBase;
    }

    /**
     * Actualiza el precio base. No modifica facturas ya generadas (RN-45).
     *
     * @param precioBase nuevo precio, positivo
     * @throws IllegalArgumentException si el precio no es positivo
     */
    public void setPrecioBase(double precioBase) {
        validarPositivo(precioBase, "El precio base");
        this.precioBase = precioBase;
    }

    /** @return categoría del servicio */
    public CategoriaServicio getCategoria() {
        return categoria;
    }
}