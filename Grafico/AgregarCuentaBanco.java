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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class  AgregarCuentaBanco extends JFrame {
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


    public AgregarCuentaBanco() {
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
        datePicker = new JDatePickerImpl(datePanelImplInicio, new DateLabelFormatter()); // Asigna a datePicker

        panel_fecha_inicio.setLayout(new BorderLayout());
        panel_fecha_inicio.add(datePicker, BorderLayout.CENTER);

        //Botón para regresar al menú principal de Cuenta de Banco
        Volverbtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Obtener datos del formulario
                    String nombreCuenta = tfNombreCuenta.getText();
                    String numeroCuenta = tfNumeroCuenta.getText();
                    String bancoOrigen = tfBancoOrigen.getText();
                    float saldoInicial = Float.parseFloat(tfSaldoInicial.getText());
                    float tasaInteres = Float.parseFloat(tfTasaInteres.getText());
                    String descripcion = tfDescripcion.getText();
                    String tipoCuenta = (String) cbTipoCuenta.getSelectedItem();

                    // Convertir fecha de inicio
                    java.util.Date dateInicio = (java.util.Date) datePicker.getModel().getValue();
                    LocalDate fechaInicio = dateInicio.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                    // Validar que todos los campos sean completos y válidos
                    if (nombreCuenta.isEmpty() || numeroCuenta.isEmpty() || bancoOrigen.isEmpty()) {
                        throw new Exception("Todos los campos deben ser completados.");
                    }

                    // Crear el objeto CuentaBancaria con los datos capturados
                    CuentaBancaria nuevaCuenta = new CuentaBancaria(
                            nombreCuenta,
                            descripcion,
                            saldoInicial,
                            tasaInteres,
                            fechaInicio,
                            bancoOrigen,
                            numeroCuenta,
                            tipoCuenta,
                            "1" // Ajusta el ID de usuario según sea necesario
                    );



                    // Guardar la cuenta en la base de datos
                    //nuevaCuenta.guardarCuentaBancariaBaseDatos();

                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(AgregarCuentaBanco.this, "Cuenta bancaria creada exitosamente.");

                    // Limpiar campos después de la creación
                    tfNombreCuenta.setText("");
                    tfNumeroCuenta.setText("");
                    tfBancoOrigen.setText("");
                    tfSaldoInicial.setText("");
                    tfTasaInteres.setText("");
                    tfDescripcion.setText("");
                    cbTipoCuenta.setSelectedIndex(0); // Ajusta según el índice por defecto
                    datePicker.getModel().setValue(null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AgregarCuentaBanco.this, "Error al crear la cuenta bancaria: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        String numero = tfNumeroCuenta.getText();

        if (nombre.isEmpty() || nombre == null) {
            throw new Exception("Debe ingresar el nombre de la cuenta.");
        }
        if (numero.isEmpty() || numero == null) {
            throw new Exception("Debe ingresar el número de la cuenta.");
        }
        if (!numero.matches("[0-9-]+")) {
            throw new Exception("El número de cuenta solo puede contener números y guiones.");
        }
        if (numero.length() > 20) {
            throw new Exception("El número de cuenta no puede tener más de 20 caracteres.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AgregarCuentaBanco frame = new AgregarCuentaBanco();
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

