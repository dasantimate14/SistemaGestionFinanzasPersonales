package Grafico;

import javax.swing.*;
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
    private JButton btnProyecciones;

    public Dashboard() {
        // Configuración de la ventana de inicio de sesión
        setSize(930, 920);
        setTitle("Menu Principal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(DashboardPanel);

        btnCuentaBancaria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancaria newframe = new CuentaBancaria();
                newframe.setVisible(true);
                dispose();
            }
        });

        // Añade ActionListeners para otros botones si es necesario
    } // Cierre del constructor

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

