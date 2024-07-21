package Grafico;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import sistemagestionfinanzas.Stock;
import sistemagestionfinanzas.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Properties;

public class Stocks extends JFrame {
    private JPanel StocksPanel;
    private JButton menuPrincipalButton;
    private JPanel datePanelContainer;
    private JButton cuentaBancariaButton;
    private JButton prestamosButton;
    private JButton tarjetasDeCreditoButton;
    private JButton stocksButton;
    private JButton plazoFijosButton;
    private JButton ingresosYGastosButton;
    private JTextField ftCantidad;
    private JTextField tfNombreEmpresa;
    private JTextField tfNombre;
    private JLabel lbNombre;
    private JTextField tfDividendoAccion;
    private JButton agregarNuevoStockButton;
    private JTable tablaStocks;
    private JTextField tdDescripcion;
    private JTextField tfSimbolo;
    private JTextField tfPrecioCompra;
    private JTextField tfDividendoAccio;
    private JTextField tfFrecuenciaDividendos;
    private JTextField tfPrecioSctual;
    private JComboBox<String> cbFrecuenciaIng;
    private JDatePickerImpl datePicker;


    public Stocks() {

        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Stocks");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(StocksPanel);

        configureNavigationButtons();

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

        agregarNuevoStockButton.addActionListener(new ActionListener() {
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

    }

    private void configureNavigationButtons() {
        menuPrincipalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard newframe = new Dashboard();
                newframe.setVisible(true);
                dispose();
            }
        });

        cuentaBancariaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        plazoFijosButton.addActionListener(new ActionListener() {
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

        tarjetasDeCreditoButton.addActionListener(new ActionListener() {
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

    private void validarCampos() throws Exception {
        String nombreEmpresa = tfNombreEmpresa.getText();
        String simbolo = tfSimbolo.getText();
        String cantidadStr = ftCantidad.getText();
        String precioCompraStr = tfPrecioCompra.getText();
        String dividendoAccionStr = tfDividendoAccion.getText();
        String frecuenciaDividendos = tfFrecuenciaDividendos.getText();

        if (nombreEmpresa.trim().isEmpty()) {
            throw new Exception("El campo 'Nombre Empresa' es obligatorio.");
        }

        if (simbolo.trim().isEmpty()) {
            throw new Exception("El campo 'Símbolo' es obligatorio.");
        }

        if (cantidadStr.trim().isEmpty()) {
            throw new Exception("El campo 'Cantidad' es obligatorio.");
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
        String nombre_empresa = tfNombreEmpresa.getText();
        String simbolo = tfSimbolo.getText();
        int cantidad = Integer.parseInt(ftCantidad.getText().trim());
        String nombre = tfNombre.getText();
        float precio_compra = Float.parseFloat(tfPrecioCompra.getText().trim());
        float dividendo = Float.parseFloat(tfDividendoAccion.getText().trim());
        int frecuencia_dividendos = Integer.parseInt(tfFrecuenciaDividendos.getText());
        LocalDate fecha_actual = LocalDate.now();

        Stock nuevoStock = new Stock(nombre, "Este es un stock de " + nombre_empresa, fecha_actual, nombre_empresa, simbolo, cantidad, precio_compra, "Sector? Falta en el form", dividendo, frecuencia_dividendos);
        Usuario.getUsuarioActual().agregarFinanceItem(nuevoStock);
        limpiarCampos();
    }

    private void limpiarCampos() {
        tfNombreEmpresa.setText("");
        tfNombre.setText("");
        tfSimbolo.setText("");
        ftCantidad.setText("");
        tfPrecioCompra.setText("");
        tfDividendoAccion.setText("");
        tfFrecuenciaDividendos.setText("");
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
}

