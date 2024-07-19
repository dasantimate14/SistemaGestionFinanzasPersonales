package sistemagestionfinanzas;

import java.sql.*;

//Clase para manejar la base de datos
public class BaseDeDatos {
    //La conexion a la base de datos se maneja con el plugin database de InteliJ
    private static Connection con = null;

    public static void establecerConexion() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Lanza una SQLException personalizada indicando que el controlador no fue encontrado
            throw new SQLException("No se encontró el controlador JDBC de MariaDB.");
        }
        con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sistema_gestion_finanzas", "root", "");
    }

    // Método para ejecutar consultas SELECT con proteccion a SQL Injections, usar cuando se guardan datos ingresados por el usuario
    public static ResultSet realizarConsultaSelect(String consulta, String[] parametros) throws SQLException {
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

    // Método para ejecutar consultas SELECT usando una consulta creada por los desarrolladores
    public static ResultSet realizarConsultaSelectInterna(String consulta) throws SQLException {
        Statement st= null;
        ResultSet rs = null;
        try {
            //Se crea un objeto statement
            st = con.createStatement();

            //Se ejecuta la consulta y se obtienen los resultados
            rs = st.executeQuery(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    // Método estático para ejecutar consultas INSERT, UPDATE, DELETE
    public static boolean ejecutarActualizacion(String consulta, String[] parametros) throws SQLException {
        try {
            PreparedStatement pst = con.prepareStatement(consulta);
            // Establecer los parámetros en el PreparedStatement
            for (int i = 0; i < parametros.length; i++) {
                pst.setObject(i + 1, parametros[i]);
            }
            int filasAfectadas = pst.executeUpdate();
            // Devuelve true si se afectó al menos una fila
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.getMessage();
            System.out.println("Error al ejecutar la actualización: " + e.getMessage());
            throw e;
        }

    }

    // Método para mostrar los datos de una tabla específica para un usuario especifico
    public static void mostrarDatosTabla(String nombreTabla, String idUsuario) throws SQLException {
        String consulta = "SELECT * FROM " + nombreTabla + " WHERE idUsuario = ?";

        try (PreparedStatement pst = con.prepareStatement(consulta)) {
            pst.setString(1, idUsuario); // Establecer el parámetro de manera segura

            try (ResultSet rs = pst.executeQuery()) {
                // Obtener metadatos de las columnas
                int columnCount = rs.getMetaData().getColumnCount();

                // Imprimir encabezados de las columnas
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + "\t");
                }
                System.out.println();

                // Imprimir los datos de cada fila
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(rs.getString(i) + "\t");
                    }
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar los datos de la tabla " + nombreTabla + ": " + e.getMessage());
            throw e;
        }
    }

    public static void cerrarConexion(){
        try {
            if (con != null){
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
