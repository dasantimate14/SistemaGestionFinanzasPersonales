package sistemagestionfinanzas;

import java.sql.*;

//Clase para manejar la base de datos
public class BaseDeDatos {
    //La conexion a la base de datos se maneja con el plugin database de InteliJ
    private static Connection con;

    public static void establecerConexion() throws SQLException {
        con = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
    }

    // Método para ejecutar consultas SELECT
    public static ResultSet realizarConsultaSelect(String consulta, Object[] parametros) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(consulta);

            // Establecer los parámetros en el PreparedStatement que evita inyecciones de SQL
            for (int i = 0; i < parametros.length; i++) {
                pst.setObject(i + 1, parametros[i]);
            }

            rs = pst.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("Error al realizar la consulta SELECT: " + e.getMessage());
            throw e;
        }
    }

    // Método estático para ejecutar consultas INSERT, UPDATE, DELETE
    public static int ejecutarActualizacion(String consulta, Object[] parametros) throws SQLException {
        try {
            PreparedStatement pst = con.prepareStatement(consulta);
            // Establecer los parámetros en el PreparedStatement
            for (int i = 0; i < parametros.length; i++) {
                pst.setObject(i + 1, parametros[i]);
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la actualización: " + e.getMessage());
            throw e;
        }
    }

}
