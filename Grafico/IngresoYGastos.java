package Grafico;

import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Properties;

import sistemagestionfinanzas.CuentaBancaria;
import sistemagestionfinanzas.Ingreso;

public class IngresoYGastos extends JFrame {
    private JButton btn_menu_principal;
    private JButton btn_cuentas_bancarias;
    private JButton btn_plazo_fijo;
    private JButton btn_prestamos;
    private JButton btn_tarjeta_credito;
    private JButton btn_stocks;
    private JButton btn_ingresos_gastos;
    private JPanel IngresosYGastosPanel;
    private JTextField tf_fuente_ingreso;
    private JComboBox<String> cb_cuenta_banco_ingreso;
    private JComboBox<String> cb_frecuencia_ing;
    private JTextField tf_monto_ingreso;
    private JTextField tf_ingreso_id;
    private JTextField tf_gasto_id;
    private JButton btn_eliminar_ing;
    private JButton btn_eliminar_gasto;
    private JButton btn_agregar_gast;
    private JButton btn_agregar_ingr;
    private JTextField tf_descripcion_gasto;
    private JComboBox<String> cb_frecuencia_gasto;
    private JTextField tf_monto_gasto;
    private JPanel fecha_ingreso_panel;
    private JPanel fecha_gastos_panel;
    private JComboBox<String> cb_cuenta_banco;
    private JTextField tf_nombre_gasto;
    private JTextField tf_nombre_ingresos;
    private JTable table_ingresos_gastos;
    private JScrollPane sp_ingreso_gastos;
    private JTextField tf_acreedor_gast;
    private JTextField tf_descripcion_ingr;
    private JDatePickerImpl date_picker_ingreso;
    private JDatePickerImpl date_picker_gastos;

    public IngresoYGastos() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Ingresos Y Gastos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(IngresosYGastosPanel);


        // Implementación del JDatePicker para fecha de ingreso
        UtilDateModel modelIngreso = new UtilDateModel();
        Properties pIngreso = new Properties();
        pIngreso.put("text.today", "Hoy");
        pIngreso.put("text.month", "Mes");
        pIngreso.put("text.year", "Año");
        JDatePanelImpl date_panel_ingres = new JDatePanelImpl(modelIngreso, pIngreso);
        date_picker_ingreso = new JDatePickerImpl(date_panel_ingres, new DateLabelFormatter());

        fecha_ingreso_panel.setLayout(new BorderLayout());
        fecha_ingreso_panel.add(date_picker_ingreso, BorderLayout.CENTER);

        // Implementación del JDatePicker para fecha de gastos
        UtilDateModel modelGastos = new UtilDateModel();
        Properties pGastos = new Properties();
        pGastos.put("text.today", "Hoy");
        pGastos.put("text.month", "Mes");
        pGastos.put("text.year", "Año");
        JDatePanelImpl date_panel_gastos  = new JDatePanelImpl(modelGastos, pGastos);
        date_picker_gastos = new JDatePickerImpl(date_panel_gastos , new DateLabelFormatter());

        fecha_gastos_panel.setLayout(new BorderLayout());
        fecha_gastos_panel.add(date_picker_gastos, BorderLayout.CENTER);


        btn_agregar_ingr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = tf_nombre_ingresos.getText();
                    String descripcion = tf_descripcion_ingr.getText();
                    // Obtener la fecha del JDatePicker y convertirla a LocalDate
                    java.util.Date date_ingreso = (java.util.Date) date_picker_ingreso.getModel().getValue();
                    LocalDate fechaInicio = date_ingreso.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    int frecuencia = (int) cb_frecuencia_ing.getSelectedItem();
                    String fuente = tf_fuente_ingreso.getText();
                    float montoOriginal = Float.parseFloat(tf_monto_ingreso.getText());
                    CuentaBancaria cuenta_bancaria = (CuentaBancaria) cb_cuenta_banco.getSelectedItem();

                    Ingreso ingreso = new Ingreso(nombre, descripcion, montoOriginal, fechaInicio, fuente, cuenta_bancaria, frecuencia);

                    ingreso.actualizarInformacion();
                    //ingreso.guardarIngresoBaseDatos(); recuerda borrar

                    JOptionPane.showMessageDialog(null, "Ingreso agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    // Mostrar mensaje de error en caso de excepción
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btn_agregar_gast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String nombre= tf_nombre_gasto.getText();
                    String descripcion= tf_descripcion_gasto.getText();
                    float montoOriginal= Float.parseFloat(tf_monto_gasto.getText());
                    java.util.Date date_gasto = (java.util.Date) date_picker_gastos.getModel().getValue();
                    LocalDate fechaInicio = date_gasto.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    String acreedor = tf_acreedor_gast
                    int frecuencia= (int) cb_frecuencia_ing.getSelectedItem();
                    String categoriaGasto=
                    CuentaBancaria cuenta
            }
        });
    }



        btn_eliminar_ing.addActionListener(new ActionListener() {
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

        btn_eliminar_gasto.addActionListener(new ActionListener() {
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
        btn_menu_principal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard newframe = new Dashboard();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_cuentas_bancarias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_plazo_fijo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlazoFijos newframe = new PlazoFijos();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_stocks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_tarjeta_credito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tarjetas newframe = new Tarjetas();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_prestamos.addActionListener(new ActionListener() {
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

    // Formatter para que la librería se extienda
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
