package sistemagestionfinanzas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlazoFijo {
    private int plazo;
    private LocalDate fecha_final;
    private CuentaBancaria cuenta;
    private String id;

    private static List<PlazoFijo> instanciasPlazosFijos = new ArrayList<>();

    public PlazoFijo(int plazo, CuentaBancaria cuenta) {
        this.plazo = plazo;
        this.cuenta = cuenta;
        this.fecha_final = LocalDate.now().plusMonths(plazo);
        instanciasPlazosFijos.add(this);
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int nuevo_plazo) {
        this.plazo = nuevo_plazo;
        this.fecha_final = LocalDate.now().plusMonths(nuevo_plazo);
    }

    public LocalDate getFechaFinal() {
        return fecha_final;
    }

    public void setFechaFinal(LocalDate nueva_fecha_final) {
        this.fecha_final = nueva_fecha_final;
    }

    public CuentaBancaria getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaBancaria cuenta) {
        this.cuenta = cuenta;
    }

    public void depositarInteres() {
        float interes_acumulado = calcularInteresAcumulado();
        cuenta.depositarMonto(interes_acumulado);
    }

    public float calcularInteresAcumulado() {
        float tasa_interes = cuenta.getInteres(); // Obtenemos la tasa de interés de la cuenta
        float monto_original = cuenta.getMontoOriginal();
        int meses = plazo;
        float interes_acumulado = monto_original * (tasa_interes / 100) * (meses / 12.0f);
        return interes_acumulado;
    }

    public float calcularMontoFinal() {
        float monto_original = cuenta.getMontoOriginal();
        float interes_acumulado = calcularInteresAcumulado();
        return monto_original + interes_acumulado;
    }

    // Método para guardar el plazo fijo en la base de datos
    public void guardarPlazoFijoEnBaseDatos() {
        String consultaRegistro = "INSERT INTO plazos_fijos (plazo, fecha_final, id_cuenta) VALUES (?, ?, ?)";
        String[] parametros = new String[]{
                String.valueOf(getPlazo()),
                String.valueOf(getFechaFinal()),
                cuenta.getId()
        };

        try {
            BaseDeDatos.establecerConexion();
            boolean registroExitoso = BaseDeDatos.ejecutarActualizacion(consultaRegistro, parametros);
            if (registroExitoso) {
                System.out.println("Registro exitoso del plazo fijo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    // Método para cargar plazos fijos desde la base de datos
    public static void cargarPlazosFijosDesdeBaseDatos(String idUsuario) {
        String consulta = "SELECT * FROM plazos_fijos WHERE id_usuario = '" + idUsuario + "'";
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            while (rs.next()) {
                // Leer cada uno de los campos en el ResultSet para crear el objeto
                int plazo = rs.getInt("plazo");
                LocalDate fecha_final = rs.getDate("fecha_final").toLocalDate();
                String id_cuenta = rs.getString("id_cuenta");

                // Aquí deberías tener una forma de obtener la instancia de CuentaBancaria
                // que corresponde a id_cuenta. Si no puedes hacerlo directamente,
                // necesitarás proporcionar la instancia de alguna otra forma.

                CuentaBancaria cuenta_asociada = obtenerCuentaPorId(id_cuenta);

                if (cuenta_asociada != null) {
                    PlazoFijo plazo_fijo = new PlazoFijo(plazo, cuenta_asociada);
                    plazo_fijo.setFechaFinal(fecha_final);
                    plazo_fijo.setId(rs.getString("id"));
                } else {
                    System.out.println("No se encontró la cuenta bancaria asociada con el ID: " + id_cuenta);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    private static CuentaBancaria obtenerCuentaPorId(String id) {
        // Implementa aquí la lógica para obtener la cuenta bancaria por ID.
        // Dependiendo de tu contexto, esto puede implicar una consulta a la base de datos,
        // o tener una forma de mapear IDs a instancias de CuentaBancaria.
        return null; // Cambia esto por la implementación real
    }

    // Métodos para obtener y establecer el ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Método para encontrar un plazo fijo por ID
    public static PlazoFijo encontrarPlazoFijoPorId(String id) {
        for (PlazoFijo plazoFijo : instanciasPlazosFijos) {
            if (plazoFijo.getId().equals(id)) {
                return plazoFijo;
            }
        }
        return null;
    }
}
