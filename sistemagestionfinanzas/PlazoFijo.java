package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlazoFijo extends FinanceItem {
    private int plazo;
    private LocalDate fecha_final;
    private CuentaBancaria cuenta;
    static int cantidad_instancias_plazo_fijo;
    public static List<PlazoFijo> instancias_plazos_fijos = new ArrayList<>();

    public PlazoFijo(String nombre, String descripcion, float montoOriginal, float tasa_interes, LocalDate fecha_inicio, int plazo, CuentaBancaria cuenta) {
        super(nombre, descripcion, montoOriginal, "Activo", tasa_interes, fecha_inicio);
        this.plazo = plazo;
        this.cuenta = cuenta;
        this.fecha_final = fecha_inicio.plusYears(plazo);
        instancias_plazos_fijos.add(this);
        cantidad_instancias_plazo_fijo++;
    }

    // Métodos get y set de la clase
    public int getPlazo() {
        return plazo;
    }
    public void setPlazo(int nuevo_plazo) {
        this.plazo = nuevo_plazo;
        this.fecha_final = getFechaInicio().plusYears(nuevo_plazo);
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

    // Método para depositar los intereses del plazo fijo mensualmente
    private void depositarInteres() throws SQLException, IOException {
        LocalDate fecha_inicio = getFechaInicio();
        ResultSet rs = null;
        String consulta = "SELECT MAX(fechaInicio) AS fecha_mas_reciente FROM ingresos WHERE idUsuario = '" + cuenta.getIdUsuario() + "' AND idCuentaBancaria = '" + cuenta.getId() + "' AND descripcion = 'Interes generado por el plazo fijo " + getNombre() + "'";

        try {
            BaseDeDatos.establecerConexion();
            rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            if (rs.next()) {
                String fecha_mas_reciente = rs.getString("fecha_mas_reciente");
                if (fecha_mas_reciente != null) {
                    fecha_inicio = LocalDate.parse(fecha_mas_reciente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }

        while (fecha_inicio.isBefore(getFechaFinal())) {
            fecha_inicio = fecha_inicio.plusMonths(1);
            if (fecha_inicio.isAfter(getFechaFinal())) {
                break;
            }
            Ingreso ingreso = new Ingreso("Interes del plazo fijo ", "Interes generado por el plazo fijo " + getNombre(), calcularPromedioMensual(), fecha_inicio, cuenta.getBanco(), cuenta);
            cuenta.depositarMonto(ingreso.getMontoOriginal());
            ingreso.guardarIngresoBaseDatos();
        }
    }

    public float calcularInteresAcumulado() {
        LocalDate fecha_actual = LocalDate.now();
        long dias_transcurridos = ChronoUnit.DAYS.between(getFechaInicio(), getFechaFinal());
        return redonderCantidad((float) (getMontoOriginal() * (getTasaInteres() / 100) * (dias_transcurridos / 365.0)));
    }

    public float calcularMontoFinal() {
        float monto_original = getMontoOriginal();
        float interes_final = 0;
        try {
            interes_final = calcularPromedioAnual() * getPlazo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return monto_original + interes_final;
    }

    // Método para guardar el plazo fijo en la base de datos
    public void guardarPlazoFijoEnBaseDatos() {
        String consulta_registro = "INSERT INTO plazos_fijos (id, nombre, descripcion, montoOriginal, tasaInteres, fechaInicio, plazo, fechaFinal, idUsuario, idCuentaBancaria) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String[] parametros = {
                getNombre(),
                getDescripcion(),
                String.valueOf(getMontoOriginal()),
                String.valueOf(getTasaInteres()),
                String.valueOf(getFechaInicio()),
                String.valueOf(getPlazo()),
                String.valueOf(getFechaFinal()),
                cuenta.getIdUsuario(),
                cuenta.getId()
        };
        try {
            BaseDeDatos.establecerConexion();
            boolean registroExitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registroExitoso) {
                System.out.println("Registro exitoso del plazo fijo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    @Override
    public float calcularValorActual() throws IOException {
        setMontoActual(getMontoOriginal() + calcularInteresAcumulado());
        return getMontoActual();
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plazo en años: ").append(plazo).append("\n");
        sb.append("Cuenta Bancaria Asociada: ").append(cuenta.getId()).append("\n");
        sb.append("Fecha de Liberación: ").append(fecha_final).append("\n");
        return sb;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        float valor_total_plazos_fijos = 0;
        float valor_total_activos = 0;
        for (FinanceItem activo : activosPasivos) {
            valor_total_activos += activo.getMontoActual();
            if (activo instanceof PlazoFijo) {
                PlazoFijo cuenta = (PlazoFijo) activo;
                valor_total_plazos_fijos += cuenta.getMontoActual();
            }
        }
        float porcentaje_representacion = redonderCantidad((valor_total_plazos_fijos / valor_total_activos) * 100);
        System.out.println("El porcentaje de Representación de todos los plazos fijos es " + porcentaje_representacion + " con un valor de " + valor_total_plazos_fijos);
    }

    @Override
    public float calcularPromedioMensual() throws SQLException, IOException {
        setPromedioMensual(redonderCantidad(getMontoOriginal() * ((getTasaInteres() / 100) / 12)));
        return getPromedioMensual();
    }

    @Override
    public float calcularPromedioAnual() throws IOException {
        return redonderCantidad(getMontoOriginal() * (getTasaInteres() / 100));
    }

    @Override
    public void actualizarInformacion() throws IOException {
        try {
            depositarInteres();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setInteres(calcularInteresAcumulado());
        setMontoActual(getMontoOriginal() + getInteres());
    }

    // Método para encontrar un plazo fijo por ID
    public static PlazoFijo encontrarPlazoFijoPorId(String id) {
        for (PlazoFijo plazoFijo : instancias_plazos_fijos) {
            if (plazoFijo.getId().equals(id)) {
                return plazoFijo;
            }
        }
        return null;
    }

    // Método para encontrar el valor de esta cuenta bancaria dentro de todas las cuentas bancarias
    public void calcularPorcentajeRepresentacionPlazoFijo() {
        float total_plazos_fijos = 0;
        for (PlazoFijo plazo_fijo : instancias_plazos_fijos) {
            total_plazos_fijos += plazo_fijo.getMontoActual();
        }
        float porcentaje_representacion = (getMontoActual() / total_plazos_fijos) * 100;
        System.out.println("El porcentaje representacion es " + redonderCantidad(porcentaje_representacion) + "% con un valor de " + getMontoActual());
    }

    public static void obtenerPlazoFijosBaseDatos(String id_usuario) throws SQLException {
        String consulta = "SELECT * FROM plazos_fijos WHERE idUsuario = ?";
        String[] parametro = {id_usuario};
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametro);
            if (rs == null) {
                throw new SQLException("No se puede obtener ningún plazo fijo para este usuario");
            }
            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                float monto_original = rs.getFloat("montoOriginal");
                float tasa_interes = rs.getFloat("tasaInteres");
                LocalDate fecha_inicio = rs.getDate("fechaInicio").toLocalDate();
                int plazo = rs.getInt("plazo");
                LocalDate fecha_final = rs.getDate("fechaFinal").toLocalDate();
                String id_cuenta_bancaria = rs.getString("idCuentaBancaria");

                CuentaBancaria cuenta_viculada = null;
                for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                    if (cuenta.getId().equals(id_cuenta_bancaria)) {
                        cuenta_viculada = cuenta;
                        PlazoFijo plazo_fijo = new PlazoFijo(nombre, descripcion, monto_original, tasa_interes, fecha_inicio, plazo, cuenta_viculada);
                        plazo_fijo.setId(id);
                    }
                }
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    public void eliminarPlazoFijoEnBaseDatos() {
        String consulta_eliminacion = "DELETE FROM plazos_fijos WHERE id = ?";
        String[] parametros = {getId()};
        try {
            BaseDeDatos.establecerConexion();
            boolean eliminacionExitosa = BaseDeDatos.ejecutarActualizacion(consulta_eliminacion, parametros);
            if (eliminacionExitosa) {
                System.out.println("Eliminación exitosa del plazo fijo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }
}