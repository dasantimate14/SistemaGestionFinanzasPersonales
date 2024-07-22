package Grafico;

import sistemagestionfinanzas.BaseDeDatos;
import sistemagestionfinanzas.CuentaBancaria;
import sistemagestionfinanzas.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.ResultSet;

public class ConsultarMovimientos extends JFrame {
    private JPanel movimiento_panel;
    private JComboBox cbNumeroCuenta;
    private JComboBox<String> cbNombreCuenta;
    private JButton btn_buscar;
    private JButton volver_btn;
    private JTable movimientos_table;
    private DefaultTableModel modelo;

    public ConsultarMovimientos() {
        // Configuración de la ventana
        setSize(960, 920);
        setTitle("Consultar Movimiento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(movimiento_panel);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID", "Nombre", "Descripción", "Monto", "Tipo", "Fecha"});
        movimientos_table.setModel(modelo);

        actualizarComboBoxes();

        volver_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        btn_buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarDatos();
                    String numeroCuenta = String.valueOf(cbNumeroCuenta.getSelectedItem());
                    try {
                        BaseDeDatos.establecerConexion();
                        ResultSet movimientos = BaseDeDatos.realizarConsultaSelect("SELECT id, nombre, descripcion, montoOriginal, tipo, fechaInicio, frecuencia, idCuentaBancaria " +
                                        "FROM ingresos " +
                                        "WHERE idUsuario = ? AND numeroCuenta = ?" +
                                        "UNION " +
                                        "SELECT id, nombre, descripcion, montoOriginal, tipo, fechaInicio, frecuencia, idCuentaBancaria " +
                                        "FROM gastos " +
                                        "WHERE idUsuario = ? AND numeroCuenta = ?",
                                new String[]{Usuario.usuario_actual.getId(), numeroCuenta, Usuario.usuario_actual.getId(), numeroCuenta});

                        while(movimientos.next()){
                            Object[] movimiento = {movimientos.getString("id"), movimientos.getString("nombre"), movimientos.getString("descripcion"), movimientos.getFloat("montoOriginal"), movimientos.getString("tipo"), movimientos.getString("fechaInicio"), movimientos.getString("frecuencia"), movimientos.getString("idCuentaBancaria")};
                            modelo.addRow(movimiento);
                        }
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ConsultarMovimientos.this, "Por favor, ingrese datos válidos. " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    BaseDeDatos.cerrarConexion();
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


        // Validar nombre de cuenta
        String nombreCuenta = (String) cbNombreCuenta.getSelectedItem();
        if (nombreCuenta == null || nombreCuenta.isEmpty()) {
            throw new Exception("Debe seleccionar un nombre de cuenta.");
        }


        // Validar tipo de cuenta
        String tipoCuenta = (String) cbNumeroCuenta.getSelectedItem();
        if (tipoCuenta == null || tipoCuenta.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar un tipo de cuenta.");
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

        public void buscarMovimientos() throws SQLException {



        }
    }
}
