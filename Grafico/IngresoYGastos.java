package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IngresoYGastos extends JFrame {
    private JButton menuPrincipalButton;
    private JButton cuentasBancariasButton;
    private JButton plazosFijosButton;
    private JButton prestamosButton;
    private JButton tarjetaDeCreditoButton;
    private JButton stocksButton;
    private JButton ingresosYGastosButton;
    private JPanel IngresosYGastosPanel;
    private JTextField tfFuenteIngreso;
    private JComboBox cbCuentaBancoIng;
    private JComboBox cbFrecuenciaIng;
    private JTextField tfMontoIngr;
    private JTextField ingresoIDTextField;
    private JTextField gastoIDTextField;
    private JButton eliminarIngresoButton;
    private JButton eliminarGastoButton;
    private JButton btnAgregarGast;
    private JButton btnAgregarIngr;
    private JTextField tfFuenteGasto;
    private JComboBox cbFrecuenciaGast;
    private JTextField tfMontoGastos;
    private JPanel FechaIngresoPanel;
    private JScrollBar scrollBar1;
    private JPanel FechaGastosPanel;
    private JComboBox cbCuentaBAncoGast;

    public IngresoYGastos() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Ingresos Y Gastos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(IngresosYGastosPanel);

        menuPrincipalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard newframe = new Dashboard();
                newframe.setVisible(true);
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

        plazosFijosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlazoFijos newframe = new PlazoFijos();
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

        tarjetaDeCreditoButton.addActionListener(new ActionListener() {
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
                IngresoYGastos frame = new IngresoYGastos();
                frame.setVisible(true);
            }
        });
    }
}
