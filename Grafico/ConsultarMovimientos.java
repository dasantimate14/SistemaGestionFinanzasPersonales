package Grafico;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import sistemagestionfinanzas.BaseDeDatos;
import sistemagestionfinanzas.CuentaBancaria;
import sistemagestionfinanzas.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.sql.ResultSet;

public class ConsultarMovimientos extends JFrame {
    private JPanel datePanelContainer;
    private JPanel MovPanel;
    private JComboBox cbNumeroCuenta;
    private JComboBox<String> cbNombreCuenta;
    private JTextField tfNumeroCuenta; //se está usando el que no es probablemente
    private JButton btnBuscar;
    private JButton Volverbtn;
    private JTable movimientosTable;
    private DefaultTableModel modelo;
    private JDatePickerImpl datePicker;

    public ConsultarMovimientos() {
        // Configuración de la ventana
        setSize(960, 920);
        setTitle("Consultar Movimiento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(MovPanel);

        // Implementación del JDatePicker dentro del GUI
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanelImpl, new DateLabelFormatter());

        datePanelContainer.setLayout(new BorderLayout());
        datePanelContainer.add(datePicker, BorderLayout.CENTER);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID", "Nombre", "Descripción", "Monto", "Tipo", "Fecha"});
        movimientosTable.setModel(modelo);

        actualizarComboBoxes();

        Volverbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarDatos();
                    // Aquí puedes agregar el código para buscar movimientos
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ConsultarMovimientos.this, "Por favor, ingrese datos válidos. " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        for(CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias){
            cbNumeroCuenta.addItem(cuenta.getNumeroCuenta());
            cbNombreCuenta.addItem(cuenta.getNombre());
        }
    }

    private void validarDatos() throws Exception {

        String numeroCuenta = tfNumeroCuenta.getText();
        if (numeroCuenta == null || numeroCuenta.isEmpty()) {
            throw new Exception("Debe introducir un número de cuenta.");
        }
        if (!numeroCuenta.matches("[0-9-]+")) {
            throw new Exception("El número de cuenta solo puede contener números y guiones.");
        }


        String nombreCuenta = (String) cbNombreCuenta.getSelectedItem();
        if (nombreCuenta == null || nombreCuenta.isEmpty()) {
            throw new Exception("Debe seleccionar un nombre de cuenta.");
        }


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

        public void buscarMovimientos() throws SQLException {
            try {
                ResultSet movimientos = BaseDeDatos.realizarConsultaSelect("SELECT id, nombre, descripcion, montoOriginal, tipo, fechaInicio, frecuencia, idCuentaBancaria " +
                                "FROM ingresos " +
                                "WHERE idUsuario = ? " +
                                "UNION " +
                                "SELECT id, nombre, descripcion, montoOriginal, tipo, fechaInicio, frecuencia, idCuentaBancaria " +
                                "FROM gastos " +
                                "WHERE idUsuario = ?",
                        new String[]{Usuario.usuario_actual.getId(), Usuario.usuario_actual.getId()});

                while(movimientos.next()){
                    Object[] movimiento = {movimientos.getString("id"), movimientos.getString("nombre"), movimientos.getString("descripcion"), movimientos.getFloat("montoOriginal"), movimientos.getString("tipo"), movimientos.getString("fechaInicio"), movimientos.getString("frecuencia"), movimientos.getString("idCuentaBancaria")};
                    modelo.addRow(movimiento);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }


        }
    }
}
