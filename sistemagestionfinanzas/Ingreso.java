package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Ingreso extends FinanceItem{
    //Declaracion de atributos
    private String fuente;
    private CuentaBancaria cuenta_bancaria;
    private int frencuencia;
    private static int cantidad_instancias = 0;
    private static List<Ingreso> instancias_ingresos = new ArrayList<>();

    //Constructor
    public Ingreso(String nombre, String  descripcion, float montoOriginal, LocalDate fechaInicio, String fuente, CuentaBancaria cuenta_bancaria, int frencuencia) {
        super(nombre, descripcion, montoOriginal, "Activo", fechaInicio);
        this.fuente = fuente;
        this.cuenta_bancaria = cuenta_bancaria;
        this.frencuencia = frencuencia;
        instancias_ingresos.add(this);
        cantidad_instancias++;
    }
    //Método Constructor para objetos sin frecuencia recurrente de deposito
    public Ingreso(String nombre, String  descripcion, float montoOriginal,
                   LocalDate fechaInicio, String fuente, CuentaBancaria cuenta_bancaria){
        this(nombre, descripcion, montoOriginal, fechaInicio, fuente, cuenta_bancaria, 0);
    }

    //Metodos get y set
    public String getFuente(){ return fuente;}
    public CuentaBancaria getCuentaBancaria(){return cuenta_bancaria;}
    public int getFrencuencia(){return frencuencia;}

    public void setFuente(String fuente){this.fuente = fuente;}
    public void setCuentaBancaria(CuentaBancaria cuenta_bancaria){this.cuenta_bancaria = cuenta_bancaria;}
    public void setFrencuencia(int frencuencia){this.frencuencia = frencuencia;}


    @Override
    protected float calcularValorActual() throws IOException {
        return getMontoOriginal();
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fuente del Ingreso: ").append(fuente).append("\n");
        sb.append("Cuenta Bancaria Asociada: ").append(cuenta_bancaria).append("\n");
        sb.append("Frecuencia de deposito: ").append(frencuencia).append("\n");
        return sb;
    }

    //No se calcula el porcentaje de representación de los ingresos porque se reflejan en la cuenta bancaria
    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
    }

    @Override
    protected float calcularPromedioMensual() throws SQLException, IOException {
        return 0;
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        return 0;
    }

    //Método que permite registrar los ingresos con frecuencia
    @Override
    protected void actualizarInformacion() throws IOException {
        if(getFrencuencia() != 0){
            LocalDate fecha_final = LocalDate.now();
            LocalDate fecha_inicio = getFechaInicio();
            ResultSet rs = null;
            String[] parametros = new String[8];
            String consulta = "SELECT MAX(fechaInicio) AS fecha_mas_reciente FROM ingresos WHERE idUsuario = '" + cuenta_bancaria.getIdUsuario() + "' AND idCuentaBancaria = '" + cuenta_bancaria.getId() + "' AND nombre = '" + getNombre() + "'";
            String consulta_registro = "INSERT INTO ingresos (id, nombre, descripcion, montoOriginal, fechaInicio, fuente, idUsuario, idCuentaBancaria) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?)";

            //Se hace la consulta para obtener la fecha más reciente de registro de un ingreso que se repite por meses según la frecuencia
            try{
                BaseDeDatos.establecerConexion();
                rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
                if(rs != null){
                    fecha_inicio = LocalDate.parse(rs.getString("fecha_mas_reciente"));
                }
                rs.close();
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                BaseDeDatos.cerrarConexion();
            }

            //Si la fecha inicio es igual al getFechaInicio quiere decir que la consulta no encontró una fecha más reciente de deposito y empezará a hacer depositos desde el primer registro del ingreso hasta la fecha actual según la frecuecia.
            while(fecha_inicio.isBefore(fecha_final)){
                //Se vacía el arreglo de parametros
                for (int i = 0; i < parametros.length; i++) {
                    parametros[i] = "";
                }
                //Se crea el objeto que registra el ingreso automaticamente por el programa
                fecha_inicio = fecha_inicio.plusMonths(getFrencuencia());
                Ingreso ingreso = new Ingreso(getNombre(), getDescripcion(), getMontoOriginal(), fecha_inicio, getFuente(), getCuentaBancaria());
                cuenta_bancaria.depositarMonto(ingreso.montoOriginal);
                parametros = new String[]{ingreso.getNombre(), ingreso.getDescripcion(), String.valueOf(ingreso.getMontoOriginal()), String.valueOf(getFechaInicio()), ingreso.getFuente(), ingreso.cuenta_bancaria.getIdUsuario(), ingreso.cuenta_bancaria.getId()};
            }
            //Registro del ingreso por frecuencia en la base de datos
            try {
                BaseDeDatos.establecerConexion();
                boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
                if (registro_exitoso){
                    System.out.println("Registro exitoso de ingreso." );
                }
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                BaseDeDatos.cerrarConexion();
            }
        }
    }

    //Metodo que calcula el porcentaje de representacion de un ingreso bajo el mismo nombre (como salario) frente a los demás
    public void calcularPorcentajeRepresentacionIngreso(String nombre){
        float sumatoria_ingreso = 0;
        float ingresos_totales = 0;
        float porcentaje_representacion = 0;
        for(Ingreso ingreso : instancias_ingresos){
            ingresos_totales += ingreso.montoOriginal;
            if(ingreso.getNombre().equals(nombre)){
                sumatoria_ingreso += ingreso.montoOriginal;
            }
        }
        //Calcular porcentaje representación
        porcentaje_representacion = (float) (sumatoria_ingreso/ingresos_totales)*100;
        System.out.println("El porcentaje de representacion de "+ nombre + " es " + porcentaje_representacion +" con un total de " + sumatoria_ingreso);
    }

    //Método para calcular el promedio mensual de todos los ingresos
    public static float calcularPromedioMensualIngresos(String id_usuario){
        // Consulta SQL para calcular el promedio de los promedios mensuales de los ingresos
        String consulta = "SELECT AVG(promedio_mensual) AS promedio_mensual_total " +
                "FROM (SELECT YEAR(fechaInicio) AS año, MONTH(fechaInicio) AS mes, AVG(montoOriginal) AS promedio_mensual " +
                "FROM ingresos WHERE idUsuario = ? " +
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
        return promedio_mensual_total;
    }

    // Método para calcular el promedio anual de todos los ingresos
    public static float calcularPromedioAnualIngresos(String id_usuario) {
        // Consulta SQL para calcular el promedio de los promedios anuales de los ingresos
        String consulta = "SELECT AVG(promedio_anual) AS promedio_anual_total " +
                "FROM (SELECT YEAR(fechaInicio) AS año, AVG(montoOriginal) AS promedio_anual " +
                "FROM ingresos WHERE idUsuario = ? " +
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
        return promedio_anual_total;
    }

    //Metodo para obtner los ingresos totales por mes de los ultimos doce meses
    public List<Float> obtenerIngresosUltimosMeses(){
        List<Float> ingresos_mensuales = new ArrayList<>();
        LocalDate fecha_actual = LocalDate.now().withDayOfMonth(1);
        LocalDate fecha_inicial = fecha_actual.minusYears(1);
        float ingreso_mensual = 0;

        //Consulta que seleciona todos los datos entre un rango de fecha que en este caso va de un mes a otro
        String consulta = "SELECT SUM(montoOriginal) AS ingreso_mensual FROM ingresos WHERE idUsuario = '" + cuenta_bancaria.getIdUsuario() + "' AND idCuentaBancaria = '" + cuenta_bancaria.getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fecha_inicial)  + "' AND DATE_ADD('" + java.sql.Date.valueOf(fecha_inicial) + "', INTERVAL 1 MONTH)";
        while(fecha_inicial.isBefore(fecha_actual)){
            try{
                BaseDeDatos.establecerConexion();
                ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);

                //Analisis del resultset y guardado del resultado en el array list
                if(rs != null){
                    ingreso_mensual = rs.getFloat("ingreso_mensual");
                    ingresos_mensuales.add(ingreso_mensual);
                    fecha_inicial = fecha_inicial.plusMonths(1);
                    rs.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        BaseDeDatos.cerrarConexion();
        return ingresos_mensuales;
    }

    //Metodo para obtner los ingresos totales por año de los ultimos doce meses
    public List<Float> obtenerIngresosAnualesRecientes(){
        List<Float> ingresos_anuales = new ArrayList<>();
        LocalDate fecha_actual = LocalDate.now().withDayOfMonth(1);
        LocalDate fecha_inicial = fecha_actual.minusYears(5).withDayOfYear(1);
        float ingreso_anual = 0;

        //Consulta que seleciona todos los datos entre un rango de fecha que en este caso va de un mes a otro
        String consulta = "SELECT SUM(montoOriginal) AS ingreso_anual FROM ingresos WHERE idUsuario = '" + cuenta_bancaria.getIdUsuario()+ "' AND idCuentaBancaria = '" + cuenta_bancaria.getId() + "' AND fechaInicio BETWEEN '" + java.sql.Date.valueOf(fecha_inicial)  + "' AND DATE_ADD('" + java.sql.Date.valueOf(fecha_inicial) + "', INTERVAL 1 YEAR)";
        while(fecha_inicial.isBefore(fecha_actual)){
            try{
                BaseDeDatos.establecerConexion();
                ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);

                //Analisis del resultset y guardado del resultado en el array list
                if(rs != null){
                    ingreso_anual = rs.getFloat("ingreso_anual");
                    ingresos_anuales.add(ingreso_anual);
                    fecha_inicial = fecha_inicial.plusYears(1);
                    rs.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        BaseDeDatos.cerrarConexion();
        return ingresos_anuales;
    }

    //Metodo para guardar el ingreso en la base de datos


}
