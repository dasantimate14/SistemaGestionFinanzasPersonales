package sistemagestionfinanzas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pruebas {
    public static void main(String[] args) {

        String consulta = "SELECT * FROM cuentas_bancarias WHERE idUsuario = 'abc123'";
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            System.out.println("Número de filas: " + rowCount);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            BaseDeDatos.cerrarConexion();
        }

        try {
            CuentaBancaria.obtenerCuentasBancariasBaseDatos("abc123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(CuentaBancaria.cantidad_instancias);
        for(CuentaBancaria cuentaBancaria: CuentaBancaria.intsancias_cuentas_bancarias){
            cuentaBancaria.obtenerInformacionCompleta();
        }


       try {
            Ingreso.obtenerIngresosBaseDatos("abc123");
            List<Ingreso> ingresos_actuales = new ArrayList<>(Ingreso.instancias_ingresos);
            if(!Ingreso.instancias_ingresos.isEmpty()){
                for(Ingreso ingreso: ingresos_actuales){
                    ingreso.obtenerInformacionCompleta();
                    //ingreso.actualizarInformacion();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
/*
        try {
            Gasto.obtenerGastoBaseDatos("abc123");
            List<Gasto> gastos_actuales = new ArrayList<>(Gasto.instancias_gastos);
            if(!Gasto.instancias_gastos.isEmpty()){
                for(Gasto gasto: gastos_actuales){
                    gasto.obtenerInformacionCompleta();
                    gasto.actualizarInformacion();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        /**Gasto.obtenerGastoBaseDatos(id_usuario);
        for(Gasto gasto : Gasto.instancias_gastos){
            gasto.obtenerInformacionCompleta();
        }


        PlazoFijo.obtenerPlazoFijosBaseDatos(id_usuario);
        Prestamo.obtenerPrestamosBaseDatos(id_usuario);
        Stock.obtenerStocksBaseDatos(id_usuario);
        TarjetaCredito.obtenerTarjetaCreditoBaseDatos(id_usuario);
         **/
        }


}
