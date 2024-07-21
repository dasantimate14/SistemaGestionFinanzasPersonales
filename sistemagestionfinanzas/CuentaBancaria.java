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
    private String numero_cuenta;
    private String tipo_cuenta;
    private String id_usuario;
    public static int cantidad_instancias = 0;
    public static List<CuentaBancaria> intsancias_cuentas_bancarias = new ArrayList<>();

    //Constructor
    public CuentaBancaria(String nombre, String  descripcion, float montoOriginal, float tasaInteres, LocalDate fechaInicio, String banco, String numero_cuenta, String tipo_cuenta, String id_usuario) {
        super(nombre, descripcion, montoOriginal, "Activo", tasaInteres, fechaInicio);
        this.banco = banco;
        this.numero_cuenta = numero_cuenta;
        this.tipo_cuenta = tipo_cuenta;
        this.montoActual = montoOriginal;
        this.id_usuario = id_usuario;
        intsancias_cuentas_bancarias.add(this);
        cantidad_instancias++;
    }

    //Metodos get y set
    public String getBanco() {return this.banco;}
    public String getNumeroCuenta() {return this.numero_cuenta;}
    public String getTipoCuenta() {return this.tipo_cuenta;}
    public String getIdUsuario() {return this.id_usuario;}
    public void setBanco(String banco) {this.banco = banco;}
    public void setNumeroCuenta(String numero_cuenta) {this.numero_cuenta = numero_cuenta;}
    public void setTipoCuenta(String tipo_cuenta) {this.tipo_cuenta = tipo_cuenta;}
    public void setIdUsuario(String id_usuario) {this.id_usuario = id_usuario;}

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
        sb.append("Número de Cuenta: ").append(numero_cuenta).append("\n");
        sb.append("Tipo de Cuenta: ").append(tipo_cuenta).append("\n");
        return sb;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        float valor_total_cuentas_bancarias = 0;
        float valor_total_activos = 0;
        float porcentaje_representacion = 0;
        for (FinanceItem activo : activosPasivos) {
            valor_total_activos += activo.getMontoActual();
            if (activo instanceof CuentaBancaria) {
                CuentaBancaria cuenta = (CuentaBancaria) activo;
                valor_total_cuentas_bancarias += cuenta.getMontoActual();
            }
        }
        porcentaje_representacion = (valor_total_cuentas_bancarias / valor_total_activos)*100;
        System.out.println("El porcentaje de Representación de todaas las Cuentas Bancarias es " + porcentaje_representacion + " con un valor de " + valor_total_cuentas_bancarias);
    }

    //Calcula el promedio de los balances mensuales
    @Override
    protected float calcularPromedioMensual() throws SQLException, IOException {
        //Arreglo para guardar todos los balances mensuales
        List<Float> balances_mensuales = new ArrayList<>();
        float sumatoria_balances = 0;
        balances_mensuales = calcularBalancesMensualesRecientes();

        //Se suman los balances y se calcula el promedio
        for(float balance : balances_mensuales){
            sumatoria_balances += balance;
        }

        setPromedioMensual(redonderCantidad((sumatoria_balances)/ balances_mensuales.size()));
        return getPromedioMensual();
    }

    //Calcula el promedio de los balances anuales
    @Override
    protected float calcularPromedioAnual() throws IOException {
        float sumatoria_balances = 0;
        float promedio_anual;
        //Arreglo para guardar todos los balances mensuales
        List<Float> balances_anuales;
        balances_anuales = calcularBalancesAnualesRecientes();

        //Se hace la sumatoria y se calcula el promedio
        for(float balance : balances_anuales){
            sumatoria_balances += balance;
        }
        promedio_anual = redonderCantidad(sumatoria_balances/ balances_anuales.size()) ;
        return promedio_anual;
    }

    @Override
    public void actualizarInformacion() throws IOException {
        calcularValorActual();
        calcularInteresAcumulado();
        setGanaciaPerdida(calcularGanaciaPerdida());
    }

    //Metodo que registra el interes en la base de datos como un objeto Ingreso
    public void registrarInteres(){
        //Calcular el balance hasta el primero del mes anterior
        float balance_mensual = getMontoOriginal();
        float interes_mensual;
        LocalDate fecha_inicial;
        LocalDate fecha_actual = LocalDate.now();
        String consulta = "SELECT MAX(fechaInicio) AS fecha_mas_reciente FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND nombre = 'Interes'";
        ResultSet rs;
        LocalDate ultima_fecha_interes = null;

        //Se realiza la consulta a la base de datos
        try {
            BaseDeDatos.establecerConexion();
            rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            if(rs.next()){
                String fecha_mas_reciente = rs.getString("fecha_mas_reciente");
                ultima_fecha_interes =  fecha_mas_reciente != null ? LocalDate.parse(fecha_mas_reciente) : null;
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }

        //Si la ultima fecha es null entonces nunca se le han registrado los intereses a la cuenta. Se crean todos los intereses por mes desde la creacion de la cuenta hasta la fecha acutal
        if (ultima_fecha_interes == null){

            //Se establece un rango del primero de un mes al otro, representando el deposito de intereses el primero de cada mes
            fecha_inicial = getFechaInicio().withDayOfMonth(1);
            fecha_actual = fecha_actual.withDayOfMonth(1);
            while (fecha_inicial.isBefore(fecha_actual)){
                try {
                    balance_mensual = calcularBalanceMensual(String.valueOf(fecha_inicial), balance_mensual);
                    interes_mensual = calcularInteresSobreBalance(balance_mensual);
                    //Se mueve la fechaInicial al siguiente mes
                    fecha_inicial = fecha_inicial.plusMonths(1);


                    //Se crea un objeto ingreso para que registre el interes
                    guardarInteres(interes_mensual, fecha_inicial);

                    //Se actualiza el deposito del monto al valor actual de cuenta
                    depositarMonto(interes_mensual);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        //Si la ultima fecha es diferente de null entonces ya se han registrado depositos de interes antes y se empieza a registrar intereses apartir de este ultimo deposito
        } else {
            //Se establece el rango inferior de las fechas al ultimo deposito que se hizo
            fecha_inicial = ultima_fecha_interes;

            //Se establece un rango del primero de un mes al otro, representando el deposito de intereses el primero de cada mes
            fecha_inicial = fecha_inicial.withDayOfMonth(1);
            fecha_actual = fecha_actual.withDayOfMonth(1);

            //Se obtiene el balance del mes anterior utilizando la fechaInicial como limite superior de la funcion calcularBalancePrevio
            try {
                balance_mensual = calcularBalancePrevio(String.valueOf(fecha_inicial));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            while(fecha_inicial.isBefore(fecha_actual)){
                try{
                    balance_mensual = calcularBalanceMensual(String.valueOf(fecha_inicial), balance_mensual);
                    interes_mensual = calcularInteresSobreBalance(balance_mensual);

                    //Se mueve la fechaInicial al siguiente mes
                    fecha_inicial = fecha_inicial.plusMonths(1);

                    //Se crea un objeto ingreso y se registra en la base de datos
                    guardarInteres(interes_mensual, fecha_inicial);

                    //Se actualiza el deposito del monto al valor actual de cuenta
                    depositarMonto(interes_mensual);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Metodo para guardar interes en la base de datos
    public void guardarInteres(float interes_mensual, LocalDate fecha_deposito){
        //Consulta para guardar el objeto interes en la base de datos
        String consulta_registro = "INSERT INTO ingresos (id, nombre, descripcion, montoOriginal, fechaInicio, fuente, idUsuario, idCuentaBancaria) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?)";

        //Objeto ingreso que se guardará en la base de datos
        Ingreso ingreso = new Ingreso("Interes", "Interes mensual generado por el " + getBanco(), interes_mensual, fecha_deposito, getBanco(), this);

        //Arreglo con los parametros de la consulta
        String[] parametros = new String[]{ingreso.getNombre(), ingreso.getDescripcion(), String.valueOf(ingreso.getMontoOriginal()), String.valueOf(ingreso.getFechaInicio()), ingreso.getFuente(), getIdUsuario(), getId()};

        //Registro en la base de datos
        try{
            BaseDeDatos.establecerConexion();
            boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registro_exitoso){
                System.out.println("Registro exitoso de Interes." );
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }

    }

    //Metodo que aplica la ecuacion del interes
    public float calcularInteresSobreBalance(float balance_mensual){
        float interes = (balance_mensual *((getTasaInteres()/100)/12));
        return redonderCantidad(interes);
    }

    //Metodo para obtener el balance para un mes en especifico
    public float calcularBalanceMensual(String fechaInicial, float balance_anterior) throws SQLException {
        String consulta_ingreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fechaInicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fechaInicial) + "', INTERVAL 1 MONTH)";
        String consulta_retiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fechaInicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fechaInicial) + "', INTERVAL 1 MONTH)";
        float ingreso_total = 0;
        float retiro_total = 0;
        float balance_mensual;

        //Se realizan las consultas a las tablas ingresos y retiros para obtener las respectivas sumatorias y calcular el balance
        BaseDeDatos.establecerConexion();
        ResultSet rs_ingreso = BaseDeDatos.realizarConsultaSelectInterna(consulta_ingreso);
        if(rs_ingreso.next()){
            ingreso_total = rs_ingreso.getFloat("ingreso_total");
            rs_ingreso.close();
        }

        ResultSet rs_retiro = BaseDeDatos.realizarConsultaSelectInterna(consulta_retiros);
        if(rs_retiro.next()){
            retiro_total = rs_retiro.getFloat("retiro_total");
            rs_retiro.close();
        }
        BaseDeDatos.cerrarConexion();

        //Se calcula el balance mensual tomando el cuenta el balance del mes anterior y los ingresos y retiros de un mes
        balance_mensual = (balance_anterior + ingreso_total) - retiro_total;
        return balance_mensual;
    }

    public void depositarMonto(float monto){
        setMontoActual(getMontoActual() + monto);
    }

    public void retirarMonto(float monto){
        setMontoActual(getMontoActual() - monto);
    }

    public float calcularBalanceActual() throws SQLException {
        float balance_actual;
        String consulta_ingreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "'";
        String consulta_retiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "'";
        float ingreso_total = 0;
        float retiro_total = 0;

        //Se realizan las consultas a las tablas ingresos y retiros para obtener las respectivas sumatorias y calcular el balance
        BaseDeDatos.establecerConexion();
        ResultSet rs_ingreso = BaseDeDatos.realizarConsultaSelectInterna(consulta_ingreso);
        if(rs_ingreso.next()){
            ingreso_total = rs_ingreso.getFloat("ingreso_total");
            rs_ingreso.close();
        }

        ResultSet rs_retiro = BaseDeDatos.realizarConsultaSelectInterna(consulta_retiros);
        if(rs_retiro.next()){
            retiro_total = rs_retiro.getFloat("retiro_total");
            rs_retiro.close();
        }
        BaseDeDatos.cerrarConexion();

        //Se calcula el balance actual tomando el monto original de la cuenta y los ingresos y retiros totales
        balance_actual = (getMontoOriginal() + ingreso_total) - retiro_total;
        return balance_actual;
    }

    //Metodo para calcular el balance previo a la fecha del parametro
    public float calcularBalancePrevio(String fecha_final) throws SQLException {
        float balance_mes_anterior;
        String consulta_ingreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(getFechaInicio()) + "' AND '" + java.sql.Date.valueOf(fecha_final) +"'";
        String consutla_retiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(getFechaInicio()) + "' AND '" + java.sql.Date.valueOf(fecha_final) +"'";
        float ingreso_total = 0;
        float retiro_total = 0;

        //Se realizan las consultas a las tablas ingresos y retiros para obtener las respectivas sumatorias y calcular el balance
        BaseDeDatos.establecerConexion();
        ResultSet rs_ingreso = BaseDeDatos.realizarConsultaSelectInterna(consulta_ingreso);
        if(rs_ingreso.next()){
            ingreso_total = rs_ingreso.getFloat("ingreso_total");
            rs_ingreso.close();
        }

        ResultSet rs_retiro = BaseDeDatos.realizarConsultaSelectInterna(consutla_retiros);
        if(rs_retiro.next()){
            retiro_total = rs_retiro.getFloat("retiro_total");
            rs_retiro.close();
        }
        BaseDeDatos.cerrarConexion();

        //Se calcula el balance mensual tomando el cuenta el balance del mes anterior y los ingresos y retiros de un mes
        balance_mes_anterior = (getMontoOriginal() + ingreso_total) - retiro_total;
        return balance_mes_anterior;
    }

    //Metodo que calcula la suma total de intereses obtenidos por la cuenta bancaria
    public void calcularInteresAcumulado(){
        float interes_acumulado = 0;
        String consulta = "SELECT SUM(montoOriginal) AS interes_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "'" + " AND idCuentaBancaria = '" + getId() + "' AND nombre = 'Interes'";
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            if(rs.next()){
                interes_acumulado = rs.getFloat("interes_total");
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            BaseDeDatos.cerrarConexion();
        }
        setInteres(interes_acumulado);

    }

    //Metodo para calcular el balance de un año específico
    public float calcularBalanceAnual(String fecha_inicial, float balance_anterior) throws SQLException {
        String consulta_ingreso = "SELECT SUM(montoOriginal) AS ingreso_total FROM ingresos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fecha_inicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fecha_inicial) + "', INTERVAL 1 YEAR)";
        String consulta_retiros = "SELECT SUM(montoOriginal) AS retiro_total FROM gastos WHERE idUsuario = '" + getIdUsuario() + "' AND idCuentaBancaria = '" + getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fecha_inicial) + "' AND DATE_ADD('" + java.sql.Date.valueOf(fecha_inicial) + "', INTERVAL 1 YEAR)";
        float ingreso_total = 0;
        float retiro_total = 0;
        float balance_anual;

        //Se realizan las consultas a las tablas ingresos y retiros para obtener las respectivas sumatorias y calcular el balance
        BaseDeDatos.establecerConexion();
        ResultSet rs_ingreso = BaseDeDatos.realizarConsultaSelectInterna(consulta_ingreso);
        if(rs_ingreso.next()){
            ingreso_total = rs_ingreso.getFloat("ingreso_total");
            rs_ingreso.close();
        }

        ResultSet rs_retiro = BaseDeDatos.realizarConsultaSelectInterna(consulta_retiros);
        if(rs_retiro.next()){
            retiro_total = rs_retiro.getFloat("retiro_total");
            rs_retiro.close();
        }
        BaseDeDatos.cerrarConexion();

        //Se calcula el balance anual tomando el cuenta el balance del año anterior y los ingresos y retiros del año
        balance_anual = (balance_anterior + ingreso_total) - retiro_total;
        return balance_anual;
    }

    //Metodo que obtiene los balances por mes del año más reciente
    public List<Float> calcularBalancesMensualesRecientes(){
        //La fecha inicial debe ser un año atrás para que se calcule el promedio mensual más reciente
        List<Float> balances_mensuales = new ArrayList<>();
        LocalDate fecha_actual = LocalDate.now();
        LocalDate fecha_inicial = fecha_actual.minusYears(1);
        float balance_mensual;

        //Se obtiene el balance previo al año de los promedios que se calcularan
        try {
            balance_mensual = calcularBalancePrevio(String.valueOf(fecha_inicial));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Se calculan los balances para cada mes y se guardan en un arrelgo
        while(fecha_inicial.isBefore(fecha_actual)){
            try{
                balance_mensual = calcularBalanceMensual(String.valueOf(fecha_inicial), balance_mensual);
                balances_mensuales.add(balance_mensual);
                fecha_inicial = fecha_inicial.plusMonths(1);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return balances_mensuales;
    }

    //Metodo para calcular todos los balances anuales desde 5 años previos a la fecha actual
    public List<Float> calcularBalancesAnualesRecientes(){
        //La fecha inicial debe ser cinco año atrás para que se calcule el promedio de los años más recientes
        LocalDate fecha_actual = LocalDate.now();
        LocalDate fecha_inicial = fecha_actual.minusYears(5);
        float balance_anual = 0;

        //Arreglo para guardar todos los balances mensuales
        List<Float> balances_anuales = new ArrayList<>();

        //Se obtiene el balance previo a los 5 años de los promedios que se calcularan
        try {
            balance_anual = calcularBalancePrevio(String.valueOf(fecha_inicial));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Se calculan los balances para cada año y se guardan en un arrelgo
        while(fecha_inicial.isBefore(fecha_actual)){
            try{
                balance_anual = calcularBalanceAnual(String.valueOf(fecha_inicial), balance_anual);
                balances_anuales.add(balance_anual);
                fecha_inicial = fecha_inicial.plusYears(1);

            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return balances_anuales;
    }

    //Metodo para encontrar el valor de esta cuenta bancaria dentro de todas las cuentas bancarias
    public void calcularPorcentajeRepresentacionCuenta(){
        float total_cuentas = 0;
        float porcentaje_representacion = 0;
        for(CuentaBancaria cuenta : intsancias_cuentas_bancarias){
            total_cuentas += cuenta.getMontoActual();
        }
        porcentaje_representacion = (getMontoActual() / total_cuentas) * 100;
        System.out.println("El porcentaje representacion es " + redonderCantidad(porcentaje_representacion) + "% con un valor de " + getMontoActual());
    }

    //Metodo para guardar una cuenta bancaria en la base de datos
    public void guardarCuentaBancariaBaseDatos(){
        //Consulta para guardar el objeto interes en la base de datos
        String consulta_registro = "INSERT INTO cuentas_bancarias (id, nombre, descripcion, montoOriginal, fechaInicio, tasaInteres, banco, numeroCuenta, tipoCuenta, idUsuario) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Arreglo con los parametros de la consulta
        String[] parametros = {getNombre(), getDescripcion(), String.valueOf(getMontoOriginal()), String.valueOf(getFechaInicio()), String.valueOf(getTasaInteres()), getBanco(), String.valueOf(getNumeroCuenta()), getTipoCuenta(), getIdUsuario()};

        //Registro en la base de datos
        try{
            BaseDeDatos.establecerConexion();
            boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registro_exitoso){
                System.out.println("Registro exitoso de Cuenta Bancaria." );
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }

    }

    public static void obtenerCuentasBancariasBaseDatos(String id_usuario) throws SQLException {
        CuentaBancaria cuenta_bancaria = null;
        String consulta = "SELECT * FROM cuentas_bancarias WHERE idUsuario = ?";
        String[] parametros = {id_usuario};
        try{
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);
            if (rs == null){
                throw new SQLException("No se puede obtener una cuenta Bancaria.");
            }
            while (rs.next()) {
                //Se leen cada uno de los campos en el resultset para crear el objeto
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                float monto_original = rs.getFloat("montoOriginal");
                float tasa_interes = rs.getFloat("tasaInteres");
                LocalDate fecha_inicio = rs.getDate("fechaInicio").toLocalDate();
                String banco = rs.getString("banco");
                String numero_cuenta = rs.getString("numeroCuenta");
                String tipo_cuenta = rs.getString("tipoCuenta");

                //Se crea el objeto con los datos capturados
                cuenta_bancaria = new CuentaBancaria(nombre, descripcion, monto_original, tasa_interes, fecha_inicio, banco, numero_cuenta, tipo_cuenta, id_usuario);
                cuenta_bancaria.setId(id);


            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            BaseDeDatos.cerrarConexion();
        }

    }


}
