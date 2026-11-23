package interfaz;

import servicio.Hotel;

import java.util.Scanner;

public class MenuConsola {
    protected Hotel hotel;
    protected Scanner entrada;

    public MenuConsola(Hotel hotel) {
        this.hotel = hotel;
        this.entrada = new Scanner(System.in);
    }

    /** Muestra el menú en ciclo hasta que el usuario elige la opción 0 (salir). */
    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();
            if (opcion > 31 || opcion < -1) {
                System.out.println("Opción no válida.");
            } else if (opcion > 0) {
                System.out.println("Opción " + opcion + " aún no implementada.");
            } else if (opcion == -1) {
                System.out.println("Escribe un número.");
            }
        } while (opcion != 0);
        System.out.println("Hasta luego.");
    }

    /** Imprime todas las opciones del menú, agrupadas por tipo de operación. */
    protected void mostrarMenu() {
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
    protected int leerOpcion() {
        System.out.print("\nElige una opción: ");
        try {
            return Integer.parseInt(entrada.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
