package interfaz;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Apoyo de consola: muestra el menú y lee los datos que escribe el usuario.
 * No contiene lógica del hotel; esa vive en {@link Main}.
 */
public class MenuConsola {
    protected Scanner entrada;

    public MenuConsola() {
        this.entrada = new Scanner(System.in);
    }

    /** Imprime todas las opciones del menú, agrupadas por tipo de operación. */
    public void mostrarMenu() {
        System.out.println("""

                ==================== SISTEMA DE GESTIÓN DE HOTEL ====================
                -- ALTAS ---------------------------------------------------------
                 1. Registrar huésped
                 2. Registrar empleado
                 3. Registrar habitación
                 4. Registrar servicio
                 5. Crear reservación
                -- CONSULTAS -----------------------------------------------------
                 6. Listar huéspedes
                 7. Listar empleados
                 8. Listar habitaciones
                 9. Listar servicios
                10. Listar reservaciones
                11. Buscar habitaciones disponibles
                -- ACTUALIZACIONES -----------------------------------------------
                12. Actualizar tipo de huésped
                13. Actualizar salario de empleado
                14. Actualizar estado de habitación
                15. Actualizar tarifa de habitación
                16. Actualizar precio de servicio
                17. Modificar fechas de reservación
                -- BAJAS ---------------------------------------------------------
                18. Dar de baja huésped
                19. Dar de baja empleado
                20. Dar de baja habitación
                21. Dar de baja servicio
                22. Cancelar reservación
                -- OPERACIÓN Y FACTURACIÓN --------------------------------------
                23. Check-in
                24. Check-out
                25. Registrar consumo de servicio
                26. Generar factura
                27. Pagar factura
                -- PERSONAL Y NÓMINA --------------------------------------------
                28. Calcular nómina
                29. Registrar limpieza de habitación
                30. Registrar propina de mesero
                31. Cerrar periodo de nómina
                -----------------------------------------------------------------
                 0. Salir""");
    }

    /**
     * Lee la opción del menú.
     *
     * @return el número escrito, o -1 si lo escrito no es un número
     */
    public int leerOpcion() {
        System.out.print("\nElige una opción: ");
        try {
            return Integer.parseInt(entrada.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ========================================================== Validaciones

    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("El valor no puede estar vacío.");
        }
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero.");
            }
        }
    }

    public int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("Elige un valor entre " + min + " y " + max + ".");
        }
    }

    public double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número válido (ej. 1500.50).");
            }
        }
    }

    public LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje));
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Usa el formato AAAA-MM-DD.");
            }
        }
    }

    public boolean leerSiNo(String mensaje) {
        while (true) {
            String r = leerTexto(mensaje).toLowerCase();
            if (r.equals("s") || r.equals("si") || r.equals("sí")) {
                return true;
            }
            if (r.equals("n") || r.equals("no")) {
                return false;
            }
            System.out.println("Responde s o n.");
        }
    }

    public List<Integer> leerListaEnteros(String mensaje) {
        while (true) {
            try {
                List<Integer> lista = new ArrayList<>();
                for (String parte : leerTexto(mensaje).split(",")) {
                    lista.add(Integer.parseInt(parte.trim()));
                }
                return lista;
            } catch (NumberFormatException e) {
                System.out.println("Escribe números separados por coma (ej. 101, 102).");
            }
        }
    }
}
