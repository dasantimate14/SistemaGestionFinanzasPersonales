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
        BtnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                dispose();
            }
        });

        // Acción para que se redirija a la interfaz de inicio de sesión
        // Se debe almacenar los datos ingresados en la base de datos aquí
        BtnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarUsuario();
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                dispose();
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

    private void guardarUsuario() {
        String nombre = this.tfNombre.getText();
        String apellido = this.tfApellido.getText();
        String correo = this.tfCorreo.getText();
        String cedula = this.tfCedula.getText();
        String contrasena = new String(this.tfContrasena.getPassword());

        // Aquí deberías agregar la lógica para guardar los datos en la base de datos.


        JOptionPane.showMessageDialog(this, "Información guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        // Limpiar los campos
        this.tfNombre.setText("");
        this.tfApellido.setText("");
        this.tfCedula.setText("");
        this.tfContrasena.setText("");
        this.tfCorreo.setText("");
    }
}
