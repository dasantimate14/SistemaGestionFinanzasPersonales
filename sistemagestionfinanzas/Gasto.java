package sistemagestionfinanzas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Gasto extends FinanceItem {
    private String acreedor;
    private int frecuencia;
    private float sumatoria_pagos;
    private String categoria_gasto;
    private boolean estatus;
    private int estatus_entero;
    private CuentaBancaria cuenta;
    public static int cantidad_instancias = 0;
    public static List<Gasto> instancias_gastos = new ArrayList<>();

    public Gasto(String nombre, String descripcion, float montoOriginal, LocalDate fechaInicio,
                 String acreedor, int frecuencia, String categoriaGasto, CuentaBancaria cuenta) {
        super(nombre, descripcion, montoOriginal, "Pasivo", 0, fechaInicio);
        this.acreedor = acreedor;
        this.frecuencia = frecuencia;
        this.categoria_gasto = categoriaGasto;
        if(frecuencia != 0){
            this.estatus = true;
        } else {
            this.estatus = false;
        }
        this.cuenta = cuenta;
        instancias_gastos.add(this);
        cantidad_instancias++;
    }

    public String getAcreedor() {
        return acreedor;
    }
    public int getFrecuencia() {return this.frecuencia;}
    public void setFrecuencia(int frecuencia) {this.frecuencia = frecuencia;}

    public float getSumatoria_pagos() {return sumatoria_pagos;}
    public void setSumatoria_pagos(float sumatoria_pagos) {this.sumatoria_pagos = sumatoria_pagos;}

    public void setAcreedor(String nuevoAcreedor) {
        this.acreedor = nuevoAcreedor;
    }

    public float getSumatoriaPagos() {
        return sumatoria_pagos;
    }

    public void setSumatoriaPagos(float nueva_sumatoria_pagos) {
        this.sumatoria_pagos = nueva_sumatoria_pagos;
    }

    public String getCategoriaGasto() {
        return categoria_gasto;
    }

    public void setCategoriaGasto(String categoria_gasto) {
        this.categoria_gasto = categoria_gasto;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean nuevoEstatus) {
        this.estatus = nuevoEstatus;
    }

    public CuentaBancaria getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaBancaria cuenta) {
        this.cuenta = cuenta;
    }

    public int getEstatus_entero() {return this.estatus_entero;}
    public void setEstatus_entero(int estatus_entero) {this.estatus_entero = estatus_entero;}


    @Override
    protected float calcularValorActual() {
        return getMontoOriginal();
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder info = new StringBuilder();
        info.append("Acreedor: ").append(acreedor).append("\n");
        info.append("Frecuencia: ").append(frecuencia).append("\n");
        info.append("Categoría de Gasto: ").append(categoria_gasto).append("\n");
        info.append("Estatus: ").append(estatus ? "Activo" : "Inactivo").append("\n");
        info.append("Cuenta Bancaria: ").append(cuenta != null ? cuenta.toString() : "N/A").append("\n");
        return info;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
    }

    @Override
    protected float calcularPromedioMensual() {
        return 0;
    }

    @Override
    protected float calcularPromedioAnual() {
        return 0;
    }

    @Override
    public void actualizarInformacion() {
        if(getFrecuencia() != 0){
            LocalDate fecha_final = LocalDate.now();
            LocalDate fecha_inicio = getFechaInicio();
            ResultSet rs = null;
            String[] parametros = new String[8];
            String consulta = "SELECT MAX(fechaInicio) AS fecha_mas_reciente FROM gastos WHERE idUsuario = '" + cuenta.getIdUsuario() + "' AND idCuentaBancaria = '" + cuenta.getId() + "' AND nombre = '" + getNombre() + "'";

            //Se hace la consulta para obtener la fecha más reciente de registro de un gasto que se repite por meses según la frecuencia
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

            //Si la fecha inicio es igual al getFechaInicio quiere decir que la consulta no encontró una fecha más reciente de deposito y empezará a hacer depositos desde el primer registro del ingreso hasta la fecha actual según la frecuecia.
            while(fecha_inicio.isBefore(fecha_final)){

                //Se crea el objeto que registra el ingreso automaticamente por el programa
                fecha_inicio = fecha_inicio.plusMonths(getFrecuencia());
                //Si despues de sumarle un mes la fecha se pasa de la fecha actual entonces sale del ciclo
                if(fecha_inicio.isAfter(fecha_final)){
                    break;
                }
                Gasto gasto = new Gasto(getNombre(), getDescripcion(), getMontoOriginal(), fecha_inicio, getAcreedor(), 0, getCategoriaGasto(), getCuenta());
                cuenta.retirarMonto(gasto.montoOriginal);

                //Se guarda el ingreso repetido en la base de datos
                gasto.guardarGastoBaseDatos();
            }
        }
    }
    public int obtenerEstatusEntero(){
        if(getEstatus()){
            setEstatus_entero(1);
        } else {
            setEstatus_entero(0);
        }
        return getEstatus_entero();
    }


    public void guardarGastoBaseDatos() {
        String consulta_registro = "INSERT INTO gastos (id, nombre, descripcion, montoOriginal, tipo, fechaInicio, acreedor, frecuencia, categoriaGasto, estatus, idUsuario, idCuentaBancaria) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String[] parametros = new String[]{
                getNombre(),
                getDescripcion(),
                String.valueOf(getMontoOriginal()),
                getTipo(),
                String.valueOf(getFechaInicio()),
                getAcreedor(),
                String.valueOf(frecuencia),
                getCategoriaGasto(),
                String.valueOf(obtenerEstatusEntero()),
                getIdUsuario(),
                getIdCuentaBancaria()
        };

        try {
            BaseDeDatos.establecerConexion();
            boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registro_exitoso) {
                System.out.println("Registro exitoso de gasto.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }
    public static void obtenerGastoBaseDatos(String id_usuario) throws SQLException {
        Gasto gasto = null;
        String consulta = "SELECT * FROM gastos WHERE idUsuario = ?";
        String[] parametro = {id_usuario};
        try{
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametro);
            if(rs == null){
                throw new SQLException("No se pudo obtener ningún ingresos para este usuario");
            }
            while (rs.next()) {
                //Se leen cada uno de los campos en el resultset para crear el objeto
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                float monto_original = rs.getFloat("montoOriginal");
                LocalDate fecha_inicio = rs.getDate("fechaInicio").toLocalDate();
                String acreedor = rs.getString("acreedor");
                int frecuencia = rs.getInt("frecuencia");
                String categoria_gasto = rs.getString("categoriaGasto");
                boolean estatus = rs.getBoolean("estatus");
                String id_cuenta_bancaria = rs.getString("idCuentaBancaria");

                CuentaBancaria cuenta_viculada = null;
                for(CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                    if(cuenta.getId().equals(id_cuenta_bancaria)) {
                        cuenta_viculada = cuenta;
                        //Se crea el objeto con los datos capturados
                        gasto = new Gasto(nombre, descripcion, monto_original, fecha_inicio, acreedor, frecuencia, categoria_gasto, cuenta_viculada);
                        gasto.setId(id);
                        gasto.setEstatus(estatus);
                        System.out.println("Gasto obtenido Correctamente");
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

    private String getIdUsuario() {
        return cuenta != null ? cuenta.getIdUsuario() : null;
    }

    private String getIdCuentaBancaria() {
        return cuenta != null ? cuenta.getId() : null;
    }

    public void calculaPorcentajeRepresentacionGasto(String nombre){
        float sumatoria_gasto = 0;
        float gastos_totales = 0;
        float porcentaje_representacion = 0;
        for(Gasto gsasto : instancias_gastos){
            gastos_totales += gsasto.montoOriginal;
            if(gsasto.getNombre().equals(nombre)){
                sumatoria_gasto += gsasto.montoOriginal;
            }
        }
        //Calcular porcentaje representación
        porcentaje_representacion = redonderCantidad((float) (sumatoria_gasto / gastos_totales)*100);
        System.out.println("El porcentaje de representacion de "+ nombre + " es " + porcentaje_representacion +" con un total de " + redonderCantidad(sumatoria_gasto));
    }

    //Método para calcular el promedio mensual de todos los gastos
    public static float calcularPromedioMensualGasto(String id_usuario){
        // Consulta SQL para calcular el promedio de los promedios mensuales de los ingresos
        String consulta = "SELECT AVG(promedio_mensual) AS promedio_mensual_total " +
                "FROM (SELECT YEAR(fechaInicio) AS año, MONTH(fechaInicio) AS mes, AVG(montoOriginal) AS promedio_mensual " +
                "FROM gastos WHERE idUsuario = ? " +
                "GROUP BY YEAR(fechaInicio), MONTH(fechaInicio)) AS promedios_mensuales";

        float promedio_mensual_total = 0;
        try{
            BaseDeDatos.establecerConexion();
            //Parametro de la consulta
            String[] parametros = {id_usuario};
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);

            //Procesar el resultado de la consulta
            if(rs.next()){
                promedio_mensual_total = rs.getFloat("promedio_mensual_total");
            }
            rs.close();
        }catch (SQLException e) {
            System.out.println("Error al calcular el promedio mensual de los ingresos: " + e.getMessage());
        } finally {
            BaseDeDatos.cerrarConexion();
        }
        return Math.round(promedio_mensual_total * 100.0f) / 100.0f;
    }

    // Método para calcular el promedio anual de todos los gastos
    public static float calcularPromedioAnualIngresos(String id_usuario) {
        // Consulta SQL para calcular el promedio de los promedios anuales de los ingresos
        String consulta = "SELECT AVG(promedio_anual) AS promedio_anual_total " +
                "FROM (SELECT YEAR(fechaInicio) AS año, AVG(montoOriginal) AS promedio_anual " +
                "FROM gastos WHERE idUsuario = ? " +
                "GROUP BY YEAR(fechaInicio)) AS promedios_anuales";

        float promedio_anual_total = 0;
        try {
            BaseDeDatos.establecerConexion();
            // Parámetros para la consulta
            String[] parametros = {id_usuario};
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);

            // Procesar el resultado de la consulta
            if (rs.next()) {
                promedio_anual_total = rs.getFloat("promedio_anual_total");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al calcular el promedio anual de los ingresos: " + e.getMessage());
        } finally {
            BaseDeDatos.cerrarConexion();
        }
        return Math.round(promedio_anual_total * 100.0f) / 100.0f;
    }

    //Metodo para obtner los gastos totales por mes de los ultimos doce meses
    public static List<Float> obtenerGastosUltimosMeses(String id_usuario){
        List<Float> gastos_mensuales = new ArrayList<>();
        LocalDate fecha_actual = LocalDate.now().withDayOfMonth(1);
        LocalDate fecha_inicial = fecha_actual.minusYears(1).withDayOfMonth(2);
        LocalDate siguiente_mes = fecha_inicial.plusMonths(1);
        float gasto_mensual = 0;

        //Consulta que seleciona todos los datos entre un rango de fecha que en este caso va de un mes a otro
        String consulta = "SELECT SUM(montoOriginal) AS gasto_mensual FROM gastos WHERE idUsuario = ? AND fechaInicio BETWEEN ? AND ? ";

        while(fecha_inicial.isBefore(fecha_actual)){
            String[] parametros = {id_usuario,fecha_inicial.toString(), String.valueOf(siguiente_mes)};
            try{
                BaseDeDatos.establecerConexion();
                ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta,parametros);

                //Analisis del resultset y guardado del resultado en el array list
                if(rs.next()){
                    gasto_mensual = rs.getFloat("gasto_mensual");
                    rs.close();
                } else {
                    gasto_mensual = 0;
                    rs.close();
                }
                gastos_mensuales.add(gasto_mensual);
                fecha_inicial = fecha_inicial.plusMonths(1);
                siguiente_mes = fecha_inicial.plusMonths(1);
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                BaseDeDatos.cerrarConexion();
            }
        }

        return gastos_mensuales;
    }

    //Metodo para obtner los gastos totales por año de los ultimos 5 años
    public static List<Float> obtenerGastosAnualesRecientes(String id_usuario){
        List<Float> gastos_anuales = new ArrayList<>();
        LocalDate fecha_actual = LocalDate.now().withDayOfMonth(1);
        LocalDate fecha_inicial = fecha_actual.minusYears(4).withDayOfYear(2);
        LocalDate siguiente_anio = fecha_inicial.plusYears(1);
        float gasto_anual = 0;

        //Consulta que seleciona todos los datos entre un rango de fecha que en este caso va de un mes a otro
        String consulta = "SELECT SUM(montoOriginal) AS gasto_anual FROM gastos WHERE idUsuario = ? AND fechaInicio BETWEEN ? AND ? ";
        while(fecha_inicial.isBefore(fecha_actual)){
            String[] parametros = {id_usuario,fecha_inicial.toString(), String.valueOf(siguiente_anio)};
            try{
                BaseDeDatos.establecerConexion();
                ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);

                //Analisis del resultset y guardado del resultado en el array list
                if(rs.next()){
                    gasto_anual = rs.getFloat("gasto_anual");
                    rs.close();
                } else {
                    gasto_anual = 0;
                    rs.close();
                }
                gastos_anuales.add(gasto_anual);
                fecha_inicial = fecha_inicial.plusYears(1);
                siguiente_anio = fecha_inicial.plusYears(1);
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                BaseDeDatos.cerrarConexion();
            }
        }
        return gastos_anuales;
    }




}
