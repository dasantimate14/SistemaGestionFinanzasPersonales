package Grafico;


import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class ConsultarMovimientos extends JFrame {
    private JPanel datePanelContainer;
    private JPanel MovPanel;
    private JComboBox cbNumeroCuenta;
    private JComboBox<String> cbNombreCuenta;
    private JTextField tfNumeroCuenta;
    private JList<String> list1;
    private JButton btnBuscar;
    private JButton Volverbtn;
    private JDatePickerImpl datePicker;

    public ConsultarMovimientos() {
        // Configuración de la ventana
        setSize(960, 920);
        setTitle("Consultar Movimiento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(MovPanel);

        //Implementación del JDatePicker dentro del GUI
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanelImpl, new DateLabelFormatter());

        datePanelContainer.setLayout(new BorderLayout());
        datePanelContainer.add(datePicker, BorderLayout.CENTER);

        actualizarComboBoxes();

        Volverbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancaria newframe = new CuentaBancaria();
                newframe.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConsultarMovimientos frame = new ConsultarMovimientos();
                frame.setVisible(true);
            }
        });
    }
    private void actualizarComboBoxes() {
        cbNumeroCuenta.removeAllItems();
        cbNombreCuenta.removeAllItems();
        for (AgregarCuentaBanco.Cuenta cuenta : AgregarCuentaBanco.getCuentas()) {
            cbNumeroCuenta.addItem(cuenta.getNumero());
            cbNombreCuenta.addItem(cuenta.getNombre());
        }
    }

    private void validarDatos() throws Exception {
        // Validar número de cuenta
        String numeroCuenta = tfNumeroCuenta.getText();
        if (numeroCuenta == null || numeroCuenta.isEmpty()) {
            throw new Exception("Debe introducir un número de cuenta.");
        }
        if (!numeroCuenta.matches("[0-9-]+")) {
            throw new Exception("El número de cuenta solo puede contener números y guiones.");
        }

        // Validar nombre de cuenta
        String nombreCuenta = (String) cbNombreCuenta.getSelectedItem();
        if (nombreCuenta == null || nombreCuenta.isEmpty()) {
            throw new Exception("Debe seleccionar un nombre de cuenta.");
        }

        // Validar fecha
        Calendar selectedDate = (Calendar) datePicker.getModel().getValue();
        if (selectedDate == null) {
            throw new Exception("Debe seleccionar una fecha.");
        }
    }


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
