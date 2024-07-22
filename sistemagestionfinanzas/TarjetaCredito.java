package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarjetaCredito extends FinanceItem {
    private String tipo_tarjeta;
    private float limite_credito;
    private float saldo_actual;
    private int numero;
    private float credito_usado;
    private CuentaBancaria cuenta_bancaria;
    public int cantidad_instancias;
    public static List<TarjetaCredito> instanciasTarjetas = new ArrayList<>();

    // Constructor
    public TarjetaCredito(String nombre, String descripcion, float monto_original, LocalDate fecha_inicio,
                          String tipo_tarjeta, float limite_credito, int numero, CuentaBancaria cuenta_bancaria) {
        super(nombre, descripcion, monto_original, "Pasivo", 0, fecha_inicio);
        this.tipo_tarjeta = tipo_tarjeta;
        this.limite_credito = limite_credito;
        this.saldo_actual = limite_credito;
        this.numero = numero;
        this.cuenta_bancaria = cuenta_bancaria;
        this.credito_usado = calcularCreditoUsado();
        instanciasTarjetas.add(this);
        cantidad_instancias++;
        System.out.println("Tarjeta creada: " + this.getNumero());
    }

    // Métodos para calcular el crédito usado
    private float calcularCreditoUsado() {
        return limite_credito - saldo_actual;
    }

    // Getters y Setters
    public String getTipoTarjeta() {
        return tipo_tarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipo_tarjeta = tipoTarjeta;
    }

    public float getLimiteCredito() {
        return limite_credito;
    }

    public void setLimiteCredito(float limiteCredito) {
        this.limite_credito = limiteCredito;
        this.credito_usado = calcularCreditoUsado();
    }

    public float getSaldoActual() {
        return saldo_actual;
    }

    public void setSaldoActual(float saldoActual) {
        this.saldo_actual = saldoActual;
        this.credito_usado = calcularCreditoUsado();
    }

    public float getTasaInteres() {
        return tasa_interes;
    }

    public void setTasaInteres(float tasaInteres) {
        this.tasa_interes = tasa_interes;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public float getCreditoUsado() {
        return credito_usado;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuenta_bancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuenta_bancaria) {
        this.cuenta_bancaria = cuenta_bancaria;
    }

    // Método para calcular el porcentaje de representación
    public void calcularPorcentajeRepresentacionTarjeta() {
        float totalTarjetas = 0;
        for (TarjetaCredito tarjeta : instanciasTarjetas) {
            totalTarjetas += tarjeta.getSaldoActual();
        }
        float porcentaje = (getSaldoActual() / totalTarjetas) * 100;
        System.out.println("Porcentaje de Representación: " + porcentaje + "%");
    }

    // Método para obtener la cantidad de instancias
    public static int obtenerCantidadInstancias() {
        return instanciasTarjetas.size();
    }

    // Método para obtener las instancias de la clase
    public static List<TarjetaCredito> obtenerInstancias() {
        return new ArrayList<>(instanciasTarjetas);
    }

    @Override
    protected float calcularValorActual() throws IOException {
        return 0;
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tipo de Tarjeta: ").append(tipo_tarjeta).append("\n");
        sb.append("Límite de Crédito: ").append(limite_credito).append("\n");
        sb.append("Saldo Actual: ").append(saldo_actual).append("\n");
        sb.append("Número: ").append(numero).append("\n");
        sb.append("Crédito Usado: ").append(credito_usado).append("\n");
        return sb;
    }

    public static float calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        // Implementación específica no necesaria para esta prueba
        return 0;
    }

    @Override
    protected float calcularPromedioMensual() throws IOException {
        return 0;
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        return 0;
    }

    @Override
    public void actualizarInformacion() throws IOException {
        // Implementación específica no necesaria para esta prueba
    }

    // Método para pagar la tarjeta de crédito
    public void pagarTarjeta(float monto) throws SQLException {
        LocalDate fecha_hoy = LocalDate.now();
        if (cuenta_bancaria != null && cuenta_bancaria.getMontoActual() >= monto) {
            // Retirar monto de la cuenta bancaria
            cuenta_bancaria.retirarMonto(monto);

            // Descontar el monto del saldo de la tarjeta de crédito
            this.saldo_actual -= monto;

            // Si el saldo actual es menor o igual a cero, ajustar valores
            if (this.saldo_actual <= 0) {
                this.saldo_actual = 0;
                this.credito_usado = 0;
                System.out.println("Crédito disponible: " + limite_credito);
                System.out.println("Crédito usado: " + credito_usado);
            } else {
                this.credito_usado = calcularCreditoUsado();
            }

            // Registrar el pago como un gasto en la base de datos
            cuenta_bancaria.retirarMonto(monto);
            Gasto pago_tarjeta = new Gasto("Pago Tarejta de Credito", "Se pagó la tarjeta con la terminación " + getNumero(), getCreditoUsado(), fecha_hoy, cuenta_bancaria.getBanco(), 0, "Pago Tarjeta", getCuentaBancaria());
            pago_tarjeta.guardarGastoBaseDatos();

            System.out.println("Pago de tarjeta realizado con éxito y registrado como gasto.");
        } else {
            System.out.println("Fondos insuficientes en la cuenta bancaria para realizar el pago.");
        }
    }

    public void guardarTarjetaBaseDatos(){
        String consulta_registro = "INSERT INTO tarjetas_creditos (id, nombre, descripcion, montoOriginal, fechaInicio, tipoTarjeta, limiteCredito, terminacionTarjeta, idUsuario, idCuentaBancaria) VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String[] parametros = new String[]{
                getNombre(),
                getDescripcion(),
                String.valueOf(getMontoOriginal()),
                String.valueOf(getFechaInicio()),
                getTipoTarjeta(),
                String.valueOf(getLimiteCredito()),
                String.valueOf(getNumero()),
                cuenta_bancaria.getIdUsuario(),
                cuenta_bancaria.getId()
        };

        try {
            BaseDeDatos.establecerConexion();
            boolean registro_exitoso = BaseDeDatos.ejecutarActualizacion(consulta_registro, parametros);
            if (registro_exitoso) {
                System.out.println("Registro exitoso de gasto.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    public static void obtenerTarjetaCreditoBaseDatos(String id_usuario) throws SQLException {
        TarjetaCredito tarjeta_credito = null;
        String consulta = "SELECT * FROM tarjetas_creditos WHERE idUsuario = ?";
        String[] parametro = {id_usuario};
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametro);
            if(rs == null){
                throw new SQLException("No se pudo encontrar ninguna tarjeta de crédito para este usuario");
            }
            while (rs.next()) {
                // Leer cada uno de los campos en el ResultSet para manejar la información
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                float monto_original = rs.getFloat("montoOriginal");
                LocalDate fecha_inicio = rs.getDate("fechaInicio").toLocalDate();
                String tipo_tarjeta = rs.getString("tipoTarjeta");
                float limite_credito = rs.getFloat("limiteCredito");
                int terminacion_tarjeta = rs.getInt("terminacionTarjeta");
                String id_cuenta_bancaria = rs.getString("idCuentaBancaria");

                CuentaBancaria cuenta_viculada = null;
                for(CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                    if(cuenta.getId().equals(id_cuenta_bancaria)) {
                        cuenta_viculada = cuenta;
                        //Se crea el objeto con los datos capturados
                        tarjeta_credito = new TarjetaCredito(nombre, descripcion, monto_original, fecha_inicio, tipo_tarjeta, limite_credito, terminacion_tarjeta, cuenta_viculada);
                        tarjeta_credito.setId(id);
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }
}

