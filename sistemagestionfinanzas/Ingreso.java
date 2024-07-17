package sistemagestionfinanzas;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ingreso extends FinanceItem{
    //Declaracion de atributos
    private String fuente;
    private CuentaBancaria cuentaBancaria;
    private int frencuencia;
    private static int cantidadInstancias;
    private static List<Ingreso> instanciasIngresos;

    //Constructor
    public Ingreso(String nombre, String  descripcion, float montoOriginal, LocalDate fechaInicio, String fuente, CuentaBancaria cuentaBancaria, int frencuencia) {
        super(nombre, descripcion, montoOriginal, "Activo", fechaInicio);
        this.fuente = fuente;
        this.cuentaBancaria = cuentaBancaria;
        this.frencuencia = frencuencia;
        instanciasIngresos.add(this);
        cantidadInstancias ++;
    }
    //Método Constructor para objetos sin frecuencia recurrente de deposito
    public Ingreso(String nombre, String  descripcion, float montoOriginal,
                   LocalDate fechaInicio, String fuente, CuentaBancaria cuentaBancaria){
        this(nombre, descripcion, montoOriginal, fechaInicio, fuente, cuentaBancaria, 0);
    }

    //Metodos get y set
    public String getFuente(){ return fuente;}
    public CuentaBancaria getCuentaBancaria(){return cuentaBancaria;}
    public int getFrencuencia(){return frencuencia;}

    public void setFuente(String fuente){this.fuente = fuente;}
    public void setCuentaBancaria(CuentaBancaria cuentaBancaria){this.cuentaBancaria = cuentaBancaria;}
    public void setFrencuencia(int frencuencia){this.frencuencia = frencuencia;}


    @Override
    protected float calcularValorActual() throws IOException {
        return 0;
    }

    @Override
    protected StringBuilder obtenerInformacionSubclase() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fuente del Ingreso: ").append(fuente).append("\n");
        sb.append("Cuenta Bancaria Asociada: ").append(cuentaBancaria).append("\n");
        sb.append("Frecuencia de deposito: ").append(frencuencia).append("\n");
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
        if(getFrencuencia() != 0){

        }



    }


}
