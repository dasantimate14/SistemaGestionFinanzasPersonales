package Grafico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

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
    private JTextField tfFuenteIngreso;
    private JTextField tfFuenteGasto;
    private JComboBox<String> cbtipoIngreso;
    private JComboBox<String> comboBox3;
    private JButton btnBuscar;
    private JButton promedioMensualDeIngresosButton;
    private JTextField tfPromMensualGastos;
    private JButton agregarIngresoButton;
    private JButton agregarGastosButton;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JList<String> list1;
    private JTextField tfPromMensualIngreso;
    private JTextField tfPromAnualIngreso;
    private JTextField ingresoIDTextField; // Campo para "Inserta el ID del Ingreso"
    private JTextField gastoIDTextField; // Campo para "Inserta el ID del Gasto"
    private JButton eliminarIngresoButton; // Botón para "Eliminar Ingreso"
    private JButton eliminarGastoButton; // Botón para "Eliminar Gasto"
    private JPanel FechaIngresoPanel;
    private JPanel FechaGastosPanel;
    private JTextField tfPromAnualGastos;

    public IngresoYGastos() {

        actualizarComboBoxes();

        //Implementación del JDatePicker dentro del GUI
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        JDatePanelImpl datePanelIngreso = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanelIngreso, new DateLabelFormatter());

        FechaIngresoPanel.setLayout(new BorderLayout());
        FechaIngresoPanel.add(datePicker, BorderLayout.CENTER);

        // Implementación del JDatePicker para Fecha de Gasto
        UtilDateModel modelGasto = new UtilDateModel();
        JDatePanelImpl datePanelGasto = new JDatePanelImpl(modelGasto, p);
        JDatePickerImpl datePickerGasto = new JDatePickerImpl(datePanelGasto, new DateLabelFormatter());

        FechaGastosPanel.setLayout(new BorderLayout());
        FechaGastosPanel.add(datePickerGasto, BorderLayout.CENTER);

        actualizarComboBoxes();

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

        eliminarIngresoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCampoIngresoID();
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
                    validarCampoGastoID();
                    // Código para eliminar gasto aquí
                    JOptionPane.showMessageDialog(null, "Gasto eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCamposBusqueda();
                    JOptionPane.showMessageDialog(null, "Búsqueda realizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void actualizarComboBoxes() {
        cbtipoIngreso.removeAllItems();
        comboBox1.removeAllItems();
        comboBox2.removeAllItems();
        comboBox3.removeAllItems();

        for (AgregarCuentaBanco.Cuenta cuenta : AgregarCuentaBanco.getCuentas()) {
            cbtipoIngreso.addItem(cuenta.getNumero());
            comboBox1.addItem(cuenta.getNumero());
            comboBox2.addItem(cuenta.getNombre());
            comboBox3.addItem(cuenta.getNombre());
        }
    }

    private void validarCamposIngreso() throws Exception {
        String fuenteIngreso = tfFuenteIngreso.getText();
        String cuentaBancaria = (String) cbtipoIngreso.getSelectedItem();
        String frecuencia = (String) comboBox3.getSelectedItem();
        String montoIngreso = textField3.getText();

        if (fuenteIngreso.isEmpty()) {
            throw new Exception("Debe seleccionar la fuente del ingreso.");
        }

        if (cuentaBancaria == null || cuentaBancaria.isEmpty() || cuentaBancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }

        if (frecuencia == null || frecuencia.isEmpty() || frecuencia.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una frecuencia.");
        }
    }

    private void validarCamposGastos() throws Exception {
        String fuenteGasto = tfFuenteGasto.getText();
        String cuentaBancaria = (String) comboBox1.getSelectedItem();
        String frecuencia = (String) comboBox2.getSelectedItem();
        String montoGasto = textField4.getText();

        if (fuenteGasto.isEmpty()) {
            throw new Exception("Debe seleccionar la fuente del gasto.");
        }

        if (cuentaBancaria == null || cuentaBancaria.isEmpty() || cuentaBancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }

        if (frecuencia == null || frecuencia.isEmpty() || frecuencia.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una frecuencia.");
        }
    }

    private void validarCampoIngresoID() throws Exception {
        String ingresoID = ingresoIDTextField.getText();

        if (ingresoID.isEmpty()) {
            throw new Exception("Debe ingresar el ID del ingreso.");
        }
    }

    private void validarCampoGastoID() throws Exception {
        String gastoID = gastoIDTextField.getText();

        if (gastoID.isEmpty()) {
            throw new Exception("Debe ingresar el ID del gasto.");
        }
    }

    private void validarCamposBusqueda() throws Exception {
        String tipoTransaccion = (String) cbtipoIngreso.getSelectedItem();
        String mes = (String) comboBox3.getSelectedItem();
        String fuenteTransaccion = (String) comboBox1.getSelectedItem();

        if (tipoTransaccion == null || tipoTransaccion.isEmpty() || tipoTransaccion.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar el tipo de transacción.");
        }

        if (mes == null || mes.isEmpty() || mes.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar un mes.");
        }

        if (fuenteTransaccion == null || fuenteTransaccion.isEmpty() || fuenteTransaccion.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una fuente de la transacción.");
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


    //Formatter para que la libreria se extienda
   public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "dd/MM/yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

}