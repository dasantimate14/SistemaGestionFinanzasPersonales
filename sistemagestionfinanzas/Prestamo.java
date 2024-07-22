package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Prestamo extends FinanceItem {
    private String tipo_prestamo;
    private float saldo_pendiente;
    private int plazo;
    private LocalDate fecha_vencimiento;
    private int estatus;
    private float cuota_mensual;
    private CuentaBancaria cuenta_bancaria;
    public static int cantidad_instancias = 0;
    public static List<Prestamo> instancias_prestamos = new ArrayList<>();

    public Prestamo(String nombre, String descripcion, float montoOriginal, float tasaInteres, LocalDate fechaInicio,
                    String tipo_prestamo, int plazo, CuentaBancaria cuenta_bancaria) {
        super(nombre, descripcion, montoOriginal, "Pasivo", tasaInteres, fechaInicio);
        this.tipo_prestamo = tipo_prestamo;
        this.plazo = plazo;
        this.fecha_vencimiento = fechaInicio.plusDays(plazo);
        this.estatus = 1;
        this.cuenta_bancaria = cuenta_bancaria;
        instancias_prestamos.add(this);
        cantidad_instancias++;
    }

    @Override
    protected float calcularValorActual() throws IOException {
        return saldo_pendiente - (cuota_mensual * (LocalDate.now().until(fecha_vencimiento, ChronoUnit.MONTHS)));
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder info = new StringBuilder();
        info.append("Tipo de Préstamo: ").append(tipo_prestamo).append("\n");
        info.append("Saldo Pendiente: ").append(saldo_pendiente).append("\n");
        info.append("Plazo: ").append(plazo).append("\n");
        info.append("Fecha de Vencimiento: ").append(fecha_vencimiento).append("\n");
        info.append("Estatus: ").append(estatus).append("\n");
        info.append("Cuota Mensual: ").append(cuota_mensual).append("\n");
        info.append("Cuenta bancaria: ").append(cuenta_bancaria.getId()).append("\n");
        return info;
    }

    public static float calcularPorcentajeRepresentacionSubclase(List<FinanceItem> activosPasivos) {
        float total_pasivos = 0;
        float total_prestamos = 0;
        for (FinanceItem item : activosPasivos) {
            total_pasivos += item.getMontoActual();
            if(item instanceof Prestamo) {
                Prestamo prestamo = (Prestamo) item;
                total_prestamos += prestamo.getMontoActual();
            }
        }
        float porcentaje = (total_prestamos / total_pasivos) * 100;
        return porcentaje;
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
        this.cuota_mensual = calcularPagoMensual();
    }

    public String getTipoPrestamo() {
        return tipo_prestamo;
    }

    public void setTipoPrestamo(String tipo_prestamo) {
        this.tipo_prestamo = tipo_prestamo;
    }

    public float getSaldoPendiente() {
        return saldo_pendiente;
    }

    public void setSaldoPendiente(float saldo_pendiente) {
        this.saldo_pendiente = saldo_pendiente;
    }

    public void setCuotaMensual(float cuota_mensual) {this.cuota_mensual = cuota_mensual;}

    public float getCuotaMensual() {return cuota_mensual;}

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int nuevo_plazo) {
        this.plazo = nuevo_plazo;
    }

    public LocalDate getFechaVencimiento() {
        return fecha_vencimiento;
    }

    public void setFechaVencimiento(LocalDate fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int nuevo_estatus) {
        this.estatus = nuevo_estatus;
    }

    public int getFrecuenciaPago() {
        return 0;
    }

    public void setFrecuenciaPago(int nuevaFrecuenciaPago) {
    }

    public CuentaBancaria getCuentaBancaria(){
        return cuenta_bancaria;
    }

    public float calcularPagoMensual() {
        float saldo_pendiente = calcularSaldoPendiente();
        float tasa_interes_mensual = super.getTasaInteres() / 12 / 100;
        int numeros_pagos = plazo * 12;
        return (saldo_pendiente * tasa_interes_mensual) / (1 - (float)Math.pow(1 + tasa_interes_mensual, -numeros_pagos));
    }

    public float calcularTiempoRestante() {
        return saldo_pendiente * (super.getTasaInteres() / 100);
    }

    public float calcularSaldoPendiente() {
        return saldo_pendiente - (cuota_mensual * plazo);
    }

    public float calcularInteresAcumulado() {
        float interes_mensual = super.getTasaInteres() / 12 / 100;
        int numeros_pagos = plazo * 12;
        return numeros_pagos * cuota_mensual * interes_mensual;
    }

    public float calcularInteresPendiente() {
        float interes_mensual = super.getTasaInteres() / 12 / 100;
        return saldo_pendiente * interes_mensual * (plazo - (LocalDate.now().until(fecha_vencimiento, ChronoUnit.MONTHS)));
    }

    public void calcularPorcentajeRepresentacionPrestamo() {
        float total_prestamos = 0;
        for (Prestamo prestamo : instancias_prestamos) {
            total_prestamos += prestamo.getMontoActual();
        }
        float porcentaje = (getMontoActual() / total_prestamos) * 100;
        System.out.println("Porcentaje de Representación: " + porcentaje + "%");
    }

    public void guardarPrestamoBaseDatos(){
        //Consulta para guardar el objeto prestamo en la base de datos
        String consulta_registro = "INSERT INTO prestamos (id, nombre, descripcion, montoOriginal, fechaInicio, tipoPrestamo, plazo, fechaVencimiento, estatus, cuotaMensual, idUsuario, idCuentaBancaria, tasaInteres) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Arreglo con los parametros de la consulta
        String[] parametros = {getNombre(), getDescripcion(), String.valueOf(getMontoOriginal()), String.valueOf(getFechaInicio()), getTipoPrestamo(), String.valueOf(getPlazo()), String.valueOf(fecha_vencimiento),String.valueOf(estatus), String.valueOf(cuota_mensual) ,cuenta_bancaria.getIdUsuario(), cuenta_bancaria.getId(), String.valueOf(getTasaInteres())};

        //Registro en la base de datos
        try{
            BaseDeDatos.establecerConexion();
            boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registro_exitoso){
                System.out.println("Registro exitoso de Prestamo." );
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }


    }

    public static void obtenerPrestamosBaseDatos(String id_usuario) throws SQLException{
        Prestamo prestamo = null;
        String consulta = "SELECT * FROM prestamos WHERE idUsuario = ?";
        String[] parametro = {id_usuario};
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametro);
            if(rs == null){
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
                int estatus = rs.getInt("estatus");
                float cuota_mensual = rs.getFloat("cuotaMensual");
                String id_cuenta_bancaria = rs.getString("idCuentaBancaria");
                float tasa_interes = rs.getFloat("tasaInteres");

                CuentaBancaria cuenta_viculada = null;
                for(CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                    if(cuenta.getId().equals(id_cuenta_bancaria)) {
                        cuenta_viculada = cuenta;
                        //Se crea el objeto con los datos capturados
                        prestamo = new Prestamo(nombre, descripcion, monto_original, tasa_interes,fecha_inicio, tipo_prestamo, plazo, cuenta_viculada);
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
