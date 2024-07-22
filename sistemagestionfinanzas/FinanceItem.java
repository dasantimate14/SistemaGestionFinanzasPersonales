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
    protected float monto_original;
    protected String tipo;
    protected float porcentaje_ganancia;
    protected float ganancia_perdida;
    protected String id;
    protected float tasa_interes;
    protected float interes;
    protected LocalDate fecha_inicio;
    protected float monto_actual;
    protected float promedio_mensual;

    //Constructor de la superclase
    public FinanceItem(String nombre, String descripcion, float monto_original, String tipo,
                       float tasa_interes, LocalDate fecha_inicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto_original = monto_original;
        this.tipo = tipo;
        this.tasa_interes = tasa_interes;
        this.fecha_inicio = fecha_inicio;
    }
    // Segundo constructor sin tasaInteres, asignando un valor por defecto
    public FinanceItem(String nombre, String descripcion, float montoOriginal, String tipo,
                       LocalDate fechaInicio) {
        this(nombre, descripcion, montoOriginal, tipo, 0.0f, fechaInicio);
    }
    //Metodos get y set de la superclase
    public String getNombre() {return nombre;}
    public String getDescripcion() {return descripcion;}
    public float getMontoOriginal() {return monto_original;}
    public String getTipo() {return tipo;}
    public float getPorcentajeGanancia() {return porcentaje_ganancia; }
    public String getId() {return id;}
    public float getTasaInteres() {return tasa_interes;}
    public float getInteres() {return interes;}
    public LocalDate getFechaInicio() {return fecha_inicio;}
    public float getMontoActual() {return monto_actual;}
    public float getPromedioMensual() {return promedio_mensual;}
    public float getGanaciaPerdida() {return ganancia_perdida;}

    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setMontoOriginal(float monto_original) {this.monto_original = monto_original;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setPorcentajeGanancia(float porcentaje_ganancia){this.porcentaje_ganancia = porcentaje_ganancia;}
    public void setId(String id) {this.id = id;}
    public void setTasaInteres(float tasa_interes) {this.tasa_interes = tasa_interes;}
    public void setInteres(float interes) {this.interes = interes;}
    public void setFechaInicio(LocalDate fecha_inicio) {this.fecha_inicio = fecha_inicio;}
    public void setMontoActual(float monto_actual) {this.monto_actual = monto_actual;}
    public void setPromedioMensual(float promedioMensual) {this.promedio_mensual = promedioMensual;}
    public void setGanaciaPerdida(float ganancia_perdida){this.ganancia_perdida = ganancia_perdida;}

    //Metodos propios de la superclase
    protected abstract float calcularValorActual() throws IOException;

    public float calcularGanaciaPerdida() throws IOException {
        setGanaciaPerdida(redonderCantidad(calcularValorActual() - monto_original));
        return getGanaciaPerdida();
    }

    protected float calcularPorcentajeGananciaPerdida() throws IOException {
        return redonderCantidad((calcularGanaciaPerdida()/ monto_original) * 100);
    }

    //Metodo para imprimir los atributos de la clase
    protected StringBuilder obtenerInformacionGeneral(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Descripcion: ").append(descripcion).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("Monto Original: ").append(monto_original).append("\n");
        sb.append("Interes: ").append(interes).append("\n");
        sb.append("Tasa interes: ").append(tasa_interes).append("%\n");
        sb.append("Cantidad de Ganancia/Perdida: ").append(ganancia_perdida).append("\n");
        sb.append("Porcentaje de ganancia: ").append(porcentaje_ganancia).append("%\n");
        sb.append("Monto Actual: ").append(monto_actual).append("\n");
        sb.append("Valor Mensual Promedio: ").append(promedio_mensual).append("\n");
        sb.append("Fecha inicio: ").append(fecha_inicio).append("\n");
        return sb;
    }

    //Metodo para obtener informacion especifica de la subclases
    protected abstract StringBuilder obtenerInformacionSubclase();

    //Metodo para obtener toda la informacion
    public final void obtenerInformacionCompleta(){
        StringBuilder informacionGeneral = obtenerInformacionGeneral();
        StringBuilder informacionSubclase = obtenerInformacionSubclase();
        System.out.print(informacionGeneral.toString());
        System.out.println(informacionSubclase.toString());

    }

    //Metodo para obtener el porcentaje que representa este elemento del total - El arreglo activosPasivos representa que se le pasa un arreglo de activos o de pasivos al método dependiendo de la clase
    protected static float calcularPorcentajeRepresentacionSubclase(List<FinanceItem> activosPasivos) {
        return 0;
    }

    protected abstract float calcularPromedioMensual() throws SQLException, IOException;

    protected abstract float calcularPromedioAnual() throws IOException;

    protected float calcularInteres(){
        setInteres(monto_original *(tasa_interes/100));
        return getInteres();
    }

    protected void calcularProyeccion(int tiempo, String unidadTiempo){
        return;
    }

    protected void mostrarInformacion(){
        obtenerInformacionCompleta();
    }

    //Metodo que usa todos los otros metodos que calculan valores que cambian con el tiempo y se actualizen al momento actual
    protected abstract void actualizarInformacion() throws IOException, SQLException;

    //Metodo para redondear la cantidad de dinero a dos decimales
    protected float redonderCantidad(float cantidad){
        return Math.round(cantidad* 100.0f) / 100.0f;

    }





}
