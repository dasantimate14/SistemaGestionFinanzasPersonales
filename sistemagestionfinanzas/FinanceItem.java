package sistemagestionfinanzas;

import java.time.LocalDate;

public class FinanceItem {
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
    protected float promedio;

    //Constructor de la superclase
    protected FinanceItem(String nombre, String descripcion, float monto, String tipo, float porcentajeGanancia, String id,
                          float tasaInteres, float interes, LocalDate fechaInicio, float promedio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipo = tipo;
        this.porcentajeGanancia = porcentajeGanancia;
        this.id = id;
        this.tasaInteres = tasaInteres;
        this.interes = interes;
        this.fechaInicio = fechaInicio;
        this.montoTotal = montoTotal;
        this.promedio = promedio;
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
    public float getPromedio() {return promedio;}

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
    public void setPromedio(float promedio) {this.promedio = promedio;}


}
