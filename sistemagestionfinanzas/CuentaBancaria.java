package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria extends FinanceItem{
    //Declaración de atributos
    private String banco;
    private int numeroCuenta;
    private String tipoCuenta;
    private String idUsuario;
    private static int cantidadInstancias;
    private static List<CuentaBancaria> instanciasCuentasBancarias;

    //Constructor
    public CuentaBancaria(String nombre, String  descripcion, float montoOriginal, String tipo, float tasaInteres, LocalDate fechaInicio, String banco, int numeroCuenta, String tipoCuenta, String idUsuario) {
        super(nombre, descripcion, montoOriginal, tipo, tasaInteres, fechaInicio);
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.montoActual = montoOriginal;
        this.idUsuario = idUsuario;
        instanciasCuentasBancarias.add(this);
        cantidadInstancias++;
    }

    //Metodos get y set
    public String getBanco() {return this.banco;}
    public int getNumeroCuenta() {return this.numeroCuenta;}
    public String getTipoCuenta() {return this.tipoCuenta;}
    public String getIdUsuario() {return this.idUsuario;}
    public void setBanco(String banco) {this.banco = banco;}
    public void setNumeroCuenta(int numeroCuenta) {this.numeroCuenta = numeroCuenta;}
    public void setTipoCuenta(String tipoCuenta) {this.tipoCuenta = tipoCuenta;}
    public void setIdUsuario(String idUsuario) {this.idUsuario = idUsuario;}

    //Devuelve el valor calculado por calcularBalanceActual
    @Override
    protected float calcularValorActual() throws IOException {
        registrarInteres();
        try {
            setMontoActual(calcularBalanceActual());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        //Arreglo para guardar todos los balances mensuales
        List<Float> balancesMensuales = new ArrayList<>();
        float sumatoriaBalances = 0;
        balancesMensuales = calcularBalancesMensualesRecientes();

        //Se suman los balances y se calcula el promedio
        for(float balance : balancesMensuales){
            sumatoriaBalances += balance;
        }
        setPromedioMensual((sumatoriaBalances)/balancesMensuales.size());
        return getPromedioMensual();
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        float sumatoriaBalances = 0;
        float promedioAnual;
        //Arreglo para guardar todos los balances mensuales
        List<Float> balancesAnuales = new ArrayList<>();
        balancesAnuales = calcularBalancesAnualesRecientes();

        //Se hace la sumatoria y se calcula el promedio
        for(float balance : balancesAnuales){
            sumatoriaBalances += balance;
        }
        promedioAnual = sumatoriaBalances/ balancesAnuales.size();
        return promedioAnual;
    }

    @Override
    protected void actualizarInformacion() throws IOException {
        calcularValorActual();
        calcularInteresAcumulado();
        setGanaciaPerdida(calcularGanaciaPerdida());
    }

    //Metodo que registra el interes en la base de datos como un objeto Ingreso
    public void registrarInteres(){
        //Calcular el balance hasta el primero del mes anterior
        float balanceMensual = getMontoOriginal();
        float interesMensual = 0;
        LocalDate fechaInicial;
        LocalDate fechaActual = LocalDate.now();
        String consulta = "SELECT MAX(fechaInicio) AS fecha_mas_reciente FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND nombre = 'Interes'";
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

        //Si la ultima fecha es null entonces nunca se le han registrado los intereses a la cuenta. Se crean todos los intereses por mes desde la creacion de la cuenta hasta la fecha acutal
        if (ultimaFechaInteres == null){
            fechaInicial = getFechaInicio();

            //Se establece un rango del primero de un mes al otro, representando el deposito de intereses el primero de cada mes
            fechaInicial = fechaInicial.withDayOfMonth(1);
            fechaActual = fechaActual.withDayOfMonth(1);
            while (fechaInicial.isBefore(fechaActual)){
                try {
                    balanceMensual = calcularBalanceMensual(String.valueOf(fechaInicial), balanceMensual);
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
        } else {
            //Se establece el rango inferior de las fechas al ultimo deposito que se hizo
            fechaInicial = ultimaFechaInteres;

            //Se establece un rango del primero de un mes al otro, representando el deposito de intereses el primero de cada mes
            fechaInicial = fechaInicial.withDayOfMonth(1);
            fechaActual = fechaActual.withDayOfMonth(1);

            //Se obtiene el balance del mes anterior utilizando la fechaInicial como limite superior de la funcion calcularBalancePrevio
            try {
                balanceMensual = calcularBalancePrevio(String.valueOf(fechaInicial));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            while(fechaInicial.isBefore(fechaActual)){
                try{
                    balanceMensual = calcularBalanceMensual(String.valueOf(fechaInicial), balanceMensual);
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
    public float calcularBalanceMensual(String fechaInicial, float balanceAnterior) throws SQLException {
        String consultaIngreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fechaInicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fechaInicial) + "', INTERVAL 1 MONTH)";
        String consultaRetiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fechaInicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fechaInicial) + "', INTERVAL 1 MONTH)";
        float ingrestoTotal = 0;
        float retiroTotal = 0;
        float balanceMensual;

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

    public float calcularBalanceActual() throws SQLException {
        float balanceActual;
        String consultaIngreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "'";
        String consultaRetiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "'";
        float ingrestoTotal = 0;
        float retiroTotal = 0;

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

        //Se calcula el balance actual tomando el monto original de la cuenta y los ingresos y retiros totales
        balanceActual = (getMontoOriginal() + ingrestoTotal) - retiroTotal;
        return balanceActual;
    }

    //Metodo para calcular el balance previo a la fecha del parametro
    public float calcularBalancePrevio(String fechaFinal) throws SQLException {
        float balanceMesAnterior;
        String consultaIngreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(getFechaInicio()) + "' AND '" + java.sql.Date.valueOf(fechaFinal) +"'";
        String consultaRetiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(getFechaInicio()) + "' AND '" + java.sql.Date.valueOf(fechaFinal) +"'";
        float ingrestoTotal = 0;
        float retiroTotal = 0;

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
        balanceMesAnterior = (getMontoOriginal() + ingrestoTotal) - retiroTotal;
        return balanceMesAnterior;
    }

    //Metodo que calcula la suma total de intereses obtenidos por la cuenta bancaria
    public void calcularInteresAcumulado(){
        float interesAcumulado = 0;
        String consulta = "SELECT SUM(montoOriginal) AS interes_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "'" + " AND idCuentaBancaria = '" + getId() + "' AND nombre = 'Interes'";
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            if(rs != null){
                interesAcumulado = rs.getFloat("interes_total");
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            BaseDeDatos.cerrarConexion();
        }
        setInteres(interesAcumulado);

    }

    //Metodo para calcular el balance de un año específico
    public float calcularBalanceAnual(String fechaInicial, float balanceAnterior) throws SQLException {
        String consultaIngreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fechaInicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fechaInicial) + "', INTERVAL 1 YEAR)";
        String consultaRetiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fechaInicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fechaInicial) + "', INTERVAL 1 YEAR)";
        float ingrestoTotal = 0;
        float retiroTotal = 0;
        float balanceAnual = 0;

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

        //Se calcula el balance anual tomando el cuenta el balance del año anterior y los ingresos y retiros del año
        balanceAnual = (balanceAnterior + ingrestoTotal) - retiroTotal;
        return balanceAnual;
    }

    //Metodo que obtiene los balances por mes del año más reciente
    public List<Float> calcularBalancesMensualesRecientes(){
        //La fecha inicial debe ser un año atrás para que se calcule el promedio mensual más reciente
        List<Float> balancesMensuales = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicial = fechaActual.minusYears(1);
        float balanceMensual;

        //Se obtiene el balance previo al año de los promedios que se calcularan
        try {
            balanceMensual = calcularBalancePrevio(String.valueOf(fechaInicial));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Se calculan los balances para cada mes y se guardan en un arrelgo
        while(fechaInicial.isBefore(fechaActual)){
            try{
                balanceMensual = calcularBalanceMensual(String.valueOf(fechaInicial), balanceMensual);
                balancesMensuales.add(balanceMensual);
                fechaInicial = fechaInicial.plusMonths(1);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return balancesMensuales;
    }

    //Metodo para calcular todos los balances anuales desde 5 años previos a la fecha actual
    public List<Float> calcularBalancesAnualesRecientes(){
        //La fecha inicial debe ser cinco año atrás para que se calcule el promedio de los años más recientes
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicial = fechaActual.minusYears(5);
        float balanceAnual = 0;

        //Arreglo para guardar todos los balances mensuales
        List<Float> balancesAnuales = new ArrayList<>();

        //Se obtiene el balance previo a los 5 años de los promedios que se calcularan
        try {
            balanceAnual = calcularBalancePrevio(String.valueOf(fechaInicial));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Se calculan los balances para cada año y se guardan en un arrelgo
        while(fechaInicial.isBefore(fechaActual)){
            try{
                balanceAnual = calcularBalanceAnual(String.valueOf(fechaInicial), balanceAnual);
                balancesAnuales.add(balanceAnual);
                fechaInicial = fechaInicial.plusYears(1);

            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return balancesAnuales;
    }

}
