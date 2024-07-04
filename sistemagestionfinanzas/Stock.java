package sistemagestionfinanzas;

import java.time.LocalDate;

public class Stock extends FinanceItem{
    //Atributos de la clase
    String nombreEmpresa;
    String simbolo;
    int cantidad;
    float precioCompra;
    String sector;
    float precioActual;
    float dividendoPorAccion;
    float dividendoAcumulado;

    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precioCompra, String sector, float dividendoPorAccion){
        super(nombre, descripcion, (cantidad*precioCompra), "Activo", tasaInteres, fechaInicio);
        this.nombreEmpresa = nombreEmpresa;
        this.simbolo = simbolo;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.sector = sector;
        this.dividendoPorAccion = dividendoPorAccion;
    }

    // Constructor con dividendoPorAccion por defecto
    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precioCompra, String sector) {
        this(nombre, descripcion, tasaInteres, fechaInicio, nombreEmpresa, simbolo, cantidad, precioCompra, sector, 0.0f);
    }

    @Override
    protected float calcularValorActual() {
        return 0;
    }

    @Override
    protected StringBuilder obtenerInformacion() {
        return null;
    }

    @Override
    protected void calcularPorcentajeRepresentacion(FinanceItem[] activosPasivos) {

    }

    @Override
    protected float calcularPromedio() {
        return 0;
    }

    @Override
    protected void actualizarInformacion() {

    }
}
