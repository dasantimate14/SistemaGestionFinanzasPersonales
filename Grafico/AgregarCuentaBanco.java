package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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


    private static List<Cuenta> cuentas = new ArrayList<>();

    public AgregarCuentaBanco() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Agregar Cuentas de Banco");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(AgregarCuentaBancoPanel);

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
                    validarDatos();
                    String nombre = tfNombreCuenta.getText();
                    String numero = tfNumeroCuenta.getText();

                    Cuenta cuenta = new Cuenta(nombre, numero);
                    agregarCuenta(cuenta);
                    JOptionPane.showMessageDialog(AgregarCuentaBanco.this, "Cuenta agregada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tfNombreCuenta.setText("");
                    tfNumeroCuenta.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AgregarCuentaBanco.this, "Por favor, ingrese datos válidos. " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public static List<Cuenta> getCuentas() {
        return cuentas;
    }

    public static void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
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

    public static class Cuenta {
        private String nombre;
        private String numero;

        public Cuenta(String nombre, String numero) {
            this.nombre = nombre;
            this.numero = numero;
        }

        @Override
        public String toString() {
            return nombre + " (" + numero + ")";
        }

        public String getNombre() {
            return nombre;
        }

        public String getNumero() {
            return numero;
        }
    }
}