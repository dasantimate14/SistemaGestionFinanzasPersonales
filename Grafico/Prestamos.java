package Grafico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.time.LocalDate;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import sistemagestionfinanzas.Prestamo;
import sistemagestionfinanzas.CuentaBancaria;

public class Prestamos extends JFrame {

    public static List<Prestamo> instanciasPrestamos = new ArrayList<>();

    private JButton paginaPrincipalButton;
    private JButton cuentasBancariasButton;
    private JButton plazosFijosButton;
    private JButton ingresosYGastosButton;
    private JButton stocksButton;
    private JButton tarjetasDeCreditoButton;
    private JButton prestamosButton;
    private JPanel fecha_de_vencimiento_panel;
    private JPanel PrestamosPanel;
    private JTextField tf_descripcion;
    private JTextField tf_nombre;
    private JComboBox<String> cb_tipo;
    private JTextField tf_monto_original;
    private JTextField tf_plazo;
    private JPanel fecha_inicio_panel;
    private JComboBox<String> cbEstatus;
    private JButton btnAgregarPrestamo;
    private JTable tabla_prestamos;
    private JComboBox<String> cb_cuentabancaria;
    private JScrollPane JScrollPane_prestamos;
    private JTextField tf_taza_interes;
    private DefaultTableModel modelo_tabla_prestamos;
    private JDatePickerImpl datePicker;

    public Prestamos() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Prestamos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(PrestamosPanel);

        // Implementación del JDatePicker para fecha de inicio
        UtilDateModel modelInicio = new UtilDateModel();
        Properties pInicio = new Properties();
        pInicio.put("text.today", "Hoy");
        pInicio.put("text.month", "Mes");
        pInicio.put("text.year", "Año");
        JDatePanelImpl datePanelImplInicio = new JDatePanelImpl(modelInicio, pInicio);
        JDatePickerImpl datePickerInicio = new JDatePickerImpl(datePanelImplInicio, new DateLabelFormatter());

        fecha_inicio_panel.setLayout(new BorderLayout());
        fecha_inicio_panel.add(datePickerInicio, BorderLayout.CENTER);

        // Implementación del JDatePicker para fecha de vencimiento
        UtilDateModel modelVencimiento = new UtilDateModel();
        Properties pVencimiento = new Properties();
        pVencimiento.put("text.today", "Hoy");
        pVencimiento.put("text.month", "Mes");
        pVencimiento.put("text.year", "Año");
        JDatePanelImpl datePanelImplVencimiento = new JDatePanelImpl(modelVencimiento, pVencimiento);
        JDatePickerImpl datePickerVencimiento = new JDatePickerImpl(datePanelImplVencimiento, new DateLabelFormatter());

        fecha_de_vencimiento_panel.setLayout(new BorderLayout());
        fecha_de_vencimiento_panel.add(datePickerVencimiento, BorderLayout.CENTER);

        // Actualizar el JComboBox de cuentas bancarias
        actualizarComboBoxCuentas();

        // Configuración de la tabla
        String[] columnNames = {"ID", "Nombre", "Descripción", "Monto Original", "Fecha Inicio", "Tipo Préstamo", "Plazo", "Fecha Vencimiento", "Cuota Mensual", "Cuenta Bancaria", "Tasa de Interés", "Estatus"};
        modelo_tabla_prestamos = new DefaultTableModel(columnNames, 0);
        tabla_prestamos.setModel(modelo_tabla_prestamos);

        // Acción del botón de agregar préstamo
        btnAgregarPrestamo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Obtener datos del formulario
                    String nombre = tf_nombre.getText();
                    String descripcion = tf_descripcion.getText();
                    String tipoPrestamo = (String) cb_tipo.getSelectedItem();
                    float montoOriginal = Float.parseFloat(tf_monto_original.getText());
                    float tasaInteres = Float.parseFloat(tf_taza_interes.getText());

                    // Convertir fechas
                    java.util.Date date_inicio = (java.util.Date) datePickerInicio.getModel().getValue();
                    LocalDate fechaInicio = date_inicio.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                    java.util.Date date_vencimiento = (java.util.Date) datePickerVencimiento.getModel().getValue();
                    LocalDate fechaVencimiento = date_vencimiento.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    int plazo = Integer.parseInt(tf_plazo.getText());
                    String cuentaSeleccionada = (String) cb_cuentabancaria.getSelectedItem();

                    // Obtener estatus seleccionado y mapear a entero
                    String estatus = (String) cbEstatus.getSelectedItem();
                    int estatusInt = estatus.equals("Activo") ? 1 : 0;

                    // Encontrar la cuenta bancaria seleccionada
                    CuentaBancaria cuentaVinculada = null;
                    for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
                        String cuentaBuscada = cuenta.getNumeroCuenta() + " " + cuenta.getNombre();
                        if (cuentaBuscada.equals(cuentaSeleccionada)) {
                            cuentaVinculada = cuenta;
                            break;
                        }
                    }

                    if (cuentaVinculada == null) {
                        JOptionPane.showMessageDialog(Prestamos.this, "Cuenta bancaria seleccionada no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Crear el objeto Prestamo con los datos capturados
                    Prestamo nuevoPrestamo = new Prestamo(nombre, descripcion, montoOriginal, tasaInteres, fechaInicio, tipoPrestamo, plazo, fechaVencimiento, cuentaVinculada);

                    // Asignar estatus al préstamo
                    nuevoPrestamo.setEstatus(estatusInt);

                    // Guardar el préstamo en la base de datos
                    // nuevoPrestamo.guardarPrestamoBaseDatos();

                    // Cargar los préstamos en la tabla
                    cargarPrestamos();

                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(Prestamos.this, "Préstamo creado exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Prestamos.this, "Error al crear el préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Otros botones
        paginaPrincipalButton.addActionListener(new ActionListener() {
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

        tarjetasDeCreditoButton.addActionListener(new ActionListener() {
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

    private void actualizarComboBoxCuentas() {
        cb_cuentabancaria.removeAllItems();
        for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
            cb_cuentabancaria.addItem(cuenta.getNumeroCuenta().toString() + " " + cuenta.getNombre().toString());
        }
    }

    private void cargarPrestamos() {
        modelo_tabla_prestamos.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

        // Agregar cada préstamo a la tabla
        for (Prestamo prestamo : Prestamo.instanciasPrestamos) {
            String estatus = prestamo.getEstatus() == 1 ? "Activo" : "Inactivo"; // Convertir el estatus a cadena

            Object[] rowData = {
                    prestamo.getId(),
                    prestamo.getNombre(),
                    prestamo.getDescripcion(),
                    prestamo.getMontoOriginal(),
                    prestamo.getFechaInicio(), // Asegúrate de que este campo sea de tipo Date o similar
                    prestamo.getTipoPrestamo(),
                    prestamo.getPlazo(),
                    prestamo.getFechaVencimiento(), // Asegúrate de que este campo sea de tipo Date o similar
                    prestamo.calcularPagoMensual(), // Calcula la cuota mensual usando el método de la clase Prestamo
                    prestamo.getCuentaBancaria().getNumeroCuenta() + " " + prestamo.getCuentaBancaria().getNombre(),
                    prestamo.getTasaInteres(),
                    estatus // Asignar el estatus como cadena
            };
            modelo_tabla_prestamos.addRow(rowData);
            adjustColumnWidths(tabla_prestamos);
        }
    }

    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    public static void main(String[] args) {
        // Crear y mostrar la ventana
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Prestamos ventana = new Prestamos();
                ventana.setVisible(true);
            }
        });
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



