package Grafico;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import sistemagestionfinanzas.FinanceItem;
import sistemagestionfinanzas.Ingreso;
import sistemagestionfinanzas.PlazoFijo;
import sistemagestionfinanzas.Prestamo;

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
        plot.setSectionPaint("Ingresos", new Color(0, 255, 0));
        plot.setSectionPaint("Gastos", new Color(255, 0, 0));

        return chart;
    }

    private PieDataset crearDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        float porcentaje_prestamo = Prestamo.calcularPorcentajeRepresentacionSubclase(usuario_actual.getActivos());


        dataset.setValue("Ingresos", porcentaje_prestamo);;

        return dataset;
    }
}
