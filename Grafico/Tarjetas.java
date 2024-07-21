package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tarjetas extends JFrame {
    private JButton btMenu;
    private JButton cuentasBancariasButton;
    private JButton préstamoButton;
    private JButton plazosFijosButton;
    private JButton tarjetasDeCreditoButton;
    private JButton stocksButton;
    private JButton ingresosYGastosButton;
    private JPanel TarjetaPanel;
    private JComboBox<String> comboBox1;
    private JTextField tfLimiteCredito;
    private JTextField tfSaldoActual;
    private JTextField tfNumeroTarjeta;
    private JComboBox<String> cbCuentaBancaria;
    private JButton agregarButton;

    public Tarjetas() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Tarjetas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(TarjetaPanel);

        cargarCuentasBancarias();

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validarCampos();
                    JOptionPane.showMessageDialog(null, "Tarjeta agregada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btMenu.addActionListener(new ActionListener() {
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
        préstamoButton.addActionListener(new ActionListener() {
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
        cbCuentaBancaria.removeAllItems();
        for (AgregarCuentaBanco.Cuenta cuenta : AgregarCuentaBanco.getCuentas()) {
            cbCuentaBancaria.addItem(cuenta.getNombre() + " (" + cuenta.getNumero() + ")");
        }
    }

    private void validarCampos() throws Exception {
        String tipoTarjeta = (String) comboBox1.getSelectedItem();
        String limiteCredito = tfLimiteCredito.getText().trim();
        String saldoActual = tfSaldoActual.getText().trim();
        String numeroTarjeta = tfNumeroTarjeta.getText().trim();
        String cuentaBancaria = (String) cbCuentaBancaria.getSelectedItem();

        if (tipoTarjeta == null || tipoTarjeta.isEmpty() || tipoTarjeta.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar un tipo de tarjeta.");
        }

        if (limiteCredito.isEmpty()) {
            throw new Exception("Debe ingresar el límite de crédito.");
        }
        if (!limiteCredito.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("El límite de crédito solo debe contener números con hasta dos decimales.");
        }

        try {
            Double.parseDouble(limiteCredito);
        } catch (NumberFormatException e) {
            throw new Exception("El límite de crédito debe ser un número válido.");
        }

        if (saldoActual.isEmpty()) {
            throw new Exception("Debe ingresar el saldo actual.");
        }
        if (!saldoActual.matches("\\d+(\\.\\d{1,2})?")) {
            throw new Exception("El saldo actual solo debe contener números con hasta dos decimales.");
        }

        try {
            Double.parseDouble(saldoActual);
        } catch (NumberFormatException e) {
            throw new Exception("El saldo actual debe ser un número válido.");
        }

        if (numeroTarjeta.isEmpty()) {
            throw new Exception("Debe ingresar el número de tarjeta.");
        }
        if (!numeroTarjeta.matches("\\d{1,22}")) {
            throw new Exception("El número de tarjeta solo puede contener números de hasta 22 dígitos.");
        }

        if (cuentaBancaria == null || cuentaBancaria.isEmpty() || cuentaBancaria.equals("Selecciona una opción")) {
            throw new Exception("Debe seleccionar una cuenta bancaria.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Tarjetas");
                frame.setContentPane(new Tarjetas().TarjetaPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
