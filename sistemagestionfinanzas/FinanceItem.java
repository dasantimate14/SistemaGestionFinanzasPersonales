package sistemagestionfinanzas;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public abstract class FinanceItem {
    //Atributos de la superclase
    protected String nombre;
    protected String descripcion;
    protected float montoOriginal;
    protected String tipo;
    protected float porcentajeGanancia;
    protected float ganaciaPerdida;
    protected String id;
    protected float tasaInteres;
    protected float interes;
    protected LocalDate fechaInicio;
    protected float montoActual;
    protected float promedioMensual;

    //Constructor de la superclase
    public FinanceItem(String nombre, String descripcion, float montoOriginal, String tipo,
                       float tasaInteres, LocalDate fechaInicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.montoOriginal = montoOriginal;
        this.tipo = tipo;
        this.tasaInteres = tasaInteres;
        this.fechaInicio = fechaInicio;
    }
    // Segundo constructor sin tasaInteres, asignando un valor por defecto
    public FinanceItem(String nombre, String descripcion, float montoOriginal, String tipo,
                       LocalDate fechaInicio) {
        this(nombre, descripcion, montoOriginal, tipo, 0.0f, fechaInicio);
    }
    //Metodos get y set de la superclase
    public String getNombre() {return nombre;}
    public String getDescripcion() {return descripcion;}
    public float getMontoOriginal() {return montoOriginal;}
    public String getTipo() {return tipo;}
    public float getPorcentajeGanancia() {return porcentajeGanancia; }
    public String getId() {return id;}
    public float getTasaInteres() {return tasaInteres;}
    public float getInteres() {return interes;}
    public LocalDate getFechaInicio() {return fechaInicio;}
    public float getMontoActual() {return montoActual;}
    public float getPromedioMensual() {return promedioMensual;}

    public float getGanaciaPerdida() {return ganaciaPerdida;}

    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setMontoOriginal(float montoOriginal) {this.montoOriginal = montoOriginal;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setPorcentajeGanancia(float procentajeGanancia){this.porcentajeGanancia = procentajeGanancia;}
    public void setId(String id) {this.id = id;}
    public void setTasaInteres(float tasaInteres) {this.tasaInteres = tasaInteres;}
    public void setInteres(float interes) {this.interes = interes;}
    public void setFechaInicio(LocalDate fechaInicio) {this.fechaInicio = fechaInicio;}
    public void setMontoActual(float montoActual) {this.montoActual = montoActual;}
    public void setPromedioMensual(float promedioMensual) {this.promedioMensual = promedioMensual;}
    public void setGanaciaPerdida(float ganaciaPerdida){this.ganaciaPerdida = ganaciaPerdida;}

    //Metodos propios de la superclase
    protected abstract float calcularValorActual() throws IOException;

    public float calcularGanaciaPerdida() throws IOException {
        setGanaciaPerdida(redonderCantidad(calcularValorActual() - montoOriginal));
        return getGanaciaPerdida();
    }

    protected float calcularPorcentajeGananciaPerdida() throws IOException {
        return redonderCantidad((calcularGanaciaPerdida()/ montoOriginal) * 100);
    }

    //Metodo para imprimir los atributos de la clase
    protected StringBuilder obtenerInformacionGeneral(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Descripcion: ").append(descripcion).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("Monto Original: ").append(montoOriginal).append("\n");
        sb.append("Interes: ").append(interes).append("\n");
        sb.append("Tasa interes: ").append(tasaInteres).append("%\n");
        sb.append("Cantidad de Ganancia/Perdida: ").append(ganaciaPerdida).append("\n");
        sb.append("Porcentaje de ganancia: ").append(porcentajeGanancia).append("%\n");
        sb.append("Monto Actual: ").append(montoActual).append("\n");
        sb.append("Valor Mensual Promedio: ").append(promedioMensual).append("\n");
        sb.append("Fecha inicio: ").append(fechaInicio).append("\n");
        return sb;
    }

    //Metodo para obtener informacion especifica de la subclases
    protected abstract StringBuilder obtenerInformacionSubclase();

    //Metodo para obtener toda la informacion
    public final void obtenerInformacionCompleta(){
        StringBuilder informacionGeneral = obtenerInformacionGeneral();
        StringBuilder informacionSubclase = obtenerInformacionSubclase();
        System.out.println(informacionGeneral.toString());
        System.out.println(informacionSubclase.toString());

    }

    //Metodo para obtener el porcentaje que representa este elemento del total - El arreglo activosPasivos representa que se le pasa un arreglo de activos o de pasivos al método dependiendo de la clase
    protected static float calcularPorcentajeRepresentacionSubclase(List<FinanceItem> activosPasivos) {
        return 0;
    }

    protected abstract float calcularPromedioMensual() throws SQLException, IOException;

    protected abstract float calcularPromedioAnual() throws IOException;

    protected void generarGrafica(){
        return;
    }

    protected JTable generarTabla(){
        return null;
    }

    protected float calcularInteres(){
        setInteres(montoOriginal *(tasaInteres/100));
        return getInteres();
    }

    protected void calcularProyeccion(int tiempo, String unidadTiempo){
        return;
    }

    protected void mostrarInformacion(){
        obtenerInformacionCompleta();
    }

    //Metodo que usa todos los otros metodos que calculan valores que cambian con el tiempo y se actualizen al momento actual
    protected abstract void actualizarInformacion() throws IOException;

    //Metodo para redondear la cantidad de dinero a dos decimales
    protected float redonderCantidad(float cantidad){
        return Math.round(cantidad* 100.0f) / 100.0f;

    }





}
