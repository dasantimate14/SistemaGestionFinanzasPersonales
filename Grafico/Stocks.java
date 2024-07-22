package Grafico;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import sistemagestionfinanzas.Stock;
import sistemagestionfinanzas.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Properties;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


import static sistemagestionfinanzas.Usuario.usuario_actual;

public class Stocks extends JFrame {
    private JPanel stocks_panel;
    private JButton menu_principal_button;
    private JPanel date_panel_container;
    private JButton cuenta_bancaria_button;
    private JButton prestamos_button;
    private JButton tarjetas_credito_button;
    private JButton stocks_button;
    private JButton plazos_fijos_button;
    private JButton ingresos_y_gastos_button;
    private JTextField fr_cantidad;
    private JTextField tf_nombre_empresa;
    private JTextField tf_nombre;
    private JLabel lbNombre;
    private JTextField tf_dividendo_accion;
    private JButton agregar_nuevo_stock_button;
    private JTable tabla_stocks;
    private JTextField tf_descripcion;
    private JTextField tf_simbolo;
    private JTextField tf_precio_compra;
    private JComboBox comboBoxSector;
    private JComboBox comboBoxFrecuencia;
    private JPanel tabla_panel;
    private JScrollPane tabla_scroll_panel;
    private JTextField tf_precio_actual;
    private JComboBox<String> cbFrecuenciaIng;
    private JDatePickerImpl date_picker_stock;
    private DefaultTableModel table_model;


    public Stocks() {

        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Stocks");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(stocks_panel);

        configureNavigationButtons();

        //Configuración JTable
        table_model = new DefaultTableModel();
        tabla_stocks.setModel(table_model);
        table_model.addColumn("Nombre Acción");
        table_model.addColumn("ID");
        table_model.addColumn("Descripción");
        table_model.addColumn("Nombre Empresa");
        table_model.addColumn("Sector");
        table_model.addColumn("Símbolo");
        table_model.addColumn("Cantidad");
        table_model.addColumn("Dividendo Por Acción");
        table_model.addColumn("Frecuencia de Pago de Dividendos");
        table_model.addColumn("Precio Compra");
        table_model.addColumn("Precio Actual");
        table_model.addColumn("Monto Original");
        table_model.addColumn("Monto Actual");
        table_model.addColumn("Cantidad de Ganancia/Perdida");
        table_model.addColumn("Porcentaje de Ganancia");
        table_model.addColumn("Valor Mensual Promedio");
        table_model.addColumn("Valor Anual Promedio");
        table_model.addColumn("Fecha Inicio");
        table_model.addColumn("Tipo");

        // Configurar el layout del panel de Stocks para agregar el JScrollPane en lugar de la tabla directamente
        tabla_panel.setLayout(new BorderLayout());
        tabla_panel.add(tabla_scroll_panel, BorderLayout.CENTER);

        // Implementación del JDatePicker dentro del GUI
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(model, p);
        date_picker_stock = new JDatePickerImpl(datePanelImpl, new DateLabelFormatter());

        date_panel_container.setLayout(new BorderLayout());
        date_panel_container.add(date_picker_stock, BorderLayout.CENTER);

        // Agregar valores a comboBoxSector
        comboBoxSector.addItem("Energía");
        comboBoxSector.addItem("Material");
        comboBoxSector.addItem("Industrial");
        comboBoxSector.addItem("Utilidades");
        comboBoxSector.addItem("Salud");
        comboBoxSector.addItem("Consumo Discrecional");
        comboBoxSector.addItem("Consumo Básico");
        comboBoxSector.addItem("Tecnología de la Información");
        comboBoxSector.addItem("Servicios de Comunicación");
        comboBoxSector.addItem("Bienes Raíces");

        //Agregar valores al comboBoxFrecuencia
        for(int i = 0; i <= 12; i ++){
            comboBoxFrecuencia.addItem(i);
        }

        agregar_nuevo_stock_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCampos();
                    agregarNuevoStock();
                    JOptionPane.showMessageDialog(null, "Stock agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cargarStocksTabla();
    }

    private void configureNavigationButtons() {
        menu_principal_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard newframe = new Dashboard();
                newframe.setVisible(true);
                dispose();
            }
        });

        cuenta_bancaria_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        plazos_fijos_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlazoFijos newframe = new PlazoFijos();
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

        tarjetas_credito_button.addActionListener(new ActionListener() {
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
    }

    private void validarCampos() throws Exception {
        String nombreEmpresa = tf_nombre_empresa.getText();
        String nombre = tf_nombre.getText();
        String simbolo = tf_simbolo.getText();
        String cantidadStr = fr_cantidad.getText();
        String precioCompraStr = tf_precio_compra.getText();
        String dividendoAccionStr = tf_dividendo_accion.getText();
        String frecuenciaDividendos = comboBoxFrecuencia.getSelectedItem().toString();
        String sector = comboBoxSector.getSelectedItem().toString();

        if(nombre.trim().isEmpty()){
            throw new Exception("El campo 'Nombre Acción' es obligatorio");
        }

        if (nombreEmpresa.trim().isEmpty()) {
            throw new Exception("El campo 'Nombre Empresa' es obligatorio.");
        }

        if (simbolo.trim().isEmpty()) {
            throw new Exception("El campo 'Símbolo' es obligatorio.");
        }

        if (cantidadStr.trim().isEmpty()) {
            throw new Exception("El campo 'Cantidad' es obligatorio.");
        }

        if(sector.trim().isEmpty()) {
            throw new Exception("El campo 'Sector' es obligatorio.");
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr.trim());
            if (cantidad <= 0) {
                throw new Exception("La cantidad debe ser un número positivo.");
            }
        } catch (NumberFormatException e) {
            throw new Exception("La cantidad debe ser un número válido.");
        }

        if (precioCompraStr.trim().isEmpty()) {
            throw new Exception("El campo 'Precio de Compra' es obligatorio.");
        }

        double precioCompra;
        try {
            precioCompra = Double.parseDouble(precioCompraStr.trim());
            if (precioCompra <= 0) {
                throw new Exception("El precio de compra debe ser un número positivo.");
            }
        } catch (NumberFormatException e) {
            throw new Exception("El precio de compra debe ser un número válido.");
        }

        if (dividendoAccionStr.trim().isEmpty()) {
            throw new Exception("El campo 'Dividendo por acción' es obligatorio.");
        }

        double dividendo;
        try {
            dividendo = Double.parseDouble(dividendoAccionStr.trim());
            if (dividendo < 0) {
                throw new Exception("El dividendo por acción no puede ser un número negativo.");
            }
        } catch (NumberFormatException e) {
            throw new Exception("El dividendo por acción debe ser un número válido.");
        }

        if (frecuenciaDividendos.trim().isEmpty()) {
            throw new Exception("El campo 'Frecuencia Dividendos' es obligatorio.");
        }
    }

    private void agregarNuevoStock() {
        String nombre_empresa = tf_nombre_empresa.getText();
        String simbolo = tf_simbolo.getText();
        String descripcion = tf_descripcion.getText();
        int cantidad = Integer.parseInt(fr_cantidad.getText().trim());
        String nombre = tf_nombre.getText();
        float precio_compra = Float.parseFloat(tf_precio_compra.getText().trim());
        float dividendo = Float.parseFloat(tf_dividendo_accion.getText().trim());
        int frecuencia_dividendos = Integer.parseInt(comboBoxFrecuencia.getSelectedItem().toString());
        String sector = comboBoxSector.getSelectedItem().toString();

        // Obtener la fecha del JDatePicker y convertirla a LocalDate
        java.util.Date date_ingreso = (java.util.Date) date_picker_stock.getModel().getValue();

        //Si no se da la fecha se asume que es la fecha actual
        LocalDate fecha_inicio;
        if(date_ingreso == null) {
            fecha_inicio = LocalDate.now();
        } else {
            fecha_inicio = date_ingreso.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }

        Stock nuevo_stock = new Stock(nombre, descripcion, fecha_inicio, nombre_empresa, simbolo, cantidad, precio_compra, sector, dividendo, frecuencia_dividendos);
        nuevo_stock.guardarStockBaseDatos(Usuario.getIdUsuarioActual());
        try {
            nuevo_stock.actualizarInformacion();
        } catch (IOException e) {
            try {
                throw new Exception("Problemas para actualizar el Stock." + e.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        // Agregar datos al modelo de la tabla
        try {
            table_model.addRow(new Object[]{
                    nombre_empresa,
                    "ID",
                    descripcion,
                    nombre_empresa,
                    sector,
                    simbolo,
                    cantidad,
                    dividendo,
                    frecuencia_dividendos,
                    precio_compra,
                    nuevo_stock.getPrecioActual(),
                    nuevo_stock.getMontoOriginal(),
                    nuevo_stock.obtenerPrecioActual(),
                    nuevo_stock.calcularGanaciaPerdida(),
                    nuevo_stock.getPorcentajeGanancia(),
                    nuevo_stock.calcularPromedioMensual(),
                    nuevo_stock.calcularPromedioAnual(),
                    fecha_inicio,
                    nuevo_stock.getTipo()

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usuario_actual.agregarFinanceItem(nuevo_stock);
        limpiarCampos();
    }

    private void limpiarCampos() {
        tf_nombre_empresa.setText("");
        tf_nombre.setText("");
        tf_simbolo.setText("");
        fr_cantidad.setText("");
        tf_precio_compra.setText("");
        tf_dividendo_accion.setText("");
        tf_descripcion.setText("");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Stocks frame = new Stocks();
                frame.setVisible(true);
            }
        });
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
            return"";
        }

    }
    private void cargarStocksTabla() {
        try {
            for (Stock stock : Stock.instancias_stocks){
                table_model.addRow(new Object[]{
                        stock.getNombre(),  // Nombre Acción
                        stock.getId(),            // ID (asumiendo que tienes un método getId())
                        stock.getDescripcion(),   // Descripción
                        stock.getNombreEmpresa(), // Nombre Empresa
                        stock.getSector(),        // Sector
                        stock.getSimbolo(),       // Símbolo
                        stock.getCantidad(),      // Cantidad
                        stock.getDividendoPorAccion(), // Dividendo Por Acción
                        stock.getFrecuenciaDividendos(), // Frecuencia de Pago de Dividendos
                        stock.getPrecioCompra(),  // Precio Compra
                        stock.obtenerPrecioActual(),  // Precio Actual
                        stock.getMontoOriginal(), // Monto Original
                        stock.getMontoActual(),   // Monto Actual
                        stock.calcularGanaciaPerdida(), // Cantidad de Ganancia/Perdida
                        stock.getPorcentajeGanancia(), // Porcentaje de Ganancia
                        stock.calcularPromedioMensual(), // Valor Mensual Promedio
                        stock.calcularPromedioAnual(),
                        stock.getFechaInicio(),   // Fecha Inicio
                        stock.getTipo()           // Tipo
                });
            }
            adjustColumnWidths(tabla_stocks);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

