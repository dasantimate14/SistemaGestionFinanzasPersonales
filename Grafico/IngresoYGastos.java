package Grafico;

import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import sistemagestionfinanzas.CuentaBancaria;
import sistemagestionfinanzas.Gasto;
import sistemagestionfinanzas.Ingreso;

public class IngresoYGastos extends JFrame {
    private JPanel ingresos_y_gastos_panel;
    private JButton btn_menu_principal;
    private JButton btn_cuentas_bancarias;
    private JButton btn_plazo_fijo;
    private JButton btn_prestamos;
    private JButton btn_tarjeta_credito;
    private JButton btn_stocks;
    private JButton btn_ingresos_gastos;
    private JComboBox cb_cuenta_banco_ingreso;
    private JComboBox cb_frecuencia_gasto;
    private JScrollPane sp_ingreso;
    private JTable table_ingreso;
    private JButton btn_agregar_ingr;
    private JButton btn_agregar_gast;
    private JButton btn_eliminar_ing;
    private JTextField tf_nombre_ingresos;
    private JTextField tf_descripcion_ingr;
    private JTextField tf_fuente_ingreso;
    private JTextField tf_monto_ingreso;
    private JComboBox<Integer> cb_frecuencia_ing;
    private JComboBox<String> cb_cuenta_gasto;
    private JDatePickerImpl date_picker_ingreso;
    private JPanel fecha_ingreso_panel;
    private JTextField tf_nombre_gasto;
    private JTextField tf_descripcion_gasto;
    private JTextField tf_monto_gasto;
    private JTextField tf_acreedor_gast;
    private JComboBox<String> cb_Categoria;
    private JDatePickerImpl date_picker_gastos;
    private JPanel fecha_gastos_panel;
    private JTable table_gasto;
    private JPanel panel_tabla_ingr;
    private JPanel gasto_tabla_panel;
    private JScrollPane sp_gasto;
    private DefaultTableModel ingreso_modelo;
    private DefaultTableModel gasto_modelo;

    public IngresoYGastos() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Ingresos Y Gastos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(ingresos_y_gastos_panel);

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
        JDatePanelImpl date_panel_gastos = new JDatePanelImpl(modelGastos, pGastos);
        date_picker_gastos = new JDatePickerImpl(date_panel_gastos, new DateLabelFormatter());

        fecha_gastos_panel.setLayout(new BorderLayout());
        fecha_gastos_panel.add(date_picker_gastos, BorderLayout.CENTER);

        // Actualizar el JComboBox de cuentas bancarias
        actualizarComboBoxCuentas();

        //Configuración de la tabla ingreso
        ingreso_modelo = new DefaultTableModel();
        ingreso_modelo.setColumnIdentifiers(new String[] {"ID","Nombre", "Descripción", "Fuente", "Cuenta de banco", "Frecuencia", "Fecha", "monto"});
        table_ingreso.setModel(ingreso_modelo);
        table_ingreso.getTableHeader().setReorderingAllowed(false);
        table_ingreso.getColumnModel().getColumn(0).setPreferredWidth(50);
        table_ingreso.getColumnModel().getColumn(1).setPreferredWidth(100);
        table_ingreso.getColumnModel().getColumn(2).setPreferredWidth(100);
        table_ingreso.getColumnModel().getColumn(3).setPreferredWidth(100);
        table_ingreso.getColumnModel().getColumn(4).setPreferredWidth(100);
        table_ingreso.getColumnModel().getColumn(5).setPreferredWidth(70);
        table_ingreso.getColumnModel().getColumn(6).setPreferredWidth(80);
        table_ingreso.getColumnModel().getColumn(7).setPreferredWidth(100);
        sp_ingreso.setViewportView(table_ingreso);

        //Configuración de la tabla Gasto
        gasto_modelo = new DefaultTableModel();
        gasto_modelo.setColumnIdentifiers(new String[] {"ID","Nombre", "Acreedor", "Descripción", "Cuenta de banco", "Frecuencia", "Fecha", "monto","Categoría","Estatus"});
        table_gasto.setModel(gasto_modelo);
        table_ingreso.getTableHeader().setReorderingAllowed(false);
        table_gasto.getColumnModel().getColumn(0).setPreferredWidth(50);
        table_gasto.getColumnModel().getColumn(1).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(2).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(3).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(4).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(5).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(6).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(7).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(8).setPreferredWidth(100);
        table_gasto.getColumnModel().getColumn(9).setPreferredWidth(100);
        sp_gasto.setViewportView(table_gasto);


        btn_agregar_ingr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String nombre = tf_nombre_ingresos.getText();
                    String descripcion = tf_descripcion_ingr.getText();
                    // Obtener la fecha del JDatePicker y convertirla a LocalDate
                    Date date_ingreso = (Date) date_picker_ingreso.getModel().getValue();
                    LocalDate fechaInicio = date_ingreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    String fuente = tf_fuente_ingreso.getText();
                    float montoOriginal = Float.parseFloat(tf_monto_ingreso.getText());
                    //CuentaBancaria cuenta_bancaria = (CuentaBancaria) cb_cuenta_gasto.getSelectedItem();
                    String cuentaSeleccionada = (String) cb_cuenta_gasto.getSelectedItem();
                    int frecuencia = Integer.parseInt(cb_frecuencia_ing.getSelectedItem().toString());


                    // Encontrar la cuenta bancaria seleccionada
                    CuentaBancaria cuentaVinculada = null;
                    for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                        String cuentaBuscada = cuenta.getNumeroCuenta().toString()+" "+cuenta.getNombre().toString();
                        if (cuentaBuscada.equals(cuentaSeleccionada)) {
                            cuentaVinculada = cuenta;
                            break;
                        }
                    }

                    if (cuentaVinculada == null) {
                        JOptionPane.showMessageDialog(IngresoYGastos.this, "Cuenta bancaria seleccionada no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Ingreso ingreso = new Ingreso(nombre, descripcion, montoOriginal, fechaInicio, fuente, cuentaVinculada, frecuencia);
                    ingreso.guardarIngresoBaseDatos();
                    ingreso.actualizarInformacion();

                    Object[] fila_ingreso = {ingreso.getId(), ingreso.getNombre(), ingreso.getDescripcion(), ingreso.getFuente(), ingreso.getCuentaBancaria().getNombre() +" "+ ingreso.getCuentaBancaria().getNumeroCuenta(), ingreso.getFrecuencia(), ingreso.getFechaInicio(), ingreso.getMontoOriginal(), };

                    ingreso_modelo.addRow(fila_ingreso);


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
                try {
                    String nombre = tf_nombre_gasto.getText();
                    String descripcion = tf_descripcion_gasto.getText();
                    float montoOriginal = Float.parseFloat(tf_monto_gasto.getText());
                    Date date_gasto = (Date) date_picker_gastos.getModel().getValue();
                    LocalDate fechaInicio = date_gasto.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    String acreedor = tf_acreedor_gast.getText();
                    String categoriaGasto = (String) cb_Categoria.getSelectedItem().toString();
                    String cuentaSeleccionada = (String) cb_cuenta_gasto.getSelectedItem();
                    int frecuencia = Integer.parseInt(cb_frecuencia_gasto.getSelectedItem().toString());

                    // Encontrar la cuenta bancaria seleccionada
                    CuentaBancaria cuentaVinculada = null;
                    for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                        String cuentaBuscada = cuenta.getNumeroCuenta()+" "+cuenta.getNombre();
                        if (cuentaBuscada.equals(cuentaSeleccionada)) {
                            cuentaVinculada = cuenta;
                            break;
                        }
                    }

                    if (cuentaVinculada == null) {
                        JOptionPane.showMessageDialog(IngresoYGastos.this, "Cuenta bancaria seleccionada no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Gasto gasto = new Gasto(nombre, descripcion, montoOriginal, fechaInicio, acreedor, frecuencia, categoriaGasto, cuentaVinculada);
                    gasto.guardarGastoBaseDatos();
                    gasto.actualizarInformacion();

                    Object[] fila_gasto = {gasto.getId(), gasto.getNombre(), gasto.getAcreedor(), gasto.getDescripcion(), gasto.getCuenta().getNumeroCuenta()+ " " + gasto.getCuenta().getNombre(), gasto.getFrecuencia(), gasto.getFechaInicio(), gasto.getMontoOriginal(), gasto.getCategoriaGasto(), gasto.getEstatus()};

                    gasto_modelo.addRow(fila_gasto);

                    JOptionPane.showMessageDialog(null, "Gasto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error, no se pudo ingresar el gasto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
        buscarGastosIngresos();
    }
    public void buscarGastosIngresos(){
        for (Ingreso ingreso: Ingreso.instancias_ingresos){
            Object[] fila_ingreso = {ingreso.getId(), ingreso.getNombre(), ingreso.getDescripcion(), ingreso.getFuente(), ingreso.getCuentaBancaria(), ingreso.getFrecuencia(), ingreso.getFechaInicio(), ingreso.getMontoOriginal()};

            ingreso_modelo.addRow(fila_ingreso);
        }
        for (Gasto gasto: Gasto.instancias_gastos){
            Object[] fila_gasto = {gasto.getId(), gasto.getNombre(), gasto.getAcreedor(), gasto.getDescripcion(), gasto.getCuenta(), gasto.getFrecuencia(), gasto.getFechaInicio(), gasto.getMontoOriginal(), gasto.getCategoriaGasto(), gasto.getEstatus()};

            gasto_modelo.addRow(fila_gasto);
        }
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
    private void actualizarComboBoxCuentas() {
        cb_cuenta_gasto.removeAllItems();
        cb_cuenta_banco_ingreso.removeAllItems();
        for(CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias){
            cb_cuenta_gasto.addItem(cuenta.getNumeroCuenta().toString()+" "+cuenta.getNombre().toString());
            cb_cuenta_banco_ingreso.addItem(cuenta.getNumeroCuenta().toString()+" "+cuenta.getNombre().toString());
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