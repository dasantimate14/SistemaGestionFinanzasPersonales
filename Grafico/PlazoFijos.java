package Grafico;

import sistemagestionfinanzas.CuentaBancaria;
import sistemagestionfinanzas.PlazoFijo;

import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PlazoFijos extends JFrame {
    private JPanel polazo_fijos_panel;
    private JButton menu_principal_button;
    private JButton cuentas_bancarias_button;
    private JButton plazo_fijos_button;
    private JButton ingresos_y_gastos_button;
    private JButton stocks_button;
    private JButton tarjetas_de_creditos_button;
    private JButton prestamos_button;
    private JTextField tf_id_plazo;
    private JButton btn_eliminar_plazo;
    private JButton btn_agregar_plazo;
    private JTextField tf_nombre;
    private JTextField tf_monto_original;
    private JTextField tf_tasaint;
    private JPanel fecha_inicio_panel;
    private JTextField tf_descripcion;
    private JPanel fecha_final_panel;
    private JTextField tf_plazo;
    private JComboBox<String> cb_cuenta_banco;
    private JScrollPane sp_plazo_fijo;
    private JTextField tf_tipo;
    private JDatePickerImpl datePickerInicio;
    private JDatePickerImpl datePickerFinal;
    private JTable plazofijo_table;

    public PlazoFijos() {
        setSize(930, 920);
        setTitle("Plazo Fijos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(polazo_fijos_panel);

        // Configurar el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Monto Original");
        model.addColumn("Tasa de Interés");
        model.addColumn("Fecha de Inicio");
        model.addColumn("Plazo");
        model.addColumn("Fecha de Vencimiento");
        model.addColumn("Cuenta Bancaria");
        model.addColumn("Interés Acumulado");
        model.addColumn("Monto Final");
        model.addColumn("Valor Actual");
        model.addColumn("Promedio Mensual");
        model.addColumn("Promedio Anual");
        plazofijo_table.setModel(model);

        cargarCuentasBancarias();
        cargarDatosPlazoFijos();
        inicializarDatePickers();

        menu_principal_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                dispose();
            }
        });

        cuentas_bancarias_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        stocks_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });

        tarjetas_de_creditos_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tarjetas newframe = new Tarjetas();
                newframe.setVisible(true);
                dispose();
            }
        });

        prestamos_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prestamos newframe = new Prestamos();
                newframe.setVisible(true);
                dispose();
            }
        });

        ingresos_y_gastos_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_agregar_plazo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarPlazoFijo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar plazo fijo: " + ex.getMessage());
                }
            }
        });

        btn_eliminar_plazo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarPlazoFijo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar plazo fijo: " + ex.getMessage());
                }
            }
        });
    }

    private void inicializarDatePickers() {
        UtilDateModel modelInicio = new UtilDateModel();
        Properties pInicio = new Properties();
        pInicio.put("text.today", "Hoy");
        pInicio.put("text.month", "Mes");
        pInicio.put("text.year", "Año");
        JDatePanelImpl datePanelInicio = new JDatePanelImpl(modelInicio, pInicio);
        datePickerInicio = new JDatePickerImpl(datePanelInicio, new DateLabelFormatter());

        fecha_inicio_panel.setLayout(new BorderLayout());
        fecha_inicio_panel.add(datePickerInicio, BorderLayout.CENTER);

        UtilDateModel modelVencimiento = new UtilDateModel();
        Properties pVencimiento = new Properties();
        pVencimiento.put("text.today", "Hoy");
        pVencimiento.put("text.month", "Mes");
        pVencimiento.put("text.year", "Año");
        JDatePanelImpl datePanelVencimiento = new JDatePanelImpl(modelVencimiento, pVencimiento);
        datePickerFinal = new JDatePickerImpl(datePanelVencimiento, new DateLabelFormatter());

        fecha_final_panel.setLayout(new BorderLayout());
        fecha_final_panel.add(datePickerFinal, BorderLayout.CENTER);
    }

    private void validarDatos() throws Exception {
        String nombre = tf_nombre.getText();
        if (nombre == null || nombre.isEmpty()) {
            throw new Exception("Debe introducir un nombre.");
        }

        String descripcion = tf_descripcion.getText();
        if (descripcion == null || descripcion.isEmpty()) {
            throw new Exception("Debe introducir una descripción.");
        }

        String tipo = tf_tipo.getText();
        if (tipo == null || tipo.isEmpty()) {
            throw new Exception("Debe introducir un tipo.");
        }

        String monto_original_str = tf_monto_original.getText();
        if (monto_original_str == null || monto_original_str.isEmpty()) {
            throw new Exception("Debe introducir un monto original.");
        }
        if (!monto_original_str.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("El monto original solo puede contener números y un máximo de dos decimales.");
        }

        String tasa_interes_str = tf_tasaint.getText();
        if (tasa_interes_str == null || tasa_interes_str.isEmpty()) {
            throw new Exception("Debe introducir una tasa de interés.");
        }
        if (!tasa_interes_str.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("La tasa de interés solo puede contener números y un máximo de dos decimales.");
        }

        String plazo_str = tf_plazo.getText();
        if (plazo_str == null || plazo_str.isEmpty()) {
            throw new Exception("Debe introducir un plazo.");
        }
        if (!plazo_str.matches("\\d+")) {
            throw new Exception("El plazo solo debe contener números.");
        }

        Date fechaInicio = (Date) datePickerInicio.getModel().getValue();
        if (fechaInicio == null) {
            throw new Exception("Debe seleccionar una fecha de inicio.");
        }

        Date fechaFinal = (Date) datePickerFinal.getModel().getValue();
        if (fechaFinal == null) {
            throw new Exception("Debe seleccionar una fecha de vencimiento.");
        }

        // Nueva validación: fecha de inicio no puede ser más reciente que la fecha de vencimiento
        if (fechaInicio.after(fechaFinal)) {
            throw new Exception("La fecha de inicio no puede ser más reciente que la fecha de vencimiento.");
        }

        Object cuenta_bancaria = cb_cuenta_banco.getSelectedItem();
        if (cuenta_bancaria == null) {
            throw new Exception("Debe seleccionar una cuenta de banco.");
        }
    }

    private void cargarCuentasBancarias() {
        cb_cuenta_banco.removeAllItems();
        for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
            cb_cuenta_banco.addItem(cuenta.getNombre() + " " + cuenta.getNombre());
        }
    }

    private void cargarDatosPlazoFijos() {
        try {
            DefaultTableModel model = (DefaultTableModel) plazofijo_table.getModel();
            model.setRowCount(0); // Limpiar la tabla antes de cargar los nuevos datos
            for (PlazoFijo plazo : PlazoFijo.instancias_plazos_fijos) {
                model.addRow(new Object[]{
                        plazo.getId(),
                        plazo.getNombre(),
                        plazo.getMontoOriginal(),
                        plazo.getTasaInteres(),
                        plazo.getFechaInicio(),
                        plazo.getPlazo(),
                        plazo.getFechaFinal(),
                        plazo.getCuenta().getNumeroCuenta(),
                        plazo.calcularInteresAcumulado(),
                        plazo.calcularMontoFinal(),
                        plazo.calcularValorActual(),
                        plazo.getPromedioMensual(),
                        plazo.calcularPromedioAnual()
                });
            }
            adjustColumnWidths(plazofijo_table);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos de plazos fijos: " + e.getMessage());
        }
    }

    private void agregarPlazoFijo() throws SQLException {
        try {
            System.out.println("Iniciando la validación de datos...");
            validarDatos();
            System.out.println("Validación de datos exitosa.");

            String nombre = tf_nombre.getText();
            String descripcion = tf_descripcion.getText();
            String tipo = tf_tipo.getText();
            float monto_original = Float.parseFloat(tf_monto_original.getText());
            float tasa_interes = Float.parseFloat(tf_tasaint.getText());
            LocalDate fecha_inicio = LocalDate.ofInstant(((Date) datePickerInicio.getModel().getValue()).toInstant(), ZoneId.systemDefault());
            LocalDate fecha_final = LocalDate.ofInstant(((Date) datePickerFinal.getModel().getValue()).toInstant(), ZoneId.systemDefault());
            int plazo = Integer.parseInt(tf_plazo.getText());
            String cuenta_selecinada = (String) cb_cuenta_banco.getSelectedItem();

            System.out.println("Datos obtenidos: " + nombre + ", " + descripcion + ", " + tipo + ", " + monto_original + ", " + tasa_interes + ", " + fecha_inicio + ", " + fecha_final + ", " + plazo + ", " + cuenta_selecinada);

            CuentaBancaria cuenta_vinculada = null;
            for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                String cuenta_buscada = cuenta.getNombre() + " " + cuenta.getNombre();
                if (cuenta_buscada.equals(cuenta_selecinada)) {
                    cuenta_vinculada = cuenta;
                    break;
                }
            }
            if (cuenta_vinculada == null) {
                JOptionPane.showMessageDialog(PlazoFijos.this, "Cuenta bancaria seleccionada no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Cuenta vinculada encontrada: " + cuenta_vinculada.getNombre());

            PlazoFijo nuevoPlazo = new PlazoFijo(nombre, descripcion, monto_original, tasa_interes, fecha_inicio, plazo, cuenta_vinculada);
            // Descomentar la línea de guardar en base de datos si es necesario
            // nuevoPlazo.guardarPlazoFijoEnBaseDatos();
            PlazoFijo.instancias_plazos_fijos.add(nuevoPlazo);
            System.out.println("Nuevo plazo fijo agregado a las instancias.");

            cargarDatosPlazoFijos(); // Recargar los datos de la tabla después de agregar un nuevo plazo fijo
            System.out.println("Datos de plazos fijos recargados.");

            JOptionPane.showMessageDialog(this, "Plazo fijo agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en el formato de los datos: " + e.getMessage());
            System.out.println("Error en el formato de los datos: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el plazo fijo en la base de datos: " + e.getMessage());
            System.out.println("Error al guardar el plazo fijo en la base de datos: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la validación de datos: " + e.getMessage());
            System.out.println("Error en la validación de datos: " + e.getMessage());
        }
    }

    private void eliminarPlazoFijo() throws SQLException {
        int selectedRow = plazofijo_table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String idPlazo = plazofijo_table.getValueAt(selectedRow, 0).toString();
                PlazoFijo plazoFijo = PlazoFijo.encontrarPlazoFijoPorId(idPlazo);
                if (plazoFijo != null) {
                    plazoFijo.eliminarPlazoFijoEnBaseDatos();
                    PlazoFijo.instancias_plazos_fijos.remove(plazoFijo);
                    cargarDatosPlazoFijos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el plazo fijo: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un plazo fijo para eliminar.");
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarCuentasBancarias();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PlazoFijos frame = new PlazoFijos();
                frame.setVisible(true);
            }
        });
    }

    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
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

    private static void adjustColumnWidths(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);

            int maxWidth = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, i).getPreferredSize().width;

            // Calculate the maximum width based on cell contents
            for (int j = 0; j < table.getRowCount(); j++) {
                Object value = table.getValueAt(j, i);
                int cellWidth = table.getCellRenderer(j, i).getTableCellRendererComponent(table, value, false,
                        false, j, i).getPreferredSize().width;
                maxWidth = Math.max(maxWidth, cellWidth);
            }

            // Set the preferred width
            column.setPreferredWidth(maxWidth + 5); // Add some padding
        }

    }
}