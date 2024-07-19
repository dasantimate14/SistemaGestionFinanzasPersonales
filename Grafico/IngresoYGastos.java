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
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCamposBusqueda();
                    // Código para buscar y mostrar los resultados aquí
                    JOptionPane.showMessageDialog(null, "Búsqueda realizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        agregarIngresoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCamposIngreso();
                    // Código para agregar ingreso aquí
                    JOptionPane.showMessageDialog(null, "Ingreso agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        agregarGastosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCamposGastos();
                    // Código para agregar gastos aquí
                    JOptionPane.showMessageDialog(null, "Gasto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void validarCamposBusqueda() throws Exception {
        String tipoTransaccion = (String) cbtipoIngreso.getSelectedItem();
        String mes = (String) cbFrecuenciaIng.getSelectedItem();
        String fuenteTransaccion = (String) cbCuentaBanco.getSelectedItem();

        if (tipoTransaccion == null || tipoTransaccion.isEmpty() || tipoTransaccion.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar el tipo de transacción.");
        }

        if (mes == null || mes.isEmpty() || mes.equals("Mes")) {
            throw new Exception("Debe seleccionar un mes.");
        }

        if (fuenteTransaccion == null || fuenteTransaccion.isEmpty()) {
            throw new Exception("Debe Introducir una fuente de la transacción.");
        }
    }

    private void validarCamposIngreso() throws Exception {
        String fuenteIngreso = textField1.getText();
        String cuentaBancaria = (String) cbtipoIngreso.getSelectedItem();
        String frecuencia = (String) cbFrecuenciaIng.getSelectedItem();
        String montoIngreso = tfMontoIngr.getText();

        if (fuenteIngreso.isEmpty()) {
            throw new Exception("Debe seleccionar la fuente del ingreso.");
        }

        if (cuentaBancaria == null || cuentaBancaria.isEmpty() || cuentaBancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }

        if (frecuencia == null || frecuencia.isEmpty() || frecuencia.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una frecuencia.");
        }

        if (montoIngreso.isEmpty()) {
            throw new Exception("Debe ingresar un monto de ingreso.");
        }

        try {
            Double.parseDouble(montoIngreso);
        } catch (NumberFormatException e) {
            throw new Exception("El monto del ingreso debe ser un número válido.");
        }
    }

    private void validarCamposGastos() throws Exception {
        String fuenteGasto = textField2.getText();
        String cuentaBancaria = (String) comboBox1.getSelectedItem();
        String frecuencia = (String) cbFrecuenciaGast.getSelectedItem();
        String montoGasto = tfMontoGastos.getText();

        if (fuenteGasto.isEmpty()) {
            throw new Exception("Debe seleccionar la fuente del gasto.");
        }

        if (cuentaBancaria == null || cuentaBancaria.isEmpty() || cuentaBancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }

        if (frecuencia == null || frecuencia.isEmpty() || frecuencia.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una frecuencia.");
        }

        if (montoGasto.isEmpty()) {
            throw new Exception("Debe ingresar un monto de gasto.");
        }

        try {
            Double.parseDouble(montoGasto);
        } catch (NumberFormatException e) {
            throw new Exception("El monto del gasto debe ser un número válido.");
        }


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