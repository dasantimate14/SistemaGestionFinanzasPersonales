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
    static int cantidadInstancias = 0;
    static Stock[] instanciasStocks = new Stock[cantidadInstancias + 1];

    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precioCompra, String sector, float dividendoPorAccion){
        super(nombre, descripcion, (cantidad*precioCompra), "Activo", tasaInteres, fechaInicio);
        this.nombreEmpresa = nombreEmpresa;
        this.simbolo = simbolo;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.sector = sector;
        this.dividendoPorAccion = dividendoPorAccion;

        //Se guarda la instancia dentro de un arreglo que pertenece a la clase misma y no a la instancia
        instanciasStocks[cantidadInstancias] = this;
        cantidadInstancias++;
    }

    // Constructor con dividendoPorAccion por defecto
    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precioCompra, String sector) {
        this(nombre, descripcion, tasaInteres, fechaInicio, nombreEmpresa, simbolo, cantidad, precioCompra, sector, 0.0f);

        //Se guarda la instancia dentro de un arreglo que pertenece a la clase misma y no a la instancia
        instanciasStocks[cantidadInstancias] = this;
        cantidadInstancias++;
    }

    //Metodos get y set de la clase
    public String getNombreEmpresa() {return nombreEmpresa;}
    public String getSimbolo() {return simbolo;}
    public int getCantidad() {return cantidad;}
    public float getPrecioCompra() {return precioCompra;}
    public String getSector() {return sector;}
    public float getDividendoPorAccion() {return dividendoPorAccion;}

    public void setNombreEmpresa(String nombreEmpresa){this.nombreEmpresa = nombreEmpresa;}
    public void setSimbolo(String simbolo){this.simbolo = simbolo;}
    public void setCantidad(int cantidad){this.cantidad = cantidad;}
    public void setPrecioCompra(float precioCompra){this.precioCompra = precioCompra;}
    public void setSector(String sector){this.sector = sector;}
    public void setDividendoPorAccion(float dividendoPorAccion){this.dividendoPorAccion = dividendoPorAccion;}

    @Override
    protected float calcularValorActual() {
        return 0;
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre Empresa: ").append(nombreEmpresa).append("\n");
        sb.append("Símbolo: ").append(simbolo).append("\n");
        sb.append("Cantidad: ").append(cantidad).append("\n");
        sb.append("Precio de Compra: ").append(precioCompra).append("\n");
        sb.append("Dividendo por Acción: ").append(dividendoPorAccion).append("\n");
        sb.append("Precio Actual: ").append(precioActual).append("\n");
        sb.append("Dividendo Acumulado: ").append(dividendoAcumulado).append("\n");
        sb.append("Sector: ").append(sector).append("\n");
        return sb;
    }


    @Override
    public void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        float valorTotalActivos = 0;
        float valorTotalStocks = 0;
        float porcentajeRepresentacion = 0;
        //Por cada activo en el arreglo acumular su valor y si es de la instancia de stock se acumula en su respectiva variable
        for(FinanceItem item : activosPasivos){
            valorTotalActivos = item.getMontoActual() + valorTotalActivos;
            if(item instanceof Stock){
                Stock stock = (Stock)item;
                valorTotalStocks = stock.getMontoActual() + valorTotalStocks;
            }
        }
        //Calculo de porcentaje
        porcentajeRepresentacion = (valorTotalStocks/valorTotalActivos)*100;
        System.out.println("El porcentaje de representación de todos los Stocks es " + porcentajeRepresentacion + "% con un valor de " + valorTotalStocks);
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
    protected void actualizarInformacion() {

    }

    //Metodo para obtener el porcentaje de representacion de una instancia de stock
    public void calcularPorcentajeRepresentacionPorSimbolo(String simbolo){
        float valorTotalStocks = 0;
        float valorStockSimbolo = 0;
        float porcentajeRepresentacion = 0;
        //Por cada stock que coincida con el simbolo se suma su monto del simbolo y por cada stock se suma a una variable de todos los stocks
        for(Stock instanciaStock : instanciasStocks){
            valorTotalStocks = instanciaStock.getMontoActual() + valorTotalStocks;
            if(instanciaStock.getSimbolo().equals(simbolo)){
                valorStockSimbolo = instanciaStock.getMontoActual() + valorStockSimbolo;
            }
        }
        //Calcular Porcentaje
        porcentajeRepresentacion = (valorStockSimbolo/valorTotalStocks)*100;
        System.out.println("El porcentaje de representación de todos los Stocks con símbolo " + simbolo +  " es " + porcentajeRepresentacion + "% con un valor de " + valorStockSimbolo);
    }



}
