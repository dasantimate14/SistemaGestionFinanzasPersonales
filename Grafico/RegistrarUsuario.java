package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrarUsuario extends JFrame {
    private JPanel RegistrarPanel;
    private JTextField tfNombre;
    private JTextField tfApellido;
    private JTextField tfCorreo;
    private JTextField tfCedula;
    private JPasswordField tfContrasena;
    private JButton BtnEnviar;
    private JButton BtnLogin;

    public RegistrarUsuario() {
        setContentPane(RegistrarPanel);
        setTitle("Registrar Usuario");
        setSize(930, 920);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Acción para que se abra "iniciar sesión" al presionar si ya se tiene una cuenta
        this.BtnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                RegistrarUsuario.this.dispose();
            }
        });

        // Acción para que se redirija a la interfaz de inicio de sesión
        // Se debe almacenar los datos ingresados en la base de datos aquí
        this.BtnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (RegistrarUsuario.this.guardarUsuario()) {
                    InicioSesion newframe = new InicioSesion();
                    newframe.setVisible(true);
                    RegistrarUsuario.this.dispose();
                }
            }
        });
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

    private boolean guardarUsuario() {
        try {
            String nombre = this.tfNombre.getText();
            String apellido = this.tfApellido.getText();
            String correo = this.tfCorreo.getText();
            String cedula = this.tfCedula.getText();
            String contrasena = new String(this.tfContrasena.getPassword());

            if (nombre.isEmpty() || nombre == null) {
                throw new Exception("Debe ingresar el nombre.");
            }
           if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
               throw new Exception("El nombre solo puede contener letras y espacios.");
           }



            if (apellido.isEmpty() || apellido == null) {
                throw new Exception("Debe ingresar el apellido.");
            }
            if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+")) {
                throw new Exception("El apellido solo puede contener letras.");
            }


            if (correo.isEmpty() || correo == null) {
                throw new Exception("Debe ingresar el correo.");
            }

            if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                throw new Exception("El correo solo puede contener puntos, arrobas, letras y números.");
            }

            if (cedula.isEmpty() || cedula == null) {
                throw new Exception("Debe ingresar la cédula.");
            }
            if (!cedula.matches("[0-9-]+")) {
                throw new Exception("La cédula solo puede contener números y guiones.");
            }


            if (contrasena.isEmpty() || contrasena == null) {
                throw new Exception("Debe ingresar la contraseña.");
            }

            JOptionPane.showMessageDialog(this, "Información guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.tfNombre.setText("");
            this.tfApellido.setText("");
            this.tfCedula.setText("");
            this.tfContrasena.setText("");
            this.tfCorreo.setText("");
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese datos válidos. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese datos válidos. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
