package modelo.facturacion;

public interface Facturable {
    String getConcepto();

    double calcularCosto(int cantidad);
}
