package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrarUsuario extends JFrame {
    private JPanel RegistrarPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPasswordField passwordField1;
    private JButton BtnEnviar;
    private JButton BtnLogin;

    public RegistrarUsuario() {
        setContentPane(RegistrarPanel);
        setTitle("Registrar Usuario");
        setSize(930, 920);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //Acción para que se abra "iniciar sesión" al presionar
        BtnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                dispose();
            }
        });
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RegistrarUsuario frame = new RegistrarUsuario();
                frame.setVisible(true);
            }
        });

    }
}
