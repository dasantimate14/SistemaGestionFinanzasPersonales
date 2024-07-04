package sistemagestionfinanzas;

import javax.swing.*;
import java.time.LocalDate;

public abstract class FinanceItem {
    //Atributos de la superclase
    protected String nombre;
    protected String descripcion;
    protected float monto;
    protected String tipo;
    protected float porcentajeGanancia;
    protected String id;
    protected float tasaInteres;
    protected float interes;
    protected LocalDate fechaInicio;
    protected float montoTotal;
    protected float promedioMensual;

    //Constructor de la superclase
    public FinanceItem(String nombre, String descripcion, float monto, String tipo,
                          float tasaInteres, LocalDate fechaInicio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipo = tipo;
        this.tasaInteres = tasaInteres;
        this.fechaInicio = fechaInicio;
    }
    //Metodos get y set de la superclase
    public String getNombre() {return nombre;}
    public String getDescripcion() {return descripcion;}
    public float getMonto() {return monto;}
    public String getTipo() {return tipo;}
    public float getPorcentajeGanancia() {return porcentajeGanancia; }
    public String getId() {return id;}
    public float getTasaInteres() {return tasaInteres;}
    public float getInteres() {return interes;}
    public LocalDate getFechaInicio() {return fechaInicio;}
    public float getMontoTotal() {return montoTotal;}
    public float getPromedioMensual() {return promedioMensual;}

    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public void setMonto(float monto) {this.monto = monto;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setPorcentajeGanancia(float procentajeGanancia){this.porcentajeGanancia = procentajeGanancia;}
    public void setId(String id) {this.id = id;}
    public void setTasaInteres(float tasaInteres) {this.tasaInteres = tasaInteres;}
    public void setInteres(float interes) {this.interes = interes;}
    public void setFechaInicio(LocalDate fechaInicio) {this.fechaInicio = fechaInicio;}
    public void setMontoTotal(float montoTotal) {this.montoTotal = montoTotal;}
    public void setPromedioMensual(float promedioMensual) {this.promedioMensual = promedioMensual;}

    //Metodos propios de la superclase
    protected abstract float calcularValorActual();

    protected float calcularGanaciaPerdida(){
        return calcularValorActual() - monto;
    }

    protected float calcularPorcentajeGananciaPerdida(){
        return (calcularGanaciaPerdida()/monto) * 100;
    }

    //Metodo para imprimir los atributos de la clase
    protected final StringBuilder obtenerInformacionGeneral(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: " + nombre + "\n");
        sb.append("ID: " + id + "\n");
        sb.append("Descripcion: " + descripcion + "\n");
        sb.append("Tipo: " + tipo + "\n");
        sb.append("Monto: " + monto + "\n");
        sb.append("Interes: " + interes + "\n");
        sb.append("Tasa interes: " + tasaInteres + "%\n");
        sb.append("Monto Total: " + montoTotal + "\n");
        sb.append("Porcentaje de ganancia: " + porcentajeGanancia + "%\n");
        sb.append("Valor Mensual Promedio : " + promedioMensual + "\n");
        sb.append("Fecha inicio: " + fechaInicio + "\n");
        return sb;
    }

    //Metodo para obtener toda la informacion


    //Metodo para obtener el porcentaje que representa este elemento del total
    protected abstract void calcularPorcentajeRepresentacion(FinanceItem[] activosPasivos);

    protected abstract float calcularPromedioMensual();

    protected abstract float calcularPromedioAnual();

    protected void generarGrafica(){
        return;
    }

    protected JTable generarTabla(){
        return null;
    }

    protected float calcularInteres(){
        setInteres(monto*(tasaInteres/100));
        return getInteres();
    }

    protected void calcularProyeccion(int tiempo, String unidadTiempo){
        return;
    }

    protected void mostrarInformacion(){
        return;
    }

    protected abstract void actualizarInformacion();





}
