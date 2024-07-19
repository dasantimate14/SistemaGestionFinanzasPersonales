package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlazoFijos extends JFrame{
    private JPanel PlazoFijosPanel;
    private JButton menuPrincipalButton;
    private JButton cuentasBancariasButton;
    private JButton plazoFijosButton;
    private JButton ingresosYGastosButton;
    private JButton stocksButton;
    private JButton tarjetasDeCreditosButton;
    private JButton prestamosButton;
    private JButton gastosButton;
    private JTextField tfNombre;
    private JTextField tfMontOriginal;
    private JTextField tfTasaInt;
    private JTextField tfPlazo;
    private JButton btnAgregarPlazo;
    private JComboBox cbCuentaBanco;
    private JTextField tfIdPlazo;
    private JPanel FechaInicioPanel;
    private JButton btnEliminarPlazo;
    private JScrollBar scrollBar1;
    private JTextField textField1;
    private JTextField textField2;

    public PlazoFijos() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Plazo Fijos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(PlazoFijosPanel);

        //Action listeners para dashboard
        menuPrincipalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción para el botón del menú principal
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                dispose();
            }
        });

        cuentasBancariasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancaria newframe = new CuentaBancaria();
                newframe.setVisible(true);
                dispose();
            }
        });

        stocksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });
        tarjetasDeCreditosButton.addActionListener(new ActionListener() {
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
        ingresosYGastosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
                newframe.setVisible(true);
                dispose();
            }
        });

    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PlazoFijos frame = new PlazoFijos();
                frame.setVisible(true);
            }
        });
    }
}