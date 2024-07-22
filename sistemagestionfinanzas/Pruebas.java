package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static sistemagestionfinanzas.Usuario.usuario_actual;

public class Pruebas {
    public static void main(String[] args) {
        try {
            Stock.obtenerStocksBaseDatos("abc123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(Stock stock: Stock.instancias_stocks){
            try {
                System.out.println(stock.obtenerPrecioActual());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
