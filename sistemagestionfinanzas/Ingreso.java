package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Ingreso extends FinanceItem{
    //Declaracion de atributos
    private String fuente;
    private CuentaBancaria cuenta_bancaria;
    private int frencuencia;
    private static int cantidad_instancias;
    private static List<Ingreso> instancias_ingresos;

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
        return 0;
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fuente del Ingreso: ").append(fuente).append("\n");
        sb.append("Cuenta Bancaria Asociada: ").append(cuenta_bancaria).append("\n");
        sb.append("Frecuencia de deposito: ").append(frencuencia).append("\n");
        return sb;
    }

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


}
