package Grafico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioSesion extends JFrame {
    private JPanel LoginPanel;
    private JTextField tfCorreo;
    private JPasswordField pfContrasena;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JLabel lbBienvenido;
    private JLabel lbIniciarSesion;
    private JLabel lbCorreo;
    private JLabel lbContrasena;

    public InicioSesion() {
        // Configuración de la ventana de inicio de sesión
        setSize(930, 920);
        setLocation(0, 0);
        setTitle("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(LoginPanel); // Asignamos el panel principal

        // Acción para el botón "Iniciar Sesión"
        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Aquí se agrega el método para que se entrelace al dashboard (página principal)
            }
        });

        // Acción para el botón "Registrarse"
        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarUsuario newframe = new RegistrarUsuario();
                newframe.setVisible(true);
                dispose(); // Cierra la ventana actual de Inicio de Sesión
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InicioSesion frame = new InicioSesion();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

