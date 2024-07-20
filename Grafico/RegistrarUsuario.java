package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import sistemagestionfinanzas.Usuario;
import sistemagestionfinanzas.BaseDeDatos;

public class RegistrarUsuario extends JFrame {
    private JPanel RegistrarPanel;
    private JTextField tfNombre;
    private JTextField tfApellido;
    private JTextField tfCorreo;
    private JPasswordField tfContrasena;
    private JButton BtnEnviar;
    private JButton BtnLogin;

    public RegistrarUsuario() {
        setContentPane(RegistrarPanel);
        setTitle("Registrar Usuario");
        setSize(930, 920);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Acción para abrir "Iniciar sesión" al presionar si ya se tiene una cuenta
        BtnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                RegistrarUsuario.this.dispose();
            }
        });

        // Acción para registrar un nuevo usuario
        BtnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String nombre = tfNombre.getText();
                String apellido = tfApellido.getText();
                String correo = tfCorreo.getText();
                String contrasena = new String(tfContrasena.getPassword());

                if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                    return;
                }

                // Validar si el correo ya existe
                try {
                    BaseDeDatos.establecerConexion();

                    if (Usuario.correoExistente(correo)) {
                        JOptionPane.showMessageDialog(null, "El correo ya está registrado.");
                        return;
                    }
                } catch (SQLException ex) { // Cambio de nombre de la variable aquí
                    ex.printStackTrace();
                } finally {
                    BaseDeDatos.cerrarConexion();
                }

                try {
                    Usuario usuario = new Usuario(nombre + " " + apellido, correo, contrasena);
                    usuario.guardarClienteEnBaseDatos();
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al registrar el usuario.");
                } finally {
                    BaseDeDatos.cerrarConexion();
                }
            }
        });
        System.out.println();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegistrarUsuario frame = new RegistrarUsuario();
                frame.setVisible(true);
            }
        });
    }
}