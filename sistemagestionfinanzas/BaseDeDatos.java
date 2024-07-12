//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sistemagestionfinanzas;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDeDatos {
    private static Connection con;

    public BaseDeDatos() {
    }

    public static Connection establecerConexion() throws SQLException {
        con = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
        return con;
    }

    public static ResultSet realizarConsultaSelect(String consulta, String[] parametros) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = con.prepareStatement(consulta);

            for(int i = 0; i < parametros.length; ++i) {
                pst.setObject(i + 1, parametros[i]);
            }

            rs = pst.executeQuery();
            return rs;
        } catch (SQLException var5) {
            SQLException e = var5;
            System.out.println("Error al realizar la consulta SELECT: " + e.getMessage());
            throw e;
        }
    }

    public static boolean ejecutarActualizacion(String consulta, Object[] parametros) throws SQLException {
        try {
            PreparedStatement pst = con.prepareStatement(consulta);

            int filasAfectadas;
            for(filasAfectadas = 0; filasAfectadas < parametros.length; ++filasAfectadas) {
                pst.setObject(filasAfectadas + 1, parametros[filasAfectadas]);
            }

            filasAfectadas = pst.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println("Error al ejecutar la actualización: " + e.getMessage());
            throw e;
        }
    }

    public static void mostrarDatosTabla(String nombreTabla, String idUsuario) throws SQLException {
        String consulta = "SELECT * FROM " + nombreTabla + " WHERE idUsuario = ?";

        try {
            PreparedStatement pst = con.prepareStatement(consulta);

            try {
                pst.setString(1, idUsuario);
                ResultSet rs = pst.executeQuery();

                try {
                    int columnCount = rs.getMetaData().getColumnCount();

                    int i;
                    for(i = 1; i <= columnCount; ++i) {
                        System.out.print(rs.getMetaData().getColumnName(i) + "\t");
                    }

                    System.out.println();

                    while(rs.next()) {
                        for(i = 1; i <= columnCount; ++i) {
                            PrintStream var10000 = System.out;
                            String var10001 = rs.getString(i);
                            var10000.print(var10001 + "\t");
                        }

                        System.out.println();
                    }
                } catch (Throwable var9) {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (Throwable var10) {
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                }

                throw var10;
            }

            if (pst != null) {
                pst.close();
            }

        } catch (SQLException var11) {
            SQLException e = var11;
            System.out.println("Error al mostrar los datos de la tabla " + nombreTabla + ": " + e.getMessage());
            throw e;
        }
    }
}
