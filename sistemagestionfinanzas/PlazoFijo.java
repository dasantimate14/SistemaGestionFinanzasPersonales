package sistemagestionfinanzas;

import java.time.LocalDate;

public class PlazoFijo {
    private int plazo;
    private LocalDate fecha_final;
    private CuentaBancaria cuenta;

    public PlazoFijo(int plazo, CuentaBancaria cuenta) {
        this.plazo = plazo;
        this.cuenta = cuenta;
        this.fecha_final = LocalDate.now().plusMonths(plazo);
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int nuevo_plazo) {
        this.plazo = nuevo_plazo;
        this.fecha_final = LocalDate.now().plusMonths(nuevo_plazo);
    }

    public LocalDate getFechaFinal() {
        return fecha_final;
    }

    public void setFechaFinal(LocalDate nueva_fecha_final) {
        this.fecha_final = nueva_fecha_final;
    }

    public CuentaBancaria getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaBancaria cuenta) {
        this.cuenta = cuenta;
    }

    public void depositarInteres() {
        float interes_acumulado = calcularInteresAcumulado();
        cuenta.depositarMonto(interes_acumulado);
    }

    public float calcularInteresAcumulado() {
        // Asumiendo que la tasa de interés está en la cuenta y es anual
        float tasa_interes = cuenta.getInteres(); // Obtenemos la tasa de interés de la cuenta
        float monto_original = cuenta.getMontoOriginal();
        int meses = plazo;
        float interes_acumulado = monto_original * (tasa_interes / 100) * (meses / 12.0f);
        return interes_acumulado;
    }

    public float calcularMontoFinal() {
        float monto_original = cuenta.getMontoOriginal();
        float interes_acumulado = calcularInteresAcumulado();
        return monto_original + interes_acumulado;
    }
}
