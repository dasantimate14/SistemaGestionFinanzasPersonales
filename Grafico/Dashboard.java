package Grafico;

import javax.swing.*;

public class Dashboard extends JFrame {
    private JPanel DashboardPanel;
    private JButton btnMenu1;
    private JButton btnCuentaBancaria;
    private JButton plazosFijosButton;
    private JButton btnIngresos;
    private JButton btnStocks;
    private JButton btnTarjetas;
    private JButton prestamosButton;
    private JButton btnGastos;
    private JButton btnProyecciones;

    public Dashboard() {
        // Configuración de la ventana de inicio de sesión
        setSize(930, 920);
        setTitle("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(DashboardPanel); // Asignamos el panel principal
    }

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

