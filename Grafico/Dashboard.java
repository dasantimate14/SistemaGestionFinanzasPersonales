package Grafico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel dashborad_panel;
    private JButton btnMenu1;
    private JButton btn_cuenta_bancaria;
    private JButton plazos_fijos_button;
    private JButton btn_ingreso;
    private JButton btn_stocks;
    private JButton btn_tarjeta;
    private JButton prestamos_button;
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
        setContentPane(dashborad_panel);

        BarrasIngresosGastosAnuales panel_graficos_anuales = new BarrasIngresosGastosAnuales();
        ingresos_gastos_anuales_paneles.setLayout(new BorderLayout());
        ingresos_gastos_anuales_paneles.add(panel_graficos_anuales, BorderLayout.CENTER);

        BarrasIngresosGastos grafico_barras_mensual = new BarrasIngresosGastos();
        ingresos_gastos_mensuales.setLayout(new BorderLayout());
        ingresos_gastos_mensuales.add(grafico_barras_mensual, BorderLayout.CENTER);

        btn_cuenta_bancaria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        // Añade ActionListeners
        plazos_fijos_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlazoFijos newframe = new PlazoFijos();
                newframe.setVisible(true);
                dispose();
            }
        });
        btn_ingreso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
                newframe.setVisible(true);
                dispose();
            }
        });
        btn_stocks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_tarjeta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tarjetas newframe = new Tarjetas();
                newframe.setVisible(true);
                dispose();
            }
        });
        prestamos_button.addActionListener(new ActionListener() {
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
