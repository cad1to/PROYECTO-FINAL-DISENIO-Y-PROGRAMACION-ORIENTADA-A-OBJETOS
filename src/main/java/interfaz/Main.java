package interfaz;

import servicio.Hotel;

public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Hotel", "Coatzacoalcos, Veracruz");
        new MenuConsola(hotel).iniciar();
    }
}
