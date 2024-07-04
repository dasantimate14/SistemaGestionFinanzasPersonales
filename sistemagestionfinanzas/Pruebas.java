package sistemagestionfinanzas;

import java.time.LocalDate;

public class Pruebas {
    public static void main(String[] args) {
        // Creando un objeto Stock usando el constructor con tasaInteres
        Stock stockConTasa = new Stock("Acción Y", "Descripción de la acción Y", 1000.0f,
                LocalDate.now(), "Empresa Y", "SYM", 100, 50.0f, "Tecnología", 2.0f);

        // Creando un objeto Stock usando el constructor sin tasaInteres
        Stock stockSinTasa = new Stock("Acción Z", "Descripción de la acción Z", 2000.0f,
                LocalDate.now(), "Empresa Z", "SYM", 200, 75.0f, "Salud", 1.5f);

        // Imprimiendo la información completa del objeto Stock usando el método de la superclase
        stockConTasa.obtenerInformacionCompleta();
        System.out.println("-----");
        stockSinTasa.obtenerInformacionCompleta();
    }

}
