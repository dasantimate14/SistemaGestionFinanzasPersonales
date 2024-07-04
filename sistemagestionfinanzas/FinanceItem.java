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
    protected float ganaciaPerdida;
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
    // Segundo constructor sin tasaInteres, asignando un valor por defecto
    public FinanceItem(String nombre, String descripcion, float monto, String tipo,
                       LocalDate fechaInicio) {
        this(nombre, descripcion, monto, tipo, 0.0f, fechaInicio);
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

    public float getGanaciaPerdida() {return ganaciaPerdida;}

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
    public void setGanaciaPerdida(float ganaciaPerdida){this.ganaciaPerdida = ganaciaPerdida;}

    //Metodos propios de la superclase
    protected abstract float calcularValorActual();

    protected float calcularGanaciaPerdida(){
        return calcularValorActual() - monto;
    }

    protected float calcularPorcentajeGananciaPerdida(){
        return (calcularGanaciaPerdida()/monto) * 100;
    }

    //Metodo para imprimir los atributos de la clase
    protected StringBuilder obtenerInformacionGeneral(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Descripcion: ").append(descripcion).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("Monto: ").append(monto).append("\n");
        sb.append("Interes: ").append(interes).append("\n");
        sb.append("Tasa interes: ").append(tasaInteres).append("%\n");
        sb.append("Cantidad de Ganancia/Perdida").append(ganaciaPerdida).append("%\n");
        sb.append("Porcentaje de ganancia: ").append(porcentajeGanancia).append("%\n");
        sb.append("Monto Total: ").append(montoTotal).append("\n");
        sb.append("Valor Mensual Promedio: ").append(promedioMensual).append("\n");
        sb.append("Fecha inicio: ").append(fechaInicio).append("\n");
        return sb;
    }

    //Metodo para obtener informacion especifica de la subclases
    protected abstract StringBuilder obtenerInformacionSubclase();

    //Metodo para obtener toda la informacion
    protected final void obtenerInformacionCompleta(){
        StringBuilder informacionGeneral = obtenerInformacionGeneral();
        StringBuilder informacionSubclase = obtenerInformacionSubclase();
        System.out.println(informacionGeneral.toString());
        System.out.println(informacionSubclase.toString());

    }

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
        obtenerInformacionCompleta();
    }

    protected abstract void actualizarInformacion();





}
