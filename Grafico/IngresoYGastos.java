package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IngresoYGastos {
    private JButton menuPrincipalButton;
    private JButton cuentaBancariaButton;
    private JButton plazoFijosButton;
    private JButton ingresosYGastosButton;
    private JButton stocksButton;
    private JButton tarjetasDeCreditoButton;
    private JButton prestamosButton;
    private JButton proyeccionesButton;
    private JPanel IngresoPanel;
    private JButton promedioMensualDeGastosButton;
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox<String> cbtipoIngreso;
    private JComboBox<String> cbCuentaBanco;
    private JButton btnBuscar;
    private JButton promedioMensualDeIngresosButton;
    private JTextField tfMontoGastos;
    private JButton agregarIngresoButton;
    private JButton agregarGastosButton;
    private JComboBox<String> comboBox1;
    private JComboBox<String> cbFrecuenciaIng;
    private JList<String> list1;
    private JTextField tfMontoIngr;
    private JComboBox cbFrecuenciaGast;


    public IngresoYGastos() {

    }
        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new JFrame("Ingreso y Gastos");
                    frame.setContentPane(new IngresoYGastos().IngresoPanel);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                }
            });
        }

        public void setVisible(boolean b) {
        }
    }