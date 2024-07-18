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
    private String nombre_empresa;
    private String simbolo;
    private int cantidad;
    private float precio_compra;
    private String sector;
    private float precio_actual;
    private float dividendo_por_accion;
    private float dividendo_acumulado;
    private float dividendoEstimado;
    private int frecuenciaDividendos;
    private static int cantidadInstancias = 0;
    private static List<Stock> instanciasStocks = new ArrayList<>();

    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precio_compra, String sector, float dividendo_por_accion, int frecuenciaDividendos){
        super(nombre, descripcion, (cantidad* precio_compra), "Activo", tasaInteres, fechaInicio);
        this.nombre_empresa = nombreEmpresa;
        this.simbolo = simbolo;
        this.cantidad = cantidad;
        this.precio_compra = precio_compra;
        this.sector = sector;
        this.dividendo_por_accion = dividendo_por_accion;
        this.frecuenciaDividendos = frecuenciaDividendos;

        //Se guarda la instancia dentro de un arreglo que pertenece a la clase misma y no a la instancia
        instanciasStocks.add(this);
        cantidadInstancias++;
    }

    // Constructor con dividendoPorAccion por defecto
    public Stock(String nombre, String descripcion, float tasaInteres, LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precio_compra, String sector) {
        this(nombre, descripcion, tasaInteres, fechaInicio, nombreEmpresa, simbolo, cantidad, precio_compra, sector, 0.0f, 0);
    }

    //Metodos get y set de la clase
    public String getNombreEmpresa() {return nombre_empresa;}
    public String getSimbolo() {return simbolo;}
    public int getCantidad() {return cantidad;}
    public float getPrecioCompra() {return precio_compra;}
    public String getSector() {return sector;}
    public float getDividendoPorAccion() {return dividendo_por_accion;}
    public float getDividendoAcumulado(){return dividendo_acumulado;}
    public float getDividendoEstimado(){return dividendoEstimado;}
    public static int getCantidadInstancias() {return cantidadInstancias;}
    public float getPrecioActual(){return precio_actual;}
    public int getFrecuenciaDividendos(){return frecuenciaDividendos;}

    public void setNombreEmpresa(String nombre_empresa){this.nombre_empresa = nombre_empresa;}
    public void setSimbolo(String simbolo){this.simbolo = simbolo;}
    public void setCantidad(int cantidad){this.cantidad = cantidad;}
    public void setPrecioCompra(float precio_compra){this.precio_compra = precio_compra;}
    public void setSector(String sector){this.sector = sector;}
    public void setDividendoPorAccion(float dividendo_por_accion){this.dividendo_por_accion = dividendo_por_accion;}
    public void setDividendoAcumulado(float dividendo_acumulado){this.dividendo_acumulado = dividendo_acumulado;}
    public void setDividendoEstimado(float dividendoEstimado){this.dividendoEstimado = dividendoEstimado;}
    public void setPrecioActual(float precio_actual){this.precio_actual = precio_actual;}

    @Override
    protected float calcularValorActual() throws IOException {
        setPrecioActual(obtenerPrecioActual() + calcularDividendoAcumulado());
        return getPrecioActual();
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre Empresa: ").append(nombre_empresa).append("\n");
        sb.append("Símbolo: ").append(simbolo).append("\n");
        sb.append("Cantidad: ").append(cantidad).append("\n");
        sb.append("Precio de Compra: ").append(precio_compra).append("\n");
        sb.append("Dividendo por Acción: ").append(dividendo_por_accion).append("\n");
        sb.append("Frecuencia de Pago de Dividendos: ").append(frecuenciaDividendos).append("\n");
        sb.append("Precio Actual: ").append(precio_actual).append("\n");
        sb.append("Dividendo Acumulado: ").append(dividendo_acumulado).append("\n");
        sb.append("Sector: ").append(sector).append("\n");
        return sb;
    }

    @Override
    public void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        float valor_total_activos = 0;
        float valorTotalStocks = 0;
        float porcentajeRepresentacion = 0;
        //Por cada activo en el arreglo acumular su valor y si es de la instancia de stock se acumula en su respectiva variable
        for(FinanceItem item : activosPasivos){
            valor_total_activos = item.getMontoActual() + valor_total_activos;
            if(item instanceof Stock){
                Stock stock = (Stock)item;
                valorTotalStocks = stock.getMontoActual() + valorTotalStocks;
            }
        }
        //Calculo de porcentaje
        porcentajeRepresentacion = (valorTotalStocks/valor_total_activos)*100;
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
        setPromedioMensual(sumaPrecios / preciosMensuales.size());

        return getPromedioMensual();
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
        setDividendoAcumulado(calcularDividendoAcumulado());
    }

    //Metodo para obtener el porcentaje de representacion de una instancia de stock
    public void calcularPorcentajeRepresentacionPorSimbolo(String simbolo){
        float valor_total_stocks = 0;
        float valor_stock_simbolo = 0;
        float porcentaje_representacion = 0;
        //Por cada stock que coincida con el simbolo se suma su monto del simbolo y por cada stock se suma a una variable de todos los stocks
        for(Stock instanciaStock : instanciasStocks){
            valor_total_stocks = instanciaStock.getMontoActual() + valor_total_stocks;
            if(instanciaStock.getSimbolo().equals(simbolo)){
                valor_stock_simbolo = instanciaStock.getMontoActual() + valor_stock_simbolo;
            }
        }
        //Calcular Porcentaje
        porcentaje_representacion = (valor_stock_simbolo / valor_total_stocks)*100;
        System.out.println("El porcentaje de representación de todos los Stocks con símbolo " + simbolo +  " es " + porcentaje_representacion + "% con un valor de " + valor_stock_simbolo);
    }

    public float calcularDividendoAcumulado(){
        LocalDate fecha_hoy = LocalDate.now();
        LocalDate fecha_compra = getFechaInicio();
        //Solo se calcula el dividiendo acumulado si es diferente de cero
        if(getDividendoPorAccion() != 0){
            while(!fecha_compra.isAfter(fecha_hoy)){
                //Se debe acumular el dividendo si es el primero del mes
                if(fecha_compra.getDayOfMonth() == 1 || fecha_compra.isBefore(fecha_hoy)){
                    dividendo_acumulado += (dividendo_por_accion * cantidad);
                }
                //Avanza al proximo mes
                fecha_compra = fecha_compra.plusMonths(frecuenciaDividendos);
            }

        }
        return dividendo_acumulado;
    }

    public float calcularDividendoFuturo(int meses){
        LocalDate fecha_compra = getFechaInicio();
        LocalDate fecha_fin = LocalDate.now();
        fecha_fin = fecha_fin.plusMonths(meses);
        //Solo se calcula el dividiendo acumulado si es diferente de cero
        if(getDividendoPorAccion() != 0){
            while(!fecha_compra.isAfter(fecha_fin)){
                //Se debe acumular el dividendo si es el primero del mes
                if(fecha_compra.getDayOfMonth() == 1 || fecha_compra.isBefore(fecha_fin)){
                    dividendo_acumulado += (dividendo_por_accion * cantidad);
                }
                //Avanza al proximo mes
                fecha_compra = fecha_compra.plusMonths(frecuenciaDividendos);
            }

        }
        return dividendo_acumulado;
    }

    public float obtenerPrecioActual() throws IOException {
       String API_KEY = "NAN1GLGHNLYTMDCH";
       String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + this.simbolo + "&apikey=" + API_KEY;
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
        String json_string = response.toString();
        String valor_precio = extraerPrecioActual(json_string);
        if (valor_precio != null) {
            precio_actual = Float.parseFloat(valor_precio);
        } else {
            precio_actual = 0.0f;
        }

        return precio_actual;
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
        List<Float> precios_mensuales = new ArrayList<>();
        String API_KEY = "NAN1GLGHNLYTMDCH";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + this.simbolo + "&apikey=" + API_KEY;
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
        String json_string = response.toString();
        precios_mensuales = extraerPreciosMensuales(json_string);

        return precios_mensuales;
    }

    //Metodo para encontrar el valor de cierre dentro de la respuesta de la API por día siguiendo un patrón de formato fecha y 4. close.
    private List<Float> extraerPreciosMensuales(String jsonString) {
        List<Float> precios_mensuales = new ArrayList<>();
        LocalDate fecha_hoy = LocalDate.now();
        LocalDate fecha_inicio = fecha_hoy.minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Pattern pattern = Pattern.compile("\"(\\d{4}-\\d{2}-\\d{2})\":\\s*\\{[^}]*\"4. close\":\\s*\"([\\d\\.]+)\"");
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            String fechaStr = matcher.group(1);
            String precioStr = matcher.group(2);

            LocalDate fecha = LocalDate.parse(fechaStr, formatter);
            if (!fecha.isBefore(fecha_inicio) && !fecha.isAfter(fecha_hoy)) {
                precios_mensuales.add(Float.parseFloat(precioStr));
            }
        }

        return precios_mensuales;
    }

    private List<Float> obtenerPreciosAnuales()throws IOException {
        List<Float> precios_anuales = new ArrayList<>();
        String API_KEY = "NAN1GLGHNLYTMDCH";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + this.simbolo + "&apikey=" + API_KEY;
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
        String json_string = response.toString();
        precios_anuales = extraerPreciosAnuales(json_string);

        return precios_anuales;
    }

    private List<Float> extraerPreciosAnuales(String jsonString) {
        List<Float> precios_anuales = new ArrayList<>();
        LocalDate fecha_hoy = LocalDate.now();
        LocalDate fecha_inicio = fecha_hoy.minusYears(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Pattern pattern = Pattern.compile("\"(\\d{4}-\\d{2}-\\d{2})\":\\s*\\{[^}]*\"4. close\":\\s*\"([\\d\\.]+)\"");
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            String fechaStr = matcher.group(1);
            String precioStr = matcher.group(2);

            LocalDate fecha = LocalDate.parse(fechaStr, formatter);
            if(!fecha.isBefore(fecha_inicio) && !fecha.isAfter(fecha_hoy)){
                precios_anuales.add(Float.parseFloat(precioStr));
            }
        }
        System.out.println(precios_anuales);
        return precios_anuales;
    }

}
