package Grafico;

import sistemagestionfinanzas.CuentaBancaria;
import sistemagestionfinanzas.TarjetaCredito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Tarjetas extends JFrame {
    private JButton bt_menu;
    private JButton cuentas_bancarias_button;
    private JButton prestamo_button;
    private JButton plazos_fijos_button;
    private JButton tarjetas_de_credito_button;
    private JButton stocks_button;
    private JButton ingresos_y_gastos_button;
    private JPanel tarjeta_panel;
    private JComboBox<String> cb_tipo_tarjeta;
    private JTextField tf_limite_credito;
    private JTextField tf_saldo_actual;
    private JTextField tf_numero_tarjeta;
    private JComboBox<String> cb_cuenta_bancaria;
    private JButton agregar_button;
    private JTable tarjetas_table;
    private JScrollPane sp_tarjetas;

    public Tarjetas() {
        setSize(930, 920);
        setTitle("Tarjetas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(tarjeta_panel);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Tipo");
        model.addColumn("Límite de Crédito");
        model.addColumn("Saldo Actual");
        model.addColumn("Número de Tarjeta");
        model.addColumn("Cuenta Bancaria");
        tarjetas_table.setModel(model);

        cargarCuentasBancarias();
        cargarDatosTarjetas();

        agregar_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCampos();
                    agregarTarjeta();
                    JOptionPane.showMessageDialog(null, "Tarjeta agregada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bt_menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard newframe = new Dashboard();
                newframe.setVisible(true);
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

        stocks_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });

        prestamo_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prestamos newframe = new Prestamos();
                newframe.setVisible(true);
                dispose();
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarCuentasBancarias();
        }
    }

    private void cargarCuentasBancarias() {
        cb_cuenta_bancaria.removeAllItems();
        for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
            cb_cuenta_bancaria.addItem(cuenta.getNumeroCuenta() + " " + cuenta.getNombre());
        }
    }

    private void cargarDatosTarjetas() {
        try {
            DefaultTableModel model = (DefaultTableModel) tarjetas_table.getModel();
            model.setRowCount(0);
            for (TarjetaCredito tarjeta : TarjetaCredito.instanciasTarjetas) {
                model.addRow(new Object[]{
                        tarjeta.getTipo(),
                        tarjeta.getLimiteCredito(),
                        tarjeta.getSaldoActual(),
                        tarjeta.getNumero(),
                        tarjeta.getCuentaBancaria().getNumeroCuenta() + " " + tarjeta.getCuentaBancaria().getNombre()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos de tarjetas: " + e.getMessage());
        }
    }

    private void agregarTarjeta() throws Exception {
        validarCampos();

        String tipo_tarjeta = (String) cb_tipo_tarjeta.getSelectedItem();
        float limite_credito = (float) Double.parseDouble(tf_limite_credito.getText().trim());
        float saldo_actual = (float) Double.parseDouble(tf_saldo_actual.getText().trim());
        String numero_tarjeta_str = tf_numero_tarjeta.getText().trim();
        int numero_tarjeta = Integer.parseInt(numero_tarjeta_str);
        String cuenta_bancaria_seleccionada = (String) cb_cuenta_bancaria.getSelectedItem();
        String nombre = "Nombre de la tarjeta"; // Ajustar según sea necesario
        String descripcion = "Descripción de la tarjeta"; // Ajustar según sea necesario
        float monto_original = 0; // Ajustar según sea necesario
        LocalDate fecha_inicio = LocalDate.now();

        CuentaBancaria cuenta_vinculada = null;
        for (CuentaBancaria cuenta : CuentaBancaria.intsancias_cuentas_bancarias) {
            String cuenta_buscada = cuenta.getNumeroCuenta() + " " + cuenta.getNombre();
            if (cuenta_buscada.equals(cuenta_bancaria_seleccionada)) {
                cuenta_vinculada = cuenta;
                break;
            }
        }
        if (cuenta_vinculada == null) {
            JOptionPane.showMessageDialog(this, "Cuenta bancaria seleccionada no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TarjetaCredito nuevaTarjeta = new TarjetaCredito(nombre, descripcion, monto_original, fecha_inicio, tipo_tarjeta, limite_credito, numero_tarjeta, cuenta_vinculada);
        //nuevaTarjeta.guardarTarjetaBaseDatos();
        cargarDatosTarjetas(); // Recargar los datos de la tabla después de agregar una nueva tarjeta
    }


    private void validarCampos() throws Exception {
        String tipo_tarjeta = (String) cb_tipo_tarjeta.getSelectedItem();
        String limite_credito = tf_limite_credito.getText().trim();
        String saldo_actual = tf_saldo_actual.getText().trim();
        String numero_tarjeta = tf_numero_tarjeta.getText().trim();
        String cuenta_bancaria = (String) cb_cuenta_bancaria.getSelectedItem();

        if (tipo_tarjeta == null || tipo_tarjeta.isEmpty() || tipo_tarjeta.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar un tipo de tarjeta.");
        }

        if (limite_credito.isEmpty()) {
            throw new Exception("Debe ingresar el límite de crédito.");
        }
        if (!limite_credito.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("El límite de crédito solo debe contener números y con hasta dos decimales.");
        }
        double limiteCredito = Double.parseDouble(limite_credito);
        if (limiteCredito < 0) {
            throw new Exception("El límite de crédito no puede ser menor a 0.");
        }

        if (saldo_actual.isEmpty()) {
            throw new Exception("Debe ingresar el saldo actual.");
        }
        if (!saldo_actual.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("El saldo actual solo debe contener números y con hasta dos decimales.");
        }
        double saldoActual = Double.parseDouble(saldo_actual);
        if (saldoActual < 0) {
            throw new Exception("El saldo actual no puede ser menor a 0.");
        }

        if (numero_tarjeta.isEmpty()) {
            throw new Exception("Debe ingresar los 4 números de la terminación del número de tarjeta.");
        }
        if (!numero_tarjeta.matches("\\d+")) {
            throw new Exception("El número de tarjeta debe contener solo números.");
        }
        if (numero_tarjeta.length() != 4 || !numero_tarjeta.matches("\\d{4}")) {
            throw new Exception("La terminación del número de tarjeta debe tener exactamente 4 dígitos.");
        }

        if (cuenta_bancaria == null || cuenta_bancaria.isEmpty() || cuenta_bancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tarjetas frame = new Tarjetas();
            frame.setVisible(true);
        });
    }
}
