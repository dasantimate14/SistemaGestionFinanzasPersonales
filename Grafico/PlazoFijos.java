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
    private JTextField tfInteresMensual;
    private JTextField tfInteresAnual;
    private JTextField tfInteresActual;
    private JTextField tfInteresAcumulado;
    private JTextField tfIdPlazo;
    private JButton btnEliminarPlazo;
    private JButton btnAgregarPlazo;
    private JTextField tfNombre;
    private JTextField tfMontOriginal;
    private JTextField tfTasaInt;
    private JPanel fechaInicioPanel;
    private JTextField tfDescripcion;
    private JPanel fechaFinalPanel;
    private JTextField tfPlazo;
    private JComboBox cbCuentaBanco;
    private JTable plazofijoTable;
    private JScrollPane spPlazofijo;

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
                CuentaBancariaG newframe = new CuentaBancariaG();
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