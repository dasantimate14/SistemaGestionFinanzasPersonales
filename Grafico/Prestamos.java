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
    private JPanel PrestamosPanel;
    private JTextField tf_descripcion;
    private JTextField tf_nombre;
    private JComboBox<String> cb_tipo;
    private JTextField tf_monto_original;
    private JTextField tf_plazo;
    private JPanel fecha_inicio_panel;
    private JComboBox<String> cb_estatus;
    private JButton btn_agregar_prestamo;
    private JTable tabla_prestamos;
    private JComboBox<String> cb_cuentabancaria;
    private JScrollPane j_scroll_pane_prestamos;
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
        datePicker = new JDatePickerImpl(datePanelImplInicio, new DateLabelFormatter());

        fecha_inicio_panel.setLayout(new BorderLayout());
        fecha_inicio_panel.add(datePicker, BorderLayout.CENTER);

        // Actualizar el JComboBox de cuentas bancarias
        actualizarComboBoxCuentas();

        // Configuración de la tabla
        String[] columnNames = {"ID", "Nombre", "Descripción", "Monto Original", "Fecha Inicio", "Tipo Préstamo", "Plazo", "Fecha Vencimiento", "Cuota Mensual", "Cuenta Bancaria", "Tasa de Interés", "Estatus"};
        modelo_tabla_prestamos = new DefaultTableModel(columnNames, 0);
        tabla_prestamos.setModel(modelo_tabla_prestamos);

        // Configurar renderizador personalizado para formatear números


        // Acción del botón de agregar préstamo
        btn_agregar_prestamo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Obtener datos del formulario
                    String nombre = tf_nombre.getText();
                    if (nombre.isEmpty()) {
                        throw new IllegalArgumentException("El nombre no puede estar vacío.");
                    }

                    String descripcion = tf_descripcion.getText();
                    if (descripcion.isEmpty()) {
                        throw new IllegalArgumentException("La descripción no puede estar vacía.");
                    }

                    String tipoPrestamo = (String) cb_tipo.getSelectedItem();
                    if (tipoPrestamo == null) {
                        throw new NullPointerException("Debe seleccionar un tipo de préstamo.");
                    }

                    float montoOriginal;
                    try {
                        montoOriginal = Float.parseFloat(tf_monto_original.getText());
                    } catch (NumberFormatException ex) {
                        throw new NumberFormatException("Monto original debe ser un número válido.");
                    }

                    float tasaInteres;
                    try {
                        tasaInteres = Float.parseFloat(tf_taza_interes.getText());
                    } catch (NumberFormatException ex) {
                        throw new NumberFormatException("Tasa de interés debe ser un número válido.");
                    }

                    java.util.Date date_inicio = (java.util.Date) datePicker.getModel().getValue();
                    if (date_inicio == null) {
                        throw new NullPointerException("Debe seleccionar una fecha de inicio.");
                    }
                    LocalDate fechaInicio = date_inicio.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                    int plazo;
                    try {
                        plazo = Integer.parseInt(tf_plazo.getText());
                    } catch (NumberFormatException ex) {
                        throw new NumberFormatException("Plazo debe ser un número entero válido.");
                    }

                    String cuentaSeleccionada = (String) cb_cuentabancaria.getSelectedItem();
                    if (cuentaSeleccionada == null) {
                        throw new NullPointerException("Debe seleccionar una cuenta bancaria.");
                    }

                    // Obtener estatus seleccionado y mapear a entero
                    String estatus = (String) cb_estatus.getSelectedItem();
                    if (estatus == null) {
                        throw new NullPointerException("Debe seleccionar un estatus.");
                    }
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
                        throw new IllegalArgumentException("Cuenta bancaria seleccionada no encontrada.");
                    }

                    // Crear el objeto Prestamo con los datos capturados
                    Prestamo nuevoPrestamo = new Prestamo(nombre, descripcion, montoOriginal, tasaInteres, fechaInicio, tipoPrestamo, plazo, cuentaVinculada);

                    // Asignar estatus al préstamo
                    nuevoPrestamo.setEstatus(estatusInt);

                    // Guardar el préstamo en la base de datos
                    // nuevoPrestamo.guardarPrestamoBaseDatos();

                    // Cargar los préstamos en la tabla
                    cargarPrestamos();

                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(Prestamos.this, "Préstamo creado exitosamente.");

                    // Limpiar campos del formulario
                    limpiarCamposFormulario();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Prestamos.this, "Error de formato: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(Prestamos.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(Prestamos.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        // Limpiar la tabla antes de cargar nuevos datos
        modelo_tabla_prestamos.setRowCount(0);

        // Recorrer la lista de préstamos e insertar cada uno en la tabla
        for (Prestamo prestamo : Prestamo.instanciasPrestamos) {
            Object[] fila = new Object[12];
            fila[0] = prestamo.getId();
            fila[1] = prestamo.getNombre();
            fila[2] = prestamo.getDescripcion();
            fila[3] = prestamo.getMontoOriginal();
            fila[4] = prestamo.getFechaInicio();
            fila[5] = prestamo.getTipoPrestamo();
            fila[6] = prestamo.getPlazo();
            fila[7] = prestamo.getFechaVencimiento();
            fila[8] = prestamo.calcularPagoMensual();
            fila[9] = prestamo.getCuentaBancaria().getNumeroCuenta() + " " + prestamo.getCuentaBancaria().getNombre();
            fila[10] = prestamo.getTasaInteres();
            fila[11] = prestamo.getEstatus() == 1 ? "Activo" : "Inactivo";
            modelo_tabla_prestamos.addRow(fila);
            adjustColumnWidths(tabla_prestamos);
        }
    }

    private void limpiarCamposFormulario() {
        tf_nombre.setText("");
        tf_descripcion.setText("");
        cb_tipo.setSelectedIndex(-1);
        tf_monto_original.setText("");
        tf_plazo.setText("");
        cb_estatus.setSelectedIndex(-1);
        cb_cuentabancaria.setSelectedIndex(-1);
        tf_taza_interes.setText("");
        datePicker.getModel().setValue(null);
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



