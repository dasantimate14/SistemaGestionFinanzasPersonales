package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class CuentaBancaria extends FinanceItem{
    //Declaración de atributos
    private String banco;
    private int numeroCuenta;
    private String tipoCuenta;

    //Constructor
    CuentaBancaria(String nombre, String  descripcion, float montoOriginal, String tipo, float tasaInteres, LocalDate fechaInicio, String banco, int numeroCuenta, String tipoCuenta) {
        super(nombre, descripcion, montoOriginal, tipo, tasaInteres, fechaInicio);
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.montoActual = montoOriginal;
    }

    @Override
    protected float calcularValorActual() throws IOException {
        return 0;
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre Banco: ").append(banco).append("\n");
        sb.append("Número de Cuenta: ").append(numeroCuenta).append("\n");
        sb.append("Tipo de Cuenta: ").append(tipoCuenta).append("\n");
        return sb;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) {

    }

    @Override
    protected float calcularPromedioMensual() throws SQLException, IOException {
        return 0;
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        return 0;
    }

    @Override
    protected void actualizarInformacion() throws IOException {

    }
}
