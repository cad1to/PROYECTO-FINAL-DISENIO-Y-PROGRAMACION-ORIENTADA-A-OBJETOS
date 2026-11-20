package modelo.alojamiento;

import modelo.comun.Desactivable;
import modelo.facturacion.Facturable;

/**
 * Habitación del hotel. Clase abstracta que concentra lo que comparten todos
 * los tipos de habitación; cada subclase define cómo calcula su tarifa.
 */
// ABSTRACCIÓN: Habitacion es abstracta, tiene un método abstracto (calcularTarifa) y métodos concretos
// INTERFAZ: Habitacion implementa Facturable (se cobra en la factura) y Desactivable (baja lógica)
public abstract class Habitacion implements Facturable, Desactivable {
    // ENCAPSULACIÓN: atributos protected, accedidos con getters/setters o métodos de negocio
    protected int numero;
    protected int piso;
    protected int capacidad;
    protected double tarifaBase;
    protected EstadoHabitacion estado;
    protected boolean activo;

    /**
     * Crea una habitación nueva, en estado DISPONIBLE y activa (RN-12).
     *
     * @param numero     número de habitación, positivo (la unicidad la valida Hotel)
     * @param piso       piso donde se ubica, positivo
     * @param capacidad  número máximo de huéspedes, positivo
     * @param tarifaBase tarifa base por noche, positiva
     * @throws IllegalArgumentException si algún dato numérico no es positivo
     */
    protected Habitacion(int numero, int piso, int capacidad, double tarifaBase) {
        validarPositivo(numero, "El número de habitación");
        validarPositivo(piso, "El piso");
        validarPositivo(capacidad, "La capacidad");
        validarPositivo(tarifaBase, "La tarifa base");
        this.numero = numero;
        this.piso = piso;
        this.capacidad = capacidad;
        this.tarifaBase = tarifaBase;
        this.estado = EstadoHabitacion.DISPONIBLE;
        this.activo = true;
    }

    /**
     * Calcula el costo de hospedaje según el tipo de habitación.
     *
     * @param noches número de noches, positivo
     * @return costo total del hospedaje en pesos
     */
    // POLIMORFISMO: cada subclase sobreescribe este método con su propia fórmula (RN-16 a RN-18)
    public abstract double calcularTarifa(int noches);

    /**
     * Costo que se factura por esta habitación; delega en {@link #calcularTarifa(int)}.
     *
     * @param noches número de noches, positivo
     * @return costo total del hospedaje en pesos
     * @throws IllegalArgumentException si las noches no son positivas
     */
    @Override
    public double calcularCosto(int noches) {
        validarPositivo(noches, "El número de noches");
        return calcularTarifa(noches);
    }

    /**
     * Concepto que aparece en el detalle de la factura.
     *
     * @return texto con el número y el tipo de habitación
     */
    @Override
    public String getConcepto() {
        return "Habitación " + numero + " (" + getClass().getSimpleName() + ")";
    }

    /**
     * Cambia el estado de la habitación. No aplica RN-42: la restricción sobre
     * asignar o liberar OCUPADA manualmente la valida {@code Hotel} al actualizar
     * el estado; el check-in y el check-out usan este método directamente.
     *
     * @param e nuevo estado, no nulo
     * @throws IllegalArgumentException si el estado es nulo
     */
    public void cambiarEstado(EstadoHabitacion e) {
        if (e == null) {
            throw new IllegalArgumentException("El estado de la habitación no puede ser nulo.");
        }
        this.estado = e;
    }

    /** @return estado actual de la habitación */
    public EstadoHabitacion getEstado() {
        return estado;
    }

    @Override
    public boolean isActivo() {
        return activo;
    }

    /** Da de baja lógica la habitación (RN-34); se conserva para el historial. */
    @Override
    public void darDeBaja() {
        this.activo = false;
    }

    /**
     * Valida que un valor sea positivo.
     *
     * @param valor  valor a revisar
     * @param nombre nombre del dato, para el mensaje de error
     * @throws IllegalArgumentException si el valor no es positivo
     */
    protected static void validarPositivo(double valor, String nombre) {
        if (valor <= 0) {
            throw new IllegalArgumentException(nombre + " debe ser positivo.");
        }
    }

    /** @return número de habitación */
    public int getNumero() {
        return numero;
    }

    /** @return piso donde se ubica */
    public int getPiso() {
        return piso;
    }

    /** @return capacidad máxima de huéspedes */
    public int getCapacidad() {
        return capacidad;
    }

    /** @return tarifa base por noche */
    public double getTarifaBase() {
        return tarifaBase;
    }

    /**
     * Actualiza la tarifa base. No modifica facturas ya generadas (RN-45).
     *
     * @param tarifaBase nueva tarifa, positiva
     * @throws IllegalArgumentException si la tarifa no es positiva
     */
    public void setTarifaBase(double tarifaBase) {
        validarPositivo(tarifaBase, "La tarifa base");
        this.tarifaBase = tarifaBase;
    }
}