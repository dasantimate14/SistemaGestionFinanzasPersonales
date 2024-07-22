package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Prestamo extends FinanceItem {
    private String tipoPrestamo;
    private float saldoPendiente;
    private int plazo;
    private LocalDate fechaVencimiento;
    private int estatus;
    private float cuotaMensual;
    private CuentaBancaria cuenta_bancaria;
    public static int cantidadInstancias = 0;
    public static List<Prestamo> instanciasPrestamos = new ArrayList<>();

    public Prestamo(String nombre, String descripcion, float montoOriginal, float tasaInteres, LocalDate fechaInicio,
                    String tipoPrestamo, int plazo, CuentaBancaria cuenta_bancaria) {
        super(nombre, descripcion, montoOriginal, "Pasivo", tasaInteres, fechaInicio);
        this.tipoPrestamo = tipoPrestamo;
        this.plazo = plazo;
        this.fechaVencimiento = fechaInicio.plusMonths(plazo);
        this.estatus = 1;
        this.cuenta_bancaria = cuenta_bancaria;
        this.saldoPendiente = montoOriginal; // Inicializando saldoPendiente con montoOriginal
        this.cuotaMensual = calcularPagoMensual(); // Calculando la cuota mensual al inicio
        instanciasPrestamos.add(this);
        cantidadInstancias++;
    }

    @Override
    protected float calcularValorActual() throws IOException {
        int mesesRestantes = (int) ChronoUnit.MONTHS.between(LocalDate.now(), fechaVencimiento);
        return saldoPendiente - (cuotaMensual * mesesRestantes);
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder info = new StringBuilder();
        info.append("Tipo de Préstamo: ").append(tipoPrestamo).append("\n");
        info.append("Saldo Pendiente: ").append(saldoPendiente).append("\n");
        info.append("Plazo: ").append(plazo).append("\n");
        info.append("Fecha de Vencimiento: ").append(fechaVencimiento).append("\n");
        info.append("Estatus: ").append(estatus).append("\n");
        info.append("Cuota Mensual: ").append(cuotaMensual).append("\n");
        info.append("Cuenta bancaria: ").append(cuenta_bancaria.getId()).append("\n");
        return info;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        float totalPasivos = 0;
        float totalPrestamos = 0;
        for (FinanceItem item : activosPasivos) {
            totalPasivos += item.getMontoActual();
            if (item instanceof Prestamo) {
                Prestamo prestamo = (Prestamo) item;
                totalPrestamos += prestamo.getMontoActual();
            }
        }
        float porcentaje = (totalPrestamos / totalPasivos) * 100;
        System.out.println("Porcentaje de Representación de los Prestamos: " + porcentaje + "%");
    }

    @Override
    protected float calcularPromedioMensual() throws SQLException, IOException {
        return calcularPagoMensual();
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        return calcularPagoMensual() * 12;
    }

    @Override
    public void actualizarInformacion() throws IOException {
        this.cuotaMensual = calcularPagoMensual();
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String nuevoTipoPrestamo) {
        this.tipoPrestamo = nuevoTipoPrestamo;
    }

    public float getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(float nuevoSaldoPendiente) {
        this.saldoPendiente = nuevoSaldoPendiente;
    }

    public void setCuotaMensual(float cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public float getCuotaMensual() {
        return cuotaMensual;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int nuevoPlazo) {
        this.plazo = nuevoPlazo;
    }

    public LocalDate getFechaDesembolso() {
        return super.getFechaInicio();
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate nuevaFechaVencimiento) {
        this.fechaVencimiento = nuevaFechaVencimiento;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int nuevoEstatus) {
        this.estatus = nuevoEstatus;
    }

    public int getFrecuenciaPago() {
        return 0;
    }

    public void setFrecuenciaPago(int nuevaFrecuenciaPago) {
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuenta_bancaria;
    }

    public float calcularPagoMensual() {
        float tasaInteresMensual = super.getTasaInteres() / 12 / 100;
        int numeroPagos = plazo * 12;
        return (saldoPendiente * tasaInteresMensual) / (1 - (float) Math.pow(1 + tasaInteresMensual, -numeroPagos));
    }

    public float calcularTiempoRestante() {
        return saldoPendiente * (super.getTasaInteres() / 100);
    }

    public float calcularSaldoPendiente() {
        return saldoPendiente - (cuotaMensual * plazo);
    }

    public float calcularInteresAcumulado() {
        float interesMensual = super.getTasaInteres() / 12 / 100;
        int numeroPagos = plazo * 12;
        return numeroPagos * cuotaMensual * interesMensual;
    }

    public float calcularInteresPendiente() {
        float interesMensual = super.getTasaInteres() / 12 / 100;
        return saldoPendiente * interesMensual * (plazo - (LocalDate.now().until(fechaVencimiento, ChronoUnit.MONTHS)));
    }

    public void calcularPorcentajeRepresentacionPrestamo() {
        float totalPrestamos = 0;
        for (Prestamo prestamo : instanciasPrestamos) {
            totalPrestamos += prestamo.getMontoActual();
        }
        float porcentaje = (getMontoActual() / totalPrestamos) * 100;
        System.out.println("Porcentaje de Representación: " + porcentaje + "%");
    }

    public void guardarPrestamoBaseDatos() {
        //Consulta para guardar el objeto prestamo en la base de datos
        String consulta_registro = "INSERT INTO prestamos (id, nombre, descripcion, montoOriginal, fechaInicio, tipoPrestamo, plazo, fechaVencimiento, estatus, cuotaMensual, idUsuario, idCuentaBancaria, tasaInteres) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Arreglo con los parametros de la consulta
        String[] parametros = {getNombre(), getDescripcion(), String.valueOf(getMontoOriginal()), String.valueOf(getFechaInicio()), getTipoPrestamo(), String.valueOf(getPlazo()), String.valueOf(fechaVencimiento), String.valueOf(estatus), String.valueOf(cuotaMensual), cuenta_bancaria.getIdUsuario(), cuenta_bancaria.getId(), String.valueOf(getTasaInteres())};

        //Registro en la base de datos
        try {
            BaseDeDatos.establecerConexion();
            boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registro_exitoso) {
                System.out.println("Registro exitoso de Prestamo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    public static void obtenerPrestamosBaseDatos(String id_usuario) throws SQLException {
        Prestamo prestamo = null;
        String consulta = "SELECT * FROM prestamos WHERE idUsuario = ?";
        String[] parametro = {id_usuario};
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametro);
            if (rs == null) {
                throw new SQLException("No se puede obtener ningun prestamo para este usuario");
            }
            while (rs.next()) {
                // Leer cada uno de los campos en el ResultSet para manejar la información
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                float monto_original = rs.getFloat("montoOriginal");
                LocalDate fecha_inicio = rs.getDate("fechaInicio").toLocalDate();
                String tipo_prestamo = rs.getString("tipoPrestamo");
                int plazo = rs.getInt("plazo");
                LocalDate fecha_vencimiento = rs.getDate("fechaVencimiento").toLocalDate();
                int estatus = rs.getInt("estatus");
                float cuota_mensual = rs.getFloat("cuotaMensual");
                String id_cuenta_bancaria = rs.getString("idCuentaBancaria");
                float tasa_interes = rs.getFloat("tasaInteres");

                CuentaBancaria cuenta_viculada = null;
                for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                    if (cuenta.getId().equals(id_cuenta_bancaria)) {
                        cuenta_viculada = cuenta;
                        // Se crea el objeto con los datos capturados
                        prestamo = new Prestamo(nombre, descripcion, monto_original, tasa_interes, fecha_inicio, tipo_prestamo, plazo,  cuenta_viculada);
                        prestamo.setId(id);
                        prestamo.setEstatus(estatus);
                        prestamo.setCuotaMensual(cuota_mensual);
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }
}
