package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class Prestamo extends FinanceItem{

    private String tipoPrestamo;
     private float saldoPendiente;
     private int plazo;
     private LocalDate fechaVencimiento;
     private boolean estatus;
     private float cuotaMensual;

    public Prestamo(String nombre, String descripcion, float montoOriginal, String tipo, float tasaInteres, LocalDate fechaInicio, String tipoPrestamo,
    float saldoPendiente, int plazo, LocalDate fechaVencimiento, float cuotaMensual) {
        super(nombre, descripcion, montoOriginal, tipo, tasaInteres, fechaInicio);
        this.tipoPrestamo = tipoPrestamo;
        this.saldoPendiente = saldoPendiente;
        this.plazo = plazo;
        this.fechaVencimiento = fechaVencimiento;
        this.cuotaMensual = cuotaMensual;

        this.estatus = false;
    }

    @Override
    protected float calcularValorActual() throws IOException {
        return 0;
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        return null;
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
