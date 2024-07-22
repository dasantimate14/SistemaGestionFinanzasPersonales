package Grafico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel DashboardPanel;
    private JButton btnMenu1;
    private JButton btnCuentaBancaria;
    private JButton plazosFijosButton;
    private JButton btnIngresos;
    private JButton btnStocks;
    private JButton btnTarjetas;
    private JButton prestamosButton;
    private JPanel ingresos_gastos_anuales_paneles;
    private JPanel pastel_subclase_panel;
    private JPanel ingresos_gastos_mensuales;
    private JLabel label_nombre_usuario;

    public Dashboard() {
        // Configuración de la ventana de inicio de sesión
        setSize(1100, 920);
        setTitle("Menu Principal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(DashboardPanel);

        // Crear y añadir el panel del gráfico al Dashboard
        BarrasIngresosGastosAnuales panel_graficos_anuales = new BarrasIngresosGastosAnuales();
        ingresos_gastos_anuales_paneles.setLayout(new BorderLayout());  // Usar BorderLayout para posicionar el gráfico
        ingresos_gastos_anuales_paneles.add(panel_graficos_anuales, BorderLayout.CENTER);

        // Crear y añadir el panel del gráfico al Dashboard
        BarrasIngresosGastos grafico_barras_mensual = new BarrasIngresosGastos();
        ingresos_gastos_mensuales.setLayout(new BorderLayout());  // Usar BorderLayout para posicionar el gráfico
        ingresos_gastos_mensuales.add(grafico_barras_mensual, BorderLayout.CENTER);

        btnCuentaBancaria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        // Añade ActionListeners
        plazosFijosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlazoFijos newframe = new PlazoFijos();
                newframe.setVisible(true);
                dispose();
            }
        });
        btnIngresos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
                newframe.setVisible(true);
                dispose();
            }
        });
        btnStocks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });

        btnTarjetas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tarjetas newframe = new Tarjetas();
                newframe.setVisible(true);
                dispose();
            }
        });
        prestamosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prestamos newframe = new Prestamos();
                newframe.setVisible(true);
                dispose();
            }
        });

    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dashboard frame = new Dashboard();
                frame.setVisible(true);
            }
        });
    }
}
