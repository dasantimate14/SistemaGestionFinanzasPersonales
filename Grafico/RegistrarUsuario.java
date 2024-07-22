package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import sistemagestionfinanzas.Usuario;
import sistemagestionfinanzas.BaseDeDatos;

public class RegistrarUsuario extends JFrame {

    public static ArrayList<Usuario> instancias_clientes = new ArrayList<>();
    public static Usuario usuario_actual;

    private JPanel registrar_panel;
    private JTextField tf_nombre;
    private JTextField tf_apellido;
    private JTextField tf_correo;
    private JPasswordField tf_contrasena;
    private JButton btn_enviar;
    private JButton btn_login;


    public static String getIdUsuarioActual() {
        return String.valueOf(usuario_actual != null ? usuario_actual.getId() : null);
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuario_actual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuario_actual;
    }

    public RegistrarUsuario() {
        setContentPane(registrar_panel);
        setTitle("Registrar Usuario");
        setSize(930, 920);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Acción para abrir "Iniciar sesión" al presionar si ya se tiene una cuenta
        btn_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                RegistrarUsuario.this.dispose();
            }
        });

        // Acción para registrar un nuevo usuario
        btn_enviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String nombre = tf_nombre.getText();
                String apellido = tf_apellido.getText();
                String correo = tf_correo.getText();
                String contrasena = new String(tf_contrasena.getPassword());

                if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                    return;
                }

                try {

                    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                        throw new Exception("El nombre solo puede contener letras.");
                    }


                    if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                        throw new Exception("El apellido solo puede contener letras.");
                    }


                    if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                        throw new Exception("El correo solo puede contener puntos, arrobas, letras y números.");
                    }


                    BaseDeDatos.establecerConexion();

                    if (Usuario.correoExistente(correo)) {
                        JOptionPane.showMessageDialog(null, "El correo ya está registrado.","Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                } finally {
                    BaseDeDatos.cerrarConexion();
                }

                try {
                    Usuario usuario = new Usuario(nombre + " " + apellido, correo, contrasena);
                    usuario.guardarClienteEnBaseDatos();
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
                    setUsuarioActual(usuario);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al registrar el usuario.");
                } finally {
                    BaseDeDatos.cerrarConexion();
                }
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                RegistrarUsuario.this.dispose();
            }
        });
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