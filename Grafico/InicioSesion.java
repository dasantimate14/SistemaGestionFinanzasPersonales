package Grafico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class InicioSesion extends JFrame {
    BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
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
                if (guardarDatos()) {
                    Dashboard newframe = new Dashboard();
                    newframe.setVisible(true);
                    dispose();
                }
            }
        });

        // Acción para que se abra "Registrar Usuarios" al tocar el botón registrarse
        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarUsuario newframe = new RegistrarUsuario();
                newframe.setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

    private boolean guardarDatos() {
        try {
            String correo = this.tfCorreo.getText();
            String password = new String(this.pfContrasena.getPassword());

            if (correo.isEmpty() || correo == null) {
                throw new Exception("Debe ingresar el correo.");
            }

            // Validación del formato del correo
            if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                throw new Exception("El correo solo puede contener puntos, arrobas, letras y números.");
            }

            if (password.isEmpty() || password == null) {
                throw new Exception("Debe ingresar la contraseña.");
            }

            System.out.println("Correo: " + correo);
            System.out.println("Contraseña: " + password);

            JOptionPane.showMessageDialog(this, "Información guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.tfCorreo.setText("");
            this.pfContrasena.setText("");
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese datos válidos. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese datos válidos. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
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