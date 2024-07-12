package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo extends FinanceItem {
    private String tipoPrestamo;
    private float saldoPendiente;
    private int plazo;
    private LocalDate fechaVencimiento;
    private boolean estatus;
    private float cuotaMensual;

    public Prestamo(String nombre, String descripcion, float montoOriginal, String tipo, float tasaInteres, LocalDate fechaInicio,
                    String tipoPrestamo, float saldoPendiente, int plazo, LocalDate fechaVencimiento, float cuotaMensual) {
        super(nombre, descripcion, montoOriginal, tipo, tasaInteres, fechaInicio);
        this.tipoPrestamo = tipoPrestamo;
        this.saldoPendiente = saldoPendiente;
        this.plazo = plazo;
        this.fechaVencimiento = fechaVencimiento;
        this.estatus = false;
        this.cuotaMensual = cuotaMensual;
    }
    @Override
    protected float calcularValorActual() throws IOException {
        // Implementa la lógica para calcular el valor actual
        return saldoPendiente - (cuotaMensual * (LocalDate.now().until(fechaVencimiento, ChronoUnit.MONTHS)));
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        // Implementa la lógica para obtener la información de la subclase
        StringBuilder info = new StringBuilder();
        info.append("Tipo de Préstamo: ").append(tipoPrestamo).append("\n");
        info.append("Saldo Pendiente: ").append(saldoPendiente).append("\n");
        info.append("Plazo: ").append(plazo).append("\n");
        info.append("Fecha de Vencimiento: ").append(fechaVencimiento).append("\n");
        info.append("Estatus: ").append(estatus ? "Activo" : "Inactivo").append("\n");
        info.append("Cuota Mensual: ").append(cuotaMensual).append("\n");
        return info;
    }

    @Override
    protected void calcularPorcentajeRepresentacionSubclase(FinanceItem[] activosPasivos) throws IOException {
        // Implementa la lógica para calcular el porcentaje de representación de la subclase
        float totalActivos = 0;
        for (FinanceItem item : activosPasivos) {
            totalActivos += item.calcularValorActual();
        }
        float porcentaje = (calcularValorActual() / totalActivos) * 100;
        System.out.println("Porcentaje de Representación: " + porcentaje + "%");
    }

    @Override
    protected float calcularPromedioMensual() throws SQLException, IOException {
        // Implementa la lógica para calcular el promedio mensual
        return calcularPagoMensual();
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        // Implementa la lógica para calcular el promedio anual
        return calcularPagoMensual() * 12;
    }

    @Override
    protected void actualizarInformacion() throws IOException {
        // Implementa la lógica para actualizar la información
        this.cuotaMensual = calcularPagoMensual();
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String nuevoTipoPrestamo) {
        this.tipoPrestamo = nuevoTipoPrestamo;
    }

    public float getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(float nuevoSaldoPendiente) {
        this.saldoPendiente = nuevoSaldoPendiente;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int nuevoPlazo) {
        this.plazo = nuevoPlazo;
    }

    public LocalDate getFechaDesembolso() {
        // Suponiendo que la fecha de desembolso es igual a la fecha de inicio
        return super.getFechaInicio();
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate nuevaFechaVencimiento) {
        this.fechaVencimiento = nuevaFechaVencimiento;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean nuevoEstatus) {
        this.estatus = nuevoEstatus;
    }

    public int getFrecuenciaPago() {
        // Suponiendo pagos mensuales
        return 1;
    }

    public void setFrecuenciaPago(int nuevaFrecuenciaPago) {
        // Implementa lógica para establecer la nueva frecuencia de pago si es necesario
    }

    public float calcularPagoMensual() {
        // Implementa la lógica para calcular el pago mensual basado en el saldo pendiente, tasa de interés y plazo
        float tasaInteresMensual = super.getTasaInteres() / 12 / 100;
        int numeroPagos = plazo * 12;
        return (saldoPendiente * tasaInteresMensual) / (1 - (float)Math.pow(1 + tasaInteresMensual, -numeroPagos));
    }

    public float calcularTipoRestante() {
        // Implementa la lógica para calcular el tipo restante
        return saldoPendiente * (super.getTasaInteres() / 100);
    }

    public float calcularSaldoPendiente() {
        // Implementa la lógica para calcular el saldo pendiente basado en pagos realizados y plazo
        return saldoPendiente - (cuotaMensual * plazo);
    }

    public float calcularInteresAcumulado() {
        // Implementa la lógica para calcular el interés acumulado
        float interesMensual = super.getTasaInteres() / 12 / 100;
        int numeroPagos = plazo * 12;
        return numeroPagos * cuotaMensual * interesMensual;
    }

    public float calcularInteresPendiente() {
        // Implementa la lógica para calcular el interés pendiente
        float interesMensual = super.getTasaInteres() / 12 / 100;
        return saldoPendiente * interesMensual * (plazo - (LocalDate.now().until(fechaVencimiento, ChronoUnit.MONTHS)));
    }
}
