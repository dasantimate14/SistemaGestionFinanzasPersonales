package Grafico;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import sistemagestionfinanzas.*;

import javax.swing.*;
import java.awt.*;

import static sistemagestionfinanzas.Usuario.usuario_actual;

public class PastelIngreso extends JPanel {
    public PastelIngreso() {
        // Crear el panel del gráfico
        ChartPanel chartPanel = new ChartPanel(crearGrafico());
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel);
    }

    private JFreeChart crearGrafico() {
        // Crear un conjunto de datos
        PieDataset dataset = crearDataset();

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Ingresos y Gastos",  // Título del gráfico
                dataset,                             // Datos
                true,                                // Incluir leyenda
                true,
                false
        );

        // Personalizar la gráfica
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Cuenta Bancaria", new Color(0, 255, 0));
        plot.setSectionPaint("Plazo Fijo", new Color(255, 0, 0));
        plot.setSectionPaint("Ingreso", new Color(0, 0, 255));

        return chart;
    }

    private PieDataset crearDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        float porcentaje_cuenta_bancaria = CuentaBancaria.calcularPorcentajeRepresentacionSubclase(usuario_actual.getActivos());
        float porcentaje_plazo_fijo = PlazoFijo.calcularPorcentajeRepresentacionSubclase(usuario_actual.getActivos());
        float porcentaje_stock = Stock.calcularPorcentajeRepresentacionSubclase(usuario_actual.getActivos());

        dataset.setValue("Cuenta Bancaria", porcentaje_cuenta_bancaria);
        dataset.setValue("Plazo Fijo", porcentaje_plazo_fijo);
        dataset.setValue("Stock", porcentaje_stock);

        return dataset;
    }
}
