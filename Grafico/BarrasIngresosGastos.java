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

public class BarrasIngresosGastos extends JPanel {

    public BarrasIngresosGastos() {
        // Crear el panel del gráfico
        ChartPanel chartPanel = new ChartPanel(crearGrafico());
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 600));
        add(chartPanel);
    }

    private JFreeChart crearGrafico() {
        // Crear un conjunto de datos
        CategoryDataset dataset = crearDataset();

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Comparación de Ingresos y Gastos",   // Título del gráfico
                "Mes",                                // Eje X
                "Monto",                              // Eje Y
                dataset,                              // Datos
                PlotOrientation.VERTICAL,             // Orientación
                true,                                 // Mostrar la leyenda
                true,                                 // Mostrar herramientas de gráficos
                false                                 // Mostrar URLs
        );

        return chart;
    }

    private CategoryDataset crearDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate fecha_actual = LocalDate.now();
        int mes_actual = fecha_actual.getMonthValue();
        List<Float> ingresos_ultimos_meses = Ingreso.obtenerIngresosUltimosMeses(Usuario.getIdUsuarioActual());
        List<Float> gastos_ultimos_meses = Gasto.obtenerGastosUltimosMeses(Usuario.getIdUsuarioActual());

        // Asumiendo que las listas contienen los datos de los últimos 12 meses en orden cronológico
        int num_meses = ingresos_ultimos_meses.size();

        // Agregar ingresos al dataset
        for (int i = 0; i < num_meses; i++) {
            System.out.println(ingresos_ultimos_meses.get(i));
            // Calcular el mes correspondiente para el ingreso actual
            int mes = (mes_actual - i - 1 + 12) % 12 + 1;
            String nombre_mes = LocalDate.of(2024, mes, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            dataset.addValue(ingresos_ultimos_meses.get(i), "Ingreso", nombre_mes);
        }

        // Agregar gastos al dataset
        for (int i = 0; i < num_meses; i++) {
            // Calcular el mes correspondiente para el gasto actual
            int mes = (mes_actual - i - 1 + 12) % 12 + 1;
            String nombre_mes = LocalDate.of(2024, mes, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            dataset.addValue(gastos_ultimos_meses.get(i), "Gasto", nombre_mes);
        }

        return dataset;
    }
}

