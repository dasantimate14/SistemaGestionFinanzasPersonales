package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Prestamos extends JFrame{
    private JButton páginaPrincipalButton;
    private JButton cuentasBancariasButton;
    private JButton plazosFijosButton;
    private JButton ingresosYGastosButton;
    private JButton stocksButton;
    private JButton tarjetasDeCréditoButton;
    private JButton préstamosButton;
    private JButton agregarPréstamoButton;
    private JButton mostrarInterésPendienteButton;
    private JButton mostrarInterésAcumuladoButton;
    private JTextField textField1;
    private JTextField textField2;
    private JList list1;
    private JComboBox comboBox1;
    private JTextField textField3;
    private JTextField textField4;
    private JComboBox comboBox2;
    private JTextField textField5;
    private JPanel FechaDeVencimientoPanel;
    private JPanel PrestamosPanel;


    public Prestamos() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Prestamos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(PrestamosPanel);

        páginaPrincipalButton.addActionListener(new ActionListener() {
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
                CuentaBancariaG newframe = new CuentaBancariaG();
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
        ingresosYGastosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
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
        tarjetasDeCréditoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tarjetas newframe = new Tarjetas();
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
                Prestamos frame = new Prestamos();
                frame.setVisible(true);
            }
        });
    }
}
