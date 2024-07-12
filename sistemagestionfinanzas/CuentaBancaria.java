package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CuentaBancaria extends FinanceItem{
    //Declaración de atributos
    private String banco;
    private int numeroCuenta;
    private String tipoCuenta;
    private static int cantidadInstancias;
    private static List<CuentaBancaria> instanciasCuentasBancarias;

    //Constructor
    CuentaBancaria(String nombre, String  descripcion, float montoOriginal, String tipo, float tasaInteres, LocalDate fechaInicio, String banco, int numeroCuenta, String tipoCuenta) {
        super(nombre, descripcion, montoOriginal, tipo, tasaInteres, fechaInicio);
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.montoActual = montoOriginal;
    }

    //Metodos get y set
    public String getBanco() {return this.banco;}
    public int getNumeroCuenta() {return this.numeroCuenta;}
    public String getTipoCuenta() {return this.tipoCuenta;}
    public void setBanco(String banco) {this.banco = banco;}
    public void setNumeroCuenta(int numeroCuenta) {this.numeroCuenta = numeroCuenta;}
    public void setTipoCuenta(String tipoCuenta) {this.tipoCuenta = tipoCuenta;}

    //Hace un balance desde que se creo la cuenta hasta el dia actual
    @Override
    protected float calcularValorActual() throws IOException {

        return getMontoActual();
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre Banco: ").append(banco).append("\n");
        sb.append("Número de Cuenta: ").append(numeroCuenta).append("\n");
        sb.append("Tipo de Cuenta: ").append(tipoCuenta).append("\n");
        return sb;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        float valorTotalCuentasBancarias = 0;
        float valorTotalActivos = 0;
        float porcentajeRepresentacion = 0;
        for (FinanceItem activo : activosPasivos) {
            valorTotalActivos += activo.getMontoActual();
            if (activo instanceof CuentaBancaria) {
                CuentaBancaria cuenta = (CuentaBancaria) activo;
                valorTotalCuentasBancarias += cuenta.getMontoActual();
            }
        }
        porcentajeRepresentacion = (valorTotalCuentasBancarias / valorTotalActivos)*100;
        System.out.println("El porcentaje de Representación de todaas las Cuentas Bancarias es " + porcentajeRepresentacion + " con un valor de " + valorTotalCuentasBancarias);
    }

    @Override
    protected float calcularPromedioMensual() throws SQLException, IOException {
        return 0;
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        return 0;
    }

    @Override
    protected void actualizarInformacion() throws IOException {

    }

    //Metodo que registra el interes en la base de datos como un objeto Ingreso
    public void registrarInteres(String idUsuario){
        //Calcular el balance hasta el primero del mes anterior
        float balanceMensual = getMontoOriginal();
        float interesMensual = 0;
        LocalDate fechaInicial;
        LocalDate fechaActual = LocalDate.now();
        String consulta = "SELECT MAX(fecha) AS fecha_mas_reciente FROM ingresos WHERE idUsuario = '" + idUsuario + "' AND idCuentaBancaria = '" + this.id + "' AND nombre = 'Interes'";
        ResultSet rs = null;
        LocalDate ultimaFechaInteres = null;

        //Se realiza la consulta a la base de datos
        try {
            BaseDeDatos.establecerConexion();
            rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            ultimaFechaInteres = LocalDate.parse(rs.getString("fecha_mas_reciente"));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }

        //Si la ultima fecha es null entonces nunca se le han registrado los intereses a la cuenta. Se crean todos los intereses por mes desde la creacion de la cuenta hasta el dia acutal
        if (ultimaFechaInteres == null){
            fechaInicial = getFechaInicio();

            //Se establece un rango del primero de un mes al otro, representando el deposito de intereses el primero de cada mes
            fechaInicial = fechaInicial.withDayOfMonth(1);
            fechaActual = fechaActual.withDayOfMonth(1);
            while (fechaInicial.isBefore(fechaActual)){
                try {
                    balanceMensual = calcularBalanceMensual(idUsuario, String.valueOf(fechaInicial), balanceMensual);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                interesMensual = calcularInteresSobreBalance(balanceMensual);
                //Se mueve la fechaInicial al siguiente mes
                fechaInicial = fechaInicial.plusMonths(1);

                //Se crea un objeto ingreso para que registre el interes

                //Se actualiza el deposito del monto al valor actual de cuenta
                depositarMonto(interesMensual);

            }

        //Si la ultima fecha es diferente de null entonces ya se han registrado depositos de interes antes y se empieza a registrar intereses apartir de este ultimo deposito
        } else if (ultimaFechaInteres != null) {
            //Se establece el rango inferior de las fechas al ultimo deposito que se hizo
            fechaInicial = ultimaFechaInteres;

            //Se obtiene el balance del mes anterior
            LocalDate fechaBalanceDelMesAnterior = fechaInicial.minusMonths(1).withDayOfMonth(1);

            //Se establece un rango del primero de un mes al otro, representando el deposito de intereses el primero de cada mes
            fechaInicial = fechaInicial.withDayOfMonth(1);
            fechaActual = fechaActual.withDayOfMonth(1);

            while(fechaInicial.isBefore(fechaActual)){
                try{
                    balanceMensual = calcularBalanceMensual(idUsuario, String.valueOf(fechaInicial), balanceMensual);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                interesMensual = calcularInteresSobreBalance(balanceMensual);

                //Se mueve la fechaInicial al siguiente mes
                fechaInicial = fechaInicial.plusMonths(1);

                //Se crea un objeto ingreso para que registre el interes

                //Se actualiza el deposito del monto al valor actual de cuenta
                depositarMonto(interesMensual);
            }
        }
    }

    //Metodo que aplica la ecuacion del interes
    public float calcularInteresSobreBalance(float balanceMensual){
        float interesMensual = 0;
        interesMensual = balanceMensual*((this.interes/100)/12);
        return interesMensual;
    }

    //Metodo para obtener el balance para un mes en especifico
    public float calcularBalanceMensual(String idUsuario, String fechaInicial, float balanceAnterior) throws SQLException {
        String consultaIngreso = "SELECT SUM(monto) AS ingreso_total FROM ingresos WHERE idUsuario = '" + idUsuario + "' AND idCuentaBancaria = '" + this.id + "' AND fechaInicio BETWEEN '" + fechaInicial + "' AND DATE_ADD('" + fechaInicial + "', INTERVAL 1 MONTH)";
        String consultaRetiros = "SELECT SUM(monto) AS retiro_total FROM gastos WHERE idUsuario = '" + idUsuario + "' AND idCuentaBancaria = '" + this.id + "' AND fechaInicio BETWEEN '" + fechaInicial + "' AND DATE_ADD('" + fechaInicial + "', INTERVAL 1 MONTH)";
        float ingrestoTotal = 0;
        float retiroTotal = 0;
        float balanceMensual = 0;

        //Se realizan las consultas a las tablas ingresos y retiros para obtener las respectivas sumatorias y calcular el balance
        BaseDeDatos.establecerConexion();
        ResultSet rsIngreso = BaseDeDatos.realizarConsultaSelectInterna(consultaIngreso);
        if(rsIngreso != null){
            ingrestoTotal = rsIngreso.getFloat("ingreso_total");
            rsIngreso.close();
        }

        ResultSet rsRetiro = BaseDeDatos.realizarConsultaSelectInterna(consultaRetiros);
        if(rsRetiro != null){
            retiroTotal = rsRetiro.getFloat("retiro_total");
            rsRetiro.close();
        }
        BaseDeDatos.cerrarConexion();

        //Se calcula el balance mensual tomando el cuenta el balance del mes anterior y los ingresos y retiros de un mes
        balanceMensual = (balanceAnterior + ingrestoTotal) - retiroTotal;
        return balanceMensual;
    }

    public void depositarMonto(float monto){
        setMontoActual(getMontoActual() + monto);
    }

    public void retirarMonto(float monto){
        setMontoActual(getMontoActual() - monto);
    }

    public float calcularBalanceActual(String idUsuario){
        float balanceActual = 0;



        return balanceActual;
    }

    public float calcularBalanceMesAnterior(String idUsuario, String fechaInicial) throws SQLException {
        float balanceMesAnterior = 0;
        return balanceMesAnterior;
    }


}
