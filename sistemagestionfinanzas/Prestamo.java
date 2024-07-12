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
        return saldoPendiente - (cuotaMensual * (LocalDate.now().until(fechaVencimiento, ChronoUnit.MONTHS)));
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
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
        return calcularPagoMensual();
    }

    @Override
    protected float calcularPromedioAnual() throws IOException {
        return calcularPagoMensual() * 12;
    }

    @Override
    protected void actualizarInformacion() throws IOException {
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
        return 0;
    }

    public void setFrecuenciaPago(int nuevaFrecuenciaPago) {
    }

    public float calcularPagoMensual() {
        float tasaInteresMensual = super.getTasaInteres() / 12 / 100;
        int numeroPagos = plazo * 12;
        return (saldoPendiente * tasaInteresMensual) / (1 - (float)Math.pow(1 + tasaInteresMensual, -numeroPagos));
    }

    public float calcularTipoRestante() {
        return saldoPendiente * (super.getTasaInteres() / 100);
    }

    public float calcularSaldoPendiente() {
        return saldoPendiente - (cuotaMensual * plazo);
    }
    public float calcularInteresAcumulado() {
        float interesMensual = super.getTasaInteres() / 12 / 100;
        int numeroPagos = plazo * 12;
        return numeroPagos * cuotaMensual * interesMensual;
    }

    public float calcularInteresPendiente() {
        float interesMensual = super.getTasaInteres() / 12 / 100;
        return saldoPendiente * interesMensual * (plazo - (LocalDate.now().until(fechaVencimiento, ChronoUnit.MONTHS)));
    }
}
