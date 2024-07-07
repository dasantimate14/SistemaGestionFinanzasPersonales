package sistemagestionfinanzas;

public class TarjetaCredito {
    private String tipoTarjeta;
    private float limiteCredito;
    private float saldoActual;
    private float tasaInteres;
    private String numero;
    private float creditoUsado;

    // Constructor
    public TarjetaCredito(String tipoTarjeta, float limiteCredito, float saldoActual, float tasaInteres, String numero) {
        this.tipoTarjeta = tipoTarjeta;
        this.limiteCredito = limiteCredito;
        this.saldoActual = saldoActual;
        this.tasaInteres = tasaInteres;
        this.numero = numero;
        this.creditoUsado = calcularCreditoUsado();
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
}