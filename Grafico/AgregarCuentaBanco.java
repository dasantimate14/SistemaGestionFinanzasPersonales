package Grafico;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import sistemagestionfinanzas.CuentaBancaria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Calendar;

public class AgregarCuentaBanco extends JFrame {
    private JPanel AgregarCuentaBancoPanel;
    private JComboBox<String> cbTipoCuenta;
    private JTextField tfNombreCuenta;
    private JTextField tfNumeroCuenta;
    private JButton crearButton;
    private JButton Volverbtn2;
    private JTextField tfBancoOrigen;
    private JTextField tfSaldoInicial;
    private JTextField tfTasaInteres;
    private JTextField tfDescripcion;
    private JPanel panel_fecha_inicio;
    private JDatePickerImpl datePicker;
    private CuentaBancariaG parentFrame;

    public AgregarCuentaBanco(CuentaBancariaG parent) {
        this.parentFrame = parent;

        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Agregar Cuentas de Banco");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(AgregarCuentaBancoPanel);

        // Implementación del JDatePicker para fecha de inicio
        UtilDateModel modelInicio = new UtilDateModel();
        Properties pInicio = new Properties();
        pInicio.put("text.today", "Hoy");
        pInicio.put("text.month", "Mes");
        pInicio.put("text.year", "Año");
        JDatePanelImpl datePanelImplInicio = new JDatePanelImpl(modelInicio, pInicio);
        datePicker = new JDatePickerImpl(datePanelImplInicio, new DateLabelFormatter());

        panel_fecha_inicio.setLayout(new BorderLayout());
        panel_fecha_inicio.add(datePicker, BorderLayout.CENTER);

        // Botón para regresar al menú principal de Cuenta de Banco
        Volverbtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setVisible(true);
                dispose();
            }
        });

        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validar datos del formulario
                    validarDatos();

                    // Obtener datos del formulario
                    String nombreCuenta = tfNombreCuenta.getText();
                    String numeroCuenta = tfNumeroCuenta.getText();
                    String bancoOrigen = tfBancoOrigen.getText();
                    float saldoInicial = Float.parseFloat(tfSaldoInicial.getText());
                    float tasaInteres = Float.parseFloat(tfTasaInteres.getText());
                    String descripcion = tfDescripcion.getText();
                    String tipoCuenta = (String) cbTipoCuenta.getSelectedItem();

                    java.util.Date dateInicio = (java.util.Date) datePicker.getModel().getValue();
                    LocalDate fechaInicio = dateInicio.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                    CuentaBancaria nuevaCuenta = new CuentaBancaria(
                            nombreCuenta,
                            descripcion,
                            saldoInicial,
                            tasaInteres,
                            fechaInicio,
                            bancoOrigen,
                            numeroCuenta,
                            tipoCuenta,
                            "1"
                    );
                    nuevaCuenta.actualizarInformacion();
                    parentFrame.getTableModel().addRow(new Object[]{nombreCuenta, descripcion, tipoCuenta, numeroCuenta, bancoOrigen, saldoInicial, tasaInteres, fechaInicio,   nuevaCuenta.calcularPromedioMensual(), nuevaCuenta.calcularPromedioAnual(), nuevaCuenta.calcularBalanceActual(), nuevaCuenta.getInteres()});

                    JOptionPane.showMessageDialog(AgregarCuentaBanco.this, "Cuenta bancaria creada exitosamente.");

                    tfNombreCuenta.setText("");
                    tfNumeroCuenta.setText("");
                    tfBancoOrigen.setText("");
                    tfSaldoInicial.setText("");
                    tfTasaInteres.setText("");
                    tfDescripcion.setText("");
                    cbTipoCuenta.setSelectedIndex(0); // Ajusta según el índice por defecto
                    datePicker.getModel().setValue(null);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AgregarCuentaBanco.this, "Error en el formato numérico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AgregarCuentaBanco.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    private void validarDatos() throws Exception {
        String nombre = tfNombreCuenta.getText();
        String descripcion = tfDescripcion.getText();
        String tipo_cuenta = (String) cbTipoCuenta.getSelectedItem();
        String numero = tfNumeroCuenta.getText();
        String banco_origen = tfBancoOrigen.getText();
        String saldo_inicial = tfSaldoInicial.getText();
        String tasa_interes = tfTasaInteres.getText();
        Object fecha_inicio = datePicker.getModel().getValue();

        if (nombre == null || nombre.isEmpty()) {
            throw new Exception("Debe ingresar el nombre de la cuenta.");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new Exception("Debe ingresar la descripción de la cuenta.");
        }

        if (tipo_cuenta == null || tipo_cuenta.isEmpty() || tipo_cuenta.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar un tipo de cuenta.");
        }

        if (numero == null || numero.isEmpty()) {
            throw new Exception("Debe ingresar el número de la cuenta.");
        }
        if (!numero.matches("[0-9-]+")) {
            throw new Exception("El número de cuenta solo puede contener números y guiones.");
        }
        if (numero.length() > 20) {
            throw new Exception("El número de cuenta no puede tener más de 20 caracteres.");
        }

        if (banco_origen == null || banco_origen.isEmpty()) {
            throw new Exception("Debe ingresar el banco de origen de la cuenta.");
        }
        if (!banco_origen.matches("[a-zA-Z]+")) {
            throw new Exception("El banco de origen solo puede contener letras.");
        }

        if (saldo_inicial == null || saldo_inicial.isEmpty()) {
            throw new Exception("Debe ingresar el saldo inicial.");
        }
        if (!saldo_inicial.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("El saldo inicial solo debe contener números y con hasta dos decimales.");
        }

        if (tasa_interes == null || tasa_interes.isEmpty()) {
            throw new Exception("Debe ingresar la tasa de interés.");
        }
        if (!tasa_interes.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("La tasa de interés solo debe contener números y con hasta dos decimales.");
        }

        if (fecha_inicio == null) {
            throw new Exception("Debe seleccionar una fecha de inicio.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CuentaBancariaG parentFrame = new CuentaBancariaG();
                parentFrame.setVisible(true);

                AgregarCuentaBanco frame = new AgregarCuentaBanco(parentFrame);
                frame.setVisible(true);
            }
        });
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
