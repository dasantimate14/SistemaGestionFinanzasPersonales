package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CuentaBancaria extends JFrame {
    private JPanel CuentaBancoPanel;
    private JButton btnMenu2;
    private JButton proyeccionesButton;
    private JButton prestamosButton;
    private JButton tarjetasDeCreditoButton;
    private JButton stocksButton;
    private JButton ingresosYGastosButton;
    private JButton plazosFijosButton;
    private JButton cuentasBancariasButton;
    private JButton btnAgregarCuenta;
    private JButton consultarMovimientosButton;

    public CuentaBancaria() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Cuentas de Banco");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(CuentaBancoPanel);

        btnAgregarCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsultarMovimientos newframe = new ConsultarMovimientos();
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
                CuentaBancaria frame = new CuentaBancaria();
                frame.setVisible(true);
            }
        });
    }
}