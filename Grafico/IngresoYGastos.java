package Grafico;

import sistemagestionfinanzas.CuentaBancaria;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
    private JComboBox<String> cbCuentaBancoIng;
    private JComboBox<String> cbFrecuenciaIng;
    private JTextField tfMontoIngr;
    private JTextField ingresoIDTextField;
    private JTextField gastoIDTextField;
    private JButton eliminarIngresoButton;
    private JButton eliminarGastoButton;
    private JButton btnAgregarGast;
    private JButton btnAgregarIngr;
    private JTextField tfFuenteGasto;
    private JComboBox<String> cbFrecuenciaGast;
    private JTextField tfMontoGastos;
    private JPanel FechaIngresoPanel;
    private JScrollBar scrollBar1;
    private JPanel FechaGastosPanel;
    private JComboBox<String> cbCuentaBAncoGast;

    public IngresoYGastos() {
        actualizarComboBoxes();
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Ingresos Y Gastos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(IngresosYGastosPanel);

        btnAgregarIngr.addActionListener(new ActionListener() {
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

        btnAgregarGast.addActionListener(new ActionListener() {
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

        eliminarIngresoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCampoIDIngresoGasto();
                    // Código para eliminar ingreso aquí
                    JOptionPane.showMessageDialog(null, "Ingreso eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminarGastoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCampoIDIngresoGasto();
                    // Código para eliminar gasto aquí
                    JOptionPane.showMessageDialog(null, "Gasto eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listeners para navegar a través del dashboard
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
        actualizarComboBoxes();
    }

    private void actualizarComboBoxes() {
        cbCuentaBancoIng.removeAllItems();
        cbCuentaBAncoGast.removeAllItems();

        List<AgregarCuentaBanco.Cuenta> cuentas = AgregarCuentaBanco.getCuentas();
        for (AgregarCuentaBanco.Cuenta cuenta : cuentas) {
            cbCuentaBancoIng.addItem(cuenta.toString());
            cbCuentaBAncoGast.addItem(cuenta.toString());
        }

        cbFrecuenciaIng.addItem("Selecciona una opción");
        cbFrecuenciaIng.addItem("Diario");
        cbFrecuenciaIng.addItem("Semanal");
        cbFrecuenciaIng.addItem("Mensual");
        cbFrecuenciaIng.addItem("Anual");

        cbFrecuenciaGast.addItem("Selecciona una opción");
        cbFrecuenciaGast.addItem("Diario");
        cbFrecuenciaGast.addItem("Semanal");
        cbFrecuenciaGast.addItem("Mensual");
        cbFrecuenciaGast.addItem("Anual");
    }

    private void validarCamposIngreso() throws Exception {
        String fuenteIngreso = tfFuenteIngreso.getText();
        String montoIngreso = tfMontoIngr.getText();
        String cuentaBancaria = (String) cbCuentaBancoIng.getSelectedItem();
        String frecuencia = (String) cbFrecuenciaIng.getSelectedItem();

        if (fuenteIngreso.isEmpty()) {
            throw new Exception("Debe ingresar la fuente del ingreso.");
        }

        if (montoIngreso.isEmpty()) {
            throw new Exception("Debe ingresar el monto del ingreso.");
        }

        if (cuentaBancaria == null || cuentaBancaria.isEmpty() || cuentaBancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }

        if (frecuencia == null || frecuencia.isEmpty() || frecuencia.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una frecuencia.");
        }

        try {
            Double.parseDouble(montoIngreso);
        } catch (NumberFormatException e) {
            throw new Exception("El monto del ingreso debe ser un número válido.");
        }
    }

    private void validarCamposGastos() throws Exception {
        String fuenteGasto = tfFuenteGasto.getText();
        String montoGasto = tfMontoGastos.getText();
        String cuentaBancaria = (String) cbCuentaBAncoGast.getSelectedItem();
        String frecuencia = (String) cbFrecuenciaGast.getSelectedItem();

        if (fuenteGasto.isEmpty()) {
            throw new Exception("Debe ingresar la fuente del gasto.");
        }

        if (montoGasto.isEmpty()) {
            throw new Exception("Debe ingresar el monto del gasto.");
        }

        if (cuentaBancaria == null || cuentaBancaria.isEmpty() || cuentaBancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }

        if (frecuencia == null || frecuencia.isEmpty() || frecuencia.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una frecuencia.");
        }

        try {
            Double.parseDouble(montoGasto);
        } catch (NumberFormatException e) {
            throw new Exception("El monto del gasto debe ser un número válido.");
        }
    }

    private void validarCampoIDIngresoGasto() throws Exception {
        String ingresoID = ingresoIDTextField.getText();
        String gastoID = gastoIDTextField.getText();

        if (ingresoID.isEmpty()) {
            throw new Exception("Debe ingresar el ID del ingreso.");
        }

        if (gastoID.isEmpty()) {
            throw new Exception("Debe ingresar el ID del gasto.");
        }

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
