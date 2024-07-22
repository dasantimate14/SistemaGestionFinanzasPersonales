package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
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
    public void actualizarInformacion() throws IOException, SQLException {
        this.cuota_mensual = calcularPagoMensual();
        setInteres(calcularInteresAcumulado());
        descontarCuota();
        if(fechaInicio.equals(LocalDate.now())) {
            setEstatus(0);
        }
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

    private void descontarCuota() throws SQLException{
        LocalDate fecha_final = LocalDate.now();
        LocalDate fecha_inicio = getFechaInicio();
        ResultSet rs = null;
        String consulta = "SELECT MAX(fechaInicio) AS fecha_mas_reciente FROM gastos WHERE idUsuario = '" + cuenta_bancaria.getIdUsuario() + "' AND idCuentaBancaria = '" + cuenta_bancaria.getId() + "' AND descripcion = 'Pago de Cuota Mensual del Prestamo " + getNombre() +"'";

        //Se hace la consulta para obtener la fecha más reciente de un descuento de cuota mensual del prestamo que se repite una vez al mes
        try{
            BaseDeDatos.establecerConexion();
            rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            if(rs.next()){
                String fecha_mas_reciente = rs.getString("fecha_mas_reciente");
                if (fecha_mas_reciente != null) {
                    fecha_inicio = LocalDate.parse(fecha_mas_reciente);
                }
            }
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }

        //Si la fecha inicio es igual al getFechaInicio quiere decir que la consulta no encontró una fecha más reciente de retiro y empezará a hacer retiros desde el primer registro del gasto hasta la fecha actual por mes. Se puede detener si la fecha de inicio es mayor que la fecha limite del pago del plazo
        while(fecha_inicio.isBefore(fecha_final) || fecha_inicio.isEqual(fecha_vencimiento)){

            fecha_inicio = fecha_inicio.plusMonths(1);
            //Si despues de sumarle un mes la fecha se pasa de la fecha actual entonces sale del ciclo
            if(fecha_inicio.isAfter(fecha_final)){
                break;
            }
            //Se crea el objeto que registra el ingreso automaticamente por el programa
            Gasto gasto = new Gasto("Pago Mensual de Prestamo", "Pago de Cuota Mensual del Prestamo " + getNombre(), getCuotaMensual(), fecha_inicio, cuenta_bancaria.getBanco(), 0, "Pago Prestamo", getCuentaBancaria());

            //Se guarda el ingreso repetido en la base de datos
            gasto.guardarGastoBaseDatos();
        }

    }

    public float calcularMontoPendiente(){
        float tasa_interes_mensual = getTasaInteres() / 12 / 100;
        int numeros_pagos = plazo * 12;
        LocalDate fecha_actual = LocalDate.now();

        // Calcular el número de pagos realizados
        long mesesTranscurridos = ChronoUnit.MONTHS.between(fechaInicio, fecha_actual);
        int pagosRealizados = (int) Math.min(mesesTranscurridos, numeros_pagos);

        // Calcular el saldo pendiente usando la fórmula
        float saldo_pendiente = getMontoOriginal() * (float) (
                (Math.pow(1 + tasa_interes_mensual, numeros_pagos) - Math.pow(1 + tasa_interes_mensual, pagosRealizados)) /
                        (Math.pow(1 + tasa_interes_mensual, numeros_pagos) - 1)
        );

        return saldo_pendiente;
    }

    public float calcularPagoMensual() {
        float saldo_pendiente = calcularSaldoPendiente();
        float tasa_interes_mensual = super.getTasaInteres() / 12 / 100;
        int numeros_pagos = plazo * 12;
        return (saldo_pendiente * tasa_interes_mensual) / (1 - (float)Math.pow(1 + tasa_interes_mensual, - numeros_pagos));
    }

    public Period calcularTiempoRestante() {
        Period diferencia = Period.between(fechaInicio, fecha_vencimiento);
        return diferencia;
    }

    public float calcularSaldoPendiente() {
        return saldo_pendiente - (cuota_mensual * plazo);
    }

    public float calcularInteresAcumulado() {
        LocalDate fecha_actual = LocalDate.now();
        long dias_transcurridos = ChronoUnit.DAYS.between(getFechaInicio(), getFechaVencimiento());
        return redonderCantidad((float) (getMontoOriginal() * (getTasaInteres()/100) * (dias_transcurridos / 365.0)));
    }

    public float calcularInteresTotal(){
        return redonderCantidad(getMontoOriginal()*(getTasaInteres()/100)* (getPlazo()/12));
    }

    public float calcularInteresPendiente() {
        float interes_acumulado = calcularInteresAcumulado();
        float interes_total = calcularInteresTotal();
        return redonderCantidad(interes_total - interes_acumulado);
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
