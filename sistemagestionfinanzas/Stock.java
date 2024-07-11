package sistemagestionfinanzas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stock extends FinanceItem{
    //Atributos de la clase
    private String nombreEmpresa;
    private String simbolo;
    private int cantidad;
    private float precioCompra;
    private String sector;
    private float precioActual;
    private float dividendoPorAccion;
    private float dividendoAcumulado;
    private float dividendoEstimado;
    private int frecuenciaDividendos;
    private static int cantidadInstancias = 0;
    private static List<Stock> instanciasStocks = new ArrayList<>();

    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precioCompra, String sector, float dividendoPorAccion, int frecuenciaDividendos){
        super(nombre, descripcion, (cantidad*precioCompra), "Activo", tasaInteres, fechaInicio);
        this.nombreEmpresa = nombreEmpresa;
        this.simbolo = simbolo;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.sector = sector;
        this.dividendoPorAccion = dividendoPorAccion;
        this.frecuenciaDividendos = frecuenciaDividendos;

        //Se guarda la instancia dentro de un arreglo que pertenece a la clase misma y no a la instancia
        instanciasStocks.add(this);
        cantidadInstancias++;
    }

    // Constructor con dividendoPorAccion por defecto
    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precioCompra, String sector) {
        this(nombre, descripcion, tasaInteres, fechaInicio, nombreEmpresa, simbolo, cantidad, precioCompra, sector, 0.0f, 0);
    }

    //Metodos get y set de la clase
    public String getNombreEmpresa() {return nombreEmpresa;}
    public String getSimbolo() {return simbolo;}
    public int getCantidad() {return cantidad;}
    public float getPrecioCompra() {return precioCompra;}
    public String getSector() {return sector;}
    public float getDividendoPorAccion() {return dividendoPorAccion;}
    public float getDividendoAcumulado(){return dividendoAcumulado;}
    public float getDividendoEstimado(){return dividendoEstimado;}
    public static int getCantidadInstancias() {return cantidadInstancias;}
    public float getPrecioActual(){return precioActual;}
    public int getFrecuenciaDividendos(){return frecuenciaDividendos;}

    public void setNombreEmpresa(String nombreEmpresa){this.nombreEmpresa = nombreEmpresa;}
    public void setSimbolo(String simbolo){this.simbolo = simbolo;}
    public void setCantidad(int cantidad){this.cantidad = cantidad;}
    public void setPrecioCompra(float precioCompra){this.precioCompra = precioCompra;}
    public void setSector(String sector){this.sector = sector;}
    public void setDividendoPorAccion(float dividendoPorAccion){this.dividendoPorAccion = dividendoPorAccion;}
    public void setDividendoAcumulado(float dividendoAcumulado){this.dividendoAcumulado = dividendoAcumulado;}
    public void setDividendoEstimado(float dividendoEstimado){this.dividendoEstimado = dividendoEstimado;}
    public void setPrecioActual(float precioActual){this.precioActual = precioActual;}

    @Override
    protected float calcularValorActual() throws IOException {
        setPrecioActual(obtenerPrecioActual() + calcularDividendoAcumulado());
        return getPrecioActual();
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre Empresa: ").append(nombreEmpresa).append("\n");
        sb.append("Símbolo: ").append(simbolo).append("\n");
        sb.append("Cantidad: ").append(cantidad).append("\n");
        sb.append("Precio de Compra: ").append(precioCompra).append("\n");
        sb.append("Dividendo por Acción: ").append(dividendoPorAccion).append("\n");
        sb.append("Frecuencia de Pago de Dividendos: ").append(frecuenciaDividendos).append("\n");
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
    protected float calcularPromedioMensual() throws IOException {
        List<Float> preciosMensuales = obtenerPreciosMensuales();
        if (preciosMensuales.isEmpty()) {
            return 0.0f;
        }

        float sumaPrecios = 0.0f;
        for (float precio : preciosMensuales) {
            sumaPrecios += precio;
        }

        return sumaPrecios / preciosMensuales.size();
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        float sumaPromedio = 0.0f;
        List<Float> preciosAnuales = obtenerPreciosAnuales();
        if(preciosAnuales.isEmpty()){
            return 0.0f;
        }
        for (float precio : preciosAnuales) {
            sumaPromedio += precio;
        }
        return sumaPromedio/preciosAnuales.size();
    }

    @Override
    protected void actualizarInformacion() throws IOException {
        setGanaciaPerdida(calcularGanaciaPerdida());
        setMontoActual(calcularValorActual());
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

    public float calcularDividendoAcumulado(){
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaCompra = getFechaInicio();
        //Solo se calcula el dividiendo acumulado si es diferente de cero
        if(getDividendoPorAccion() != 0){
            while(!fechaCompra.isAfter(fechaHoy)){
                //Se debe acumular el dividendo si es el primero del mes
                if(fechaCompra.getDayOfMonth() == 1 && fechaCompra.equals(getFechaInicio())){
                    dividendoAcumulado += (dividendoPorAccion * cantidad);
                } else if (fechaCompra.isBefore(fechaHoy)){
                    dividendoAcumulado += (dividendoPorAccion * cantidad);
                }
                //Avanza al proximo mes
                fechaCompra = fechaCompra.plusMonths(frecuenciaDividendos);
            }

        }
        return dividendoAcumulado;
    }

    public float calcularDividendoFuturo(int meses){
        LocalDate fechaCompra = getFechaInicio();
        LocalDate fechaFin = LocalDate.now();
        fechaFin = fechaFin.plusMonths(meses);
        //Solo se calcula el dividiendo acumulado si es diferente de cero
        if(getDividendoPorAccion() != 0){
            while(!fechaCompra.isAfter(fechaFin)){
                //Se debe acumular el dividendo si es el primero del mes
                if(fechaCompra.getDayOfMonth() == 1 && fechaCompra.equals(getFechaInicio())){
                    dividendoAcumulado += (dividendoPorAccion * cantidad);
                } else if (fechaCompra.isBefore(fechaFin)){
                    dividendoAcumulado += (dividendoPorAccion * cantidad);
                }
                //Avanza al proximo mes
                fechaCompra = fechaCompra.plusMonths(frecuenciaDividendos);
            }

        }
        return dividendoAcumulado;
    }

    public float obtenerPrecioActual() throws IOException {
       String APIKEY = "NAN1GLGHNLYTMDCH";
       String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + this.simbolo + "&apikey=" + APIKEY;
        HttpURLConnection con = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            // Establecer conexión
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            // Leer la respuesta
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                response.append(linea);
            }
        } finally {
            // Cerrar conexiones
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }

        // Extraer el precio actual usando regex
        String jsonString = response.toString();
        String priceValue = extraerPrecioActual(jsonString);
        if (priceValue != null) {
            precioActual = Float.parseFloat(priceValue);
        } else {
            precioActual = 0.0f;
        }

        return precioActual;
    }
    private String extraerPrecioActual(String jsonString) {
        // Define el patrón regex para encontrar "05. price": "valor"
        String regex = "\"05\\. price\":\\s*\"([\\d\\.]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonString);

        // Encuentra el valor de price si hay coincidencia
        if (matcher.find()) {
            return matcher.group(1); // Devuelve el valor encontrado
        } else {
            return null; // Devuelve null si no se encuentra
        }
    }

    private List<Float> obtenerPreciosMensuales() throws IOException {
        List<Float> preciosMensuales = new ArrayList<>();
        String APIKEY = "NAN1GLGHNLYTMDCH";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + this.simbolo + "&apikey=" + APIKEY;
        HttpURLConnection con = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            // Establecer conexión
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            // Leer la respuesta
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                response.append(linea);
            }
        } finally {
            // Cerrar conexiones
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }

        // Procesar el JSON para extraer los precios de cierre
        String jsonString = response.toString();
        preciosMensuales = extraerPreciosMensuales(jsonString);

        return preciosMensuales;
    }

    //Metodo para encontrar el valor de cierre dentro de la respuesta de la API por día siguiendo un patrón de formato fecha y 4. close.
    private List<Float> extraerPreciosMensuales(String jsonString) {
        List<Float> preciosMensuales = new ArrayList<>();
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaInicio = fechaHoy.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Pattern pattern = Pattern.compile("\"(\\d{4}-\\d{2}-\\d{2})\":\\s*\\{[^}]*\"4. close\":\\s*\"([\\d\\.]+)\"");
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            String fechaStr = matcher.group(1);
            String precioStr = matcher.group(2);

            LocalDate fecha = LocalDate.parse(fechaStr, formatter);
            if (!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaHoy)) {
                preciosMensuales.add(Float.parseFloat(precioStr));
            }
        }

        return preciosMensuales;
    }

    private List<Float> obtenerPreciosAnuales()throws IOException {
        List<Float> preciosAnuales = new ArrayList<>();
        String APIKEY = "NAN1GLGHNLYTMDCH";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + this.simbolo + "&apikey=" + APIKEY;
        HttpURLConnection con = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            // Establecer conexión
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            // Leer la respuesta
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                response.append(linea);
            }
        } finally {
            // Cerrar conexiones
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }

        // Procesar el JSON para extraer los precios de cierre
        String jsonString = response.toString();
        preciosAnuales = extraerPreciosAnuales(jsonString);

        return preciosAnuales;
    }

    private List<Float> extraerPreciosAnuales(String jsonString) {
        List<Float> preciosAnuales = new ArrayList<>();
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaInicio = fechaHoy.minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Pattern pattern = Pattern.compile("\"(\\d{4}-\\d{2}-\\d{2})\":\\s*\\{[^}]*\"4. close\":\\s*\"([\\d\\.]+)\"");
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            String fechaStr = matcher.group(1);
            String precioStr = matcher.group(2);

            LocalDate fecha = LocalDate.parse(fechaStr, formatter);
            if(!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaHoy)){
                preciosAnuales.add(Float.parseFloat(precioStr));
            }
        }
        System.out.println(preciosAnuales);
        return preciosAnuales;
    }

}
