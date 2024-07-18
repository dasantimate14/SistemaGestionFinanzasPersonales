package sistemagestionfinanzas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlazoFijo {
    private int plazo;
    private LocalDate fecha_final;
    private CuentaBancaria cuenta;
    private String id;

    private static List<PlazoFijo> instanciasPlazosFijos = new ArrayList<>();
    private static List<CuentaBancaria> instanciasCuentasBancarias = new ArrayList<>(); // Lista de cuentas bancarias

    public PlazoFijo(int plazo, CuentaBancaria cuenta) {
        this.plazo = plazo;
        this.cuenta = Objects.requireNonNull(cuenta, "La cuenta bancaria no puede ser nula");
        this.fecha_final = LocalDate.now().plusMonths(plazo);
        instanciasPlazosFijos.add(this);
        agregarCuentaBancaria(cuenta); // Agregamos la cuenta a la lista de instancias
    }

    private void agregarCuentaBancaria(CuentaBancaria cuenta) {
        if (!instanciasCuentasBancarias.contains(cuenta)) {
            instanciasCuentasBancarias.add(cuenta);
        }
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
        this.cuenta = Objects.requireNonNull(cuenta, "La cuenta bancaria no puede ser nula");
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

                // Obtener la instancia de CuentaBancaria correspondiente a id_cuenta
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
        for (CuentaBancaria cuenta : instanciasCuentasBancarias) {
            if (cuenta.getId().equals(id)) {
                return cuenta;
            }
        }
        return null;
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

    // Método para cargar ingresos desde la base de datos
    public static void cargarDatosDesdeBaseDatos(String idUsuario) {
        cargarPlazosFijosDesdeBaseDatos(idUsuario);
        cargarIngresosDesdeBaseDatos(idUsuario);
    }

    private static void cargarIngresosDesdeBaseDatos(String idUsuario) {
        String consulta = "SELECT * FROM ingresos WHERE idUsuario = '" + idUsuario + "'";
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            while (rs.next()) {
                // Leer cada uno de los campos en el ResultSet para manejar la información
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                float monto_original = rs.getFloat("montoOriginal");
                LocalDate fecha_inicio = rs.getDate("fechaInicio").toLocalDate();
                String fuente = rs.getString("fuente");
                int frecuencia = rs.getInt("frecuencia");
                String id_cuenta_bancaria = rs.getString("idCuentaBancaria");

                CuentaBancaria cuenta_viculada = null;
                for (CuentaBancaria cuenta : instanciasCuentasBancarias) {
                    cuenta.obtenerInformacionCompleta();
                    if (cuenta.getId().equals(id_cuenta_bancaria)) {
                        cuenta_viculada = cuenta;
                        break;
                    }
                }

                if (cuenta_viculada == null) {
                    System.out.println("No existe la cuenta con el ID: " + id_cuenta_bancaria);
                } else {
                    // Aquí podrías hacer algo con la información de ingreso, como agregarlo a una lista
                    // Por ejemplo, podrías almacenar los ingresos en una lista o en otro tipo de estructura
                    System.out.println("Ingreso cargado: " + nombre);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }
}


