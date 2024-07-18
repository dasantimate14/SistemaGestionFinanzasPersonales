package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarjetaCredito extends FinanceItem {
    private String tipoTarjeta;
    private float limiteCredito;
    private float saldoActual;
    private String numero;
    private float creditoUsado;
    private CuentaBancaria cuentaBancaria;

    private static List<TarjetaCredito> instanciasTarjetas = new ArrayList<>();

    // Constructor
    public TarjetaCredito(String nombre, String descripcion, float montoOriginal, String tipo, float tasaInteres, LocalDate fechaInicio,
                          String tipoTarjeta, float limiteCredito, float saldoActual, String numero, CuentaBancaria cuentaBancaria) {
        super(nombre, descripcion, montoOriginal, tipo, tasaInteres, fechaInicio);
        this.tipoTarjeta = tipoTarjeta;
        this.limiteCredito = limiteCredito;
        this.saldoActual = saldoActual;
        this.numero = numero;
        this.cuentaBancaria = cuentaBancaria;
        this.creditoUsado = calcularCreditoUsado();
        instanciasTarjetas.add(this);
    }

    // Métodos para calcular el crédito usado
    private float calcularCreditoUsado() {
        return limiteCredito - saldoActual;
    }

    // Getters y Setters
    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public float getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(float limiteCredito) {
        this.limiteCredito = limiteCredito;
        this.creditoUsado = calcularCreditoUsado();
    }

    public float getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(float saldoActual) {
        this.saldoActual = saldoActual;
        this.creditoUsado = calcularCreditoUsado();
    }

    public float getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(float tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public float getCreditoUsado() {
        return creditoUsado;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
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
        sb.append("Tipo de Tarjeta: ").append(tipoTarjeta).append("\n");
        sb.append("Límite de Crédito: ").append(limiteCredito).append("\n");
        sb.append("Saldo Actual: ").append(saldoActual).append("\n");
        sb.append("Número: ").append(numero).append("\n");
        sb.append("Crédito Usado: ").append(creditoUsado).append("\n");
        return sb;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {
        // Implementación específica no necesaria para esta prueba
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
    protected void actualizarInformacion() throws IOException {
        // Implementación específica no necesaria para esta prueba
    }

    // Método para pagar la tarjeta de crédito
    public void pagarTarjeta(float monto) throws SQLException {
        if (cuentaBancaria != null && cuentaBancaria.getMontoActual() >= monto) {
            // Retirar monto de la cuenta bancaria
            cuentaBancaria.retirarMonto(monto);

            // Descontar el monto del saldo de la tarjeta de crédito
            this.saldoActual -= monto;

            // Si el saldo actual es menor o igual a cero, ajustar valores
            if (this.saldoActual <= 0) {
                this.saldoActual = 0;
                this.creditoUsado = 0;
                System.out.println("Crédito disponible: " + limiteCredito);
                System.out.println("Crédito usado: " + creditoUsado);
            } else {
                this.creditoUsado = calcularCreditoUsado();
            }

            // Registrar el pago como un gasto en la base de datos
            String consulta = "INSERT INTO gastos (idUsuario, idCuentaBancaria, idTarjetaCredito, montoOriginal, fechaInicio) VALUES (?, ?, ?, ?, ?)";
            String[] parametros = { cuentaBancaria.getIdUsuario(), String.valueOf(cuentaBancaria.getId()), String.valueOf(this.getId()), String.valueOf(monto), String.valueOf(LocalDate.now()) };
            BaseDeDatos.ejecutarActualizacion(consulta, parametros);

            System.out.println("Pago de tarjeta realizado con éxito y registrado como gasto.");
        } else {
            System.out.println("Fondos insuficientes en la cuenta bancaria para realizar el pago.");
        }
    }
}


