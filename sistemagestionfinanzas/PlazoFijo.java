package sistemagestionfinanzas;

import java.time.LocalDate;

public class PlazoFijo {
    private int plazo;
    private LocalDate fechaFinal;
    private CuentaBancaria cuenta;

    public PlazoFijo(int plazo, CuentaBancaria cuenta) {
        this.plazo = plazo;
        this.cuenta = cuenta;
        this.fechaFinal = LocalDate.now().plusMonths(plazo);
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int nuevoPlazo) {
        this.plazo = nuevoPlazo;
        this.fechaFinal = LocalDate.now().plusMonths(nuevoPlazo);
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate nuevaFechaFinal) {
        this.fechaFinal = nuevaFechaFinal;
    }

    public CuentaBancaria getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaBancaria cuenta) {
        this.cuenta = cuenta;
    }

    public void depositarInteres() {
        float interesAcumulado = calcularInteresAcumulado();
        cuenta.depositarMonto(interesAcumulado);
    }

    public float calcularInteresAcumulado() {
        // Asumiendo que la tasa de interés está en la cuenta y es anual
        float tasaInteres = cuenta.getInteres(); // Obtenemos la tasa de interés de la cuenta
        float montoOriginal = cuenta.getMontoOriginal();
        int meses = plazo;
        float interesAcumulado = montoOriginal * (tasaInteres / 100) * (meses / 12.0f);
        return interesAcumulado;
    }

    public float calcularMontoFinal() {
        float montoOriginal = cuenta.getMontoOriginal();
        float interesAcumulado = calcularInteresAcumulado();
        return montoOriginal + interesAcumulado;
    }
}
