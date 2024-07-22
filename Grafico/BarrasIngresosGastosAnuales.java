package Grafico;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import sistemagestionfinanzas.Gasto;
import sistemagestionfinanzas.Ingreso;
import sistemagestionfinanzas.Usuario;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class BarrasIngresosGastosAnuales extends JPanel{
    public BarrasIngresosGastosAnuales(){
        //Crear el panel grafico
        ChartPanel chartPanel = new ChartPanel(crearGrafico());
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 600));
        add(chartPanel);
    }

    private JFreeChart crearGrafico() {
        // Crear un conjunto de datos
        CategoryDataset dataset = crearDataset();

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Comparación de Ingresos y Gastos",   // Título del gráfico
                "Año",                                // Eje X
                "Monto",                              // Eje Y
                dataset,                              // Datos
                PlotOrientation.VERTICAL,             // Orientación
                true,                                 // Mostrar la leyenda
                true,                                 // Mostrar herramientas de gráficos
                false                                 // Mostrar URLs
        );

        return chart;
    }

    private CategoryDataset crearDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate fecha_actual = LocalDate.now();
        int anio_actual = fecha_actual.getYear();

        // Se obtienen las sumatorias de gastos e ingresos para los últimos cinco años
        List<Float> ingresos_ultimos_cinco_anos = Ingreso.obtenerIngresosAnualesRecientes(Usuario.getIdUsuarioActual());
        List<Float> gastos_ultimos_cinco_anos = Gasto.obtenerGastosAnualesRecientes(Usuario.getIdUsuarioActual());

        // Agregar datos para cada serie
        for (int i = 0; i < ingresos_ultimos_cinco_anos.size(); i++) {
            // Calcula el año correspondiente
            int anio = anio_actual - (ingresos_ultimos_cinco_anos.size() - 1 - i);

            // Agregar ingresos y gastos al dataset
            dataset.addValue(gastos_ultimos_cinco_anos.get(i), "Gasto", String.valueOf(anio));
            dataset.addValue(ingresos_ultimos_cinco_anos.get(i), "Ingreso", String.valueOf(anio));

        }

        return dataset;
    }
}
