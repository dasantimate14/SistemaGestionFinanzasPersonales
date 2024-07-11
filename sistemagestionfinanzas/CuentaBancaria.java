package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CuentaBancaria extends FinanceItem{
    //Declaración de atributos
    private String banco;
    private int numeroCuenta;
    private String tipoCuenta;
    private static int cantidadInstancias;
    private static List<CuentaBancaria> instanciasCuentasBancarias;

    //Constructor
    CuentaBancaria(String nombre, String  descripcion, float montoOriginal, String tipo, float tasaInteres, LocalDate fechaInicio, String banco, int numeroCuenta, String tipoCuenta) {
        super(nombre, descripcion, montoOriginal, tipo, tasaInteres, fechaInicio);
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.montoActual = montoOriginal;
    }

    //Metodos get y set

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
        float valorTotalCuentasBancarias = 0;
        float valorTotalActivos = 0;
        float porcentajeRepresentacion = 0;
        for (FinanceItem activo : activosPasivos) {
            valorTotalActivos += activo.getMontoActual();
            if (activo instanceof CuentaBancaria) {
                CuentaBancaria cuenta = (CuentaBancaria) activo;
                valorTotalCuentasBancarias += cuenta.getMontoActual();
            }
        }
        porcentajeRepresentacion = (valorTotalCuentasBancarias / valorTotalActivos)*100;
        System.out.println("El porcentaje de Representación de todaas las Cuentas Bancarias es " + porcentajeRepresentacion + " con un valor de " + valorTotalCuentasBancarias);
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
