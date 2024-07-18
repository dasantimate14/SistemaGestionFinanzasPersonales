package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class  AgregarCuentaBanco extends JFrame {
    private JPanel AgregarCuentaBancoPanel;
    private JComboBox<String> comboBox1;
    private JTextField tfNombreCuenta;
    private JTextField tfNumeroCuenta;
    private JTextField tfSaldoInicial;
    private JComboBox<String> comboBox2;
    private JButton crearButton;
    private JButton Volverbtn2;


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
                CuentaBancaria newframe = new CuentaBancaria();
                newframe.setVisible(true);
                dispose();
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
