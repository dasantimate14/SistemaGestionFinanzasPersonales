package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgregarCuentaBanco extends JFrame {
    private JPanel AgregarCuentaBancoPanel;
    private JComboBox<String> comboBox1;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox<String> comboBox2;
    private JTextField textField1;
    private JButton crearButton;
    private JButton Volverbtn2;

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AgregarCuentaBanco frame = new AgregarCuentaBanco();
                frame.setVisible(true);
            }
        });
    }
}
