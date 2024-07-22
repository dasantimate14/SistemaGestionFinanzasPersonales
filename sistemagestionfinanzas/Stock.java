package sistemagestionfinanzas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private float dividendo_estimado;
    private int frecuencia_dividendos;
    public static int cantidad_instancias = 0;
    public static List<Stock> instancias_stocks = new ArrayList<>();

    public Stock(String nombre, String descripcion,  LocalDate fecha_inicio,
                 String nombre_empresa, String simbolo, int cantidad, float precio_compra, String sector, float dividendo_por_accion, int frecuencia_dividendos){
        super(nombre, descripcion, (cantidad* precio_compra), "Activo", fecha_inicio);
        this.nombre_empresa = nombre_empresa;
        this.simbolo = simbolo;
        this.cantidad = cantidad;
        this.precio_compra = precio_compra;
        this.sector = sector;
        this.dividendo_por_accion = dividendo_por_accion;
        this.frecuencia_dividendos = frecuencia_dividendos;

        //Se guarda la instancia dentro de un arreglo que pertenece a la clase misma y no a la instancia
        instancias_stocks.add(this);
        cantidad_instancias++;
    }

    // Constructor con dividendoPorAccion por defecto
    public Stock(String nombre, String descripcion,  LocalDate fechaInicio,
                 String nombreEmpresa, String simbolo, int cantidad, float precio_compra, String sector) {
        this(nombre, descripcion, fechaInicio, nombreEmpresa, simbolo, cantidad, precio_compra, sector, 0.0f, 0);
    }

    //Metodos get y set de la clase
    public String getNombreEmpresa() {return nombre_empresa;}
    public String getSimbolo() {return simbolo;}
    public int getCantidad() {return cantidad;}
    public float getPrecioCompra() {return precio_compra;}
    public String getSector() {return sector;}
    public float getDividendoPorAccion() {return dividendo_por_accion;}
    public float getDividendoAcumulado(){return dividendo_acumulado;}
    public float getDividendoEstimado(){return dividendo_estimado;}
    public static int getCantidadInstancias() {return cantidad_instancias;}
    public float getPrecioActual(){return precio_actual;}
    public int getFrecuenciaDividendos(){return frecuencia_dividendos;}

    public void setNombreEmpresa(String nombre_empresa){this.nombre_empresa = nombre_empresa;}
    public void setSimbolo(String simbolo){this.simbolo = simbolo;}
    public void setCantidad(int cantidad){this.cantidad = cantidad;}
    public void setPrecioCompra(float precio_compra){this.precio_compra = precio_compra;}
    public void setSector(String sector){this.sector = sector;}
    public void setDividendoPorAccion(float dividendo_por_accion){this.dividendo_por_accion = dividendo_por_accion;}
    public void setDividendoAcumulado(float dividendo_acumulado){this.dividendo_acumulado = dividendo_acumulado;}
    public void setDividendoEstimado(float dividendo_estimado){this.dividendo_estimado = dividendo_estimado;}
    public void setPrecioActual(float precio_actual){this.precio_actual = precio_actual;}

    @Override
    protected float calcularValorActual() throws IOException {
        setMontoActual((obtenerPrecioActual()*getCantidad()) + calcularDividendoAcumulado());
        return getMontoActual();
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre Empresa: ").append(nombre_empresa).append("\n");
        sb.append("Símbolo: ").append(simbolo).append("\n");
        sb.append("Cantidad: ").append(cantidad).append("\n");
        sb.append("Precio de Compra: ").append(precio_compra).append("\n");
        sb.append("Dividendo por Acción: ").append(dividendo_por_accion).append("\n");
        sb.append("Frecuencia de Pago de Dividendos: ").append(frecuencia_dividendos).append("\n");
        sb.append("Precio Actual: ").append(precio_actual).append("\n");
        sb.append("Dividendo Acumulado: ").append(dividendo_acumulado).append("\n");
        sb.append("Sector: ").append(sector).append("\n");
        return sb;
    }

    public static float calcularPorcentajeRepresentacionSubclase(List<FinanceItem> activosPasivos) {
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
        return porcentajeRepresentacion;
    }

    @Override
    public float calcularPromedioMensual() throws IOException {
        List<Float> precios_mensuales = obtenerPreciosMensuales();
        if (precios_mensuales.isEmpty()) {
            return 0.0f;
        }

        float suma_precios = 0.0f;
        for (float precio : precios_mensuales) {
            suma_precios += precio;
        }
        setPromedioMensual(redonderCantidad(suma_precios / precios_mensuales.size()));

        return getPromedioMensual();
    }

    @Override
    public float calcularPromedioAnual() throws IOException {
        float suma_promedio = 0.0f;
        List<Float> precios_anuales = obtenerPreciosAnuales();
        if(precios_anuales.isEmpty()){
            return 0.0f;
        }
        for (float precio : precios_anuales) {
            suma_promedio += precio;
        }
        return redonderCantidad(suma_promedio / precios_anuales.size());
    }

    @Override
    public void actualizarInformacion() throws IOException {
        setGanaciaPerdida(calcularGanaciaPerdida());
        setMontoActual(calcularValorActual());
        setDividendoAcumulado(calcularDividendoAcumulado());
        setPorcentajeGanancia(calcularPorcentajeGananciaPerdida());
        setPromedioMensual(calcularPromedioMensual());
        setPrecioActual(obtenerPrecioActual());
    }

    //Metodo para obtener el porcentaje de representacion de una instancia de stock
    public void calcularPorcentajeRepresentacionPorSimbolo(String simbolo){
        float valor_total_stocks = 0;
        float valor_stock_simbolo = 0;
        float porcentaje_representacion = 0;
        //Por cada stock que coincida con el simbolo se suma su monto del simbolo y por cada stock se suma a una variable de todos los stocks
        for(Stock instanciaStock : instancias_stocks){
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
                    dividendo_acumulado += redonderCantidad(dividendo_por_accion * cantidad);
                }
                //Avanza al proximo mes
                fecha_compra = fecha_compra.plusMonths(frecuencia_dividendos);
            }

        }
        return dividendo_acumulado;
    }

    //Metodo para calcular una cantidad total de dividendo acumulado que puede tener una accion en cierta cantidad de meses
    public float calcularDividendoFuturo(int meses){
        LocalDate fecha_compra = getFechaInicio();
        LocalDate fecha_fin = LocalDate.now();
        fecha_fin = fecha_fin.plusMonths(meses);
        //Solo se calcula el dividiendo acumulado si es diferente de cero
        if(getDividendoPorAccion() != 0){
            while(!fecha_compra.isAfter(fecha_fin)){
                //Se debe acumular el dividendo si es el primero del mes
                if(fecha_compra.getDayOfMonth() == 1 || fecha_compra.isBefore(fecha_fin)){
                    dividendo_estimado += (dividendo_por_accion * cantidad);
                }
                //Avanza al proximo mes
                fecha_compra = fecha_compra.plusMonths(frecuencia_dividendos);
            }

        }
        return getDividendoEstimado();
    }

    public float obtenerPrecioActual() throws IOException {
       String API_KEY = "TX51VG4HOMO73PGY";
       String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + getSimbolo() + "&apikey=" + API_KEY;
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
        System.out.println(json_string);
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
        String API_KEY = "TX51VG4HOMO73PGY";
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
        String API_KEY = "TX51VG4HOMO73PGY";
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

    public void guardarStockBaseDatos(String id_usuario){
        //Consulta para guardar el objeto stock en la base de datos
        String consulta_registro = "INSERT INTO stocks (id, nombre, descripcion, montoOriginal, fechaInicio, nombreEmpresa, simbolo, cantidad, precioCompra, sector, dividendoPorAccion, frecuenciaDividendos, idUsuario) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Arreglo de los parametros para la consulta
        String[] parametros = {getNombre(), getDescripcion(), String.valueOf(getMontoOriginal()), String.valueOf(getFechaInicio()), getNombreEmpresa(), getSimbolo(), String.valueOf(getCantidad()), String.valueOf(getPrecioCompra()), getSector(), String.valueOf(getDividendoPorAccion()), String.valueOf(getFrecuenciaDividendos()), id_usuario};

        //Registro en la base de datos
        try{
            BaseDeDatos.establecerConexion();
            boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registro_exitoso){
                System.out.println("Registro exitoso de Stock." );
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    public static void obtenerStocksBaseDatos(String id_usuario) throws SQLException {
        Stock stocks = null;
        String consulta = "SELECT * FROM stocks WHERE idUsuario = ?";
        String[] parametros = {id_usuario};
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);
            if(rs == null){
                throw new SQLException("No se pudo encontrar ningún Stock para este usuario");
            }
            while (rs.next()) {
                // Se leen cada uno de los campos en el resultset para crear el objeto
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                LocalDate fecha_inicio = rs.getDate("fechaInicio").toLocalDate();
                String nombre_empresa = rs.getString("nombreEmpresa");
                String simbolo = rs.getString("simbolo"); // Cambiado de 'numeroCuenta' a 'simbolo'
                int cantidad = rs.getInt("cantidad");
                float precio_compra = rs.getFloat("precioCompra");
                String sector = rs.getString("sector");
                float dividendo_por_accion = rs.getFloat("dividendoPorAccion");
                int frecuencia_dividendos = rs.getInt("frecuenciaDividendos");

                // Se crea el objeto con los datos capturados
                stocks = new Stock(nombre, descripcion, fecha_inicio, nombre_empresa, simbolo, cantidad, precio_compra, sector, dividendo_por_accion, frecuencia_dividendos);
                stocks.setId(id);
                }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

}
