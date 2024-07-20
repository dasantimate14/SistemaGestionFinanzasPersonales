package Grafico;

import sistemagestionfinanzas.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InicioSesion extends JFrame {
    BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
    private String correo;
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
        // Configuración de la ventana
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
                    //Se crean los objetos del usuario una vez se haya validado
                    Usuario.cargarClientesDesdeBaseDeDatos();
                    Usuario usuario = null;
                    for (Usuario usuarios : Usuario.instancias_clientes) {
                        if (usuarios.getEmail().equals(correo)) {
                            usuario = usuarios;
                            break; // Salir del bucle una vez encontrado el usuario
                        }
                    }
                    Usuario.setUsuarioActual(usuario);
                    if (usuario != null) {
                        obtenerDatosBaseDatos();
                        Dashboard newframe = new Dashboard();
                        newframe.setVisible(true);
                        dispose();
                    }
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

    private void obtenerDatosBaseDatos() {
        Usuario usuario_actual = Usuario.getUsuarioActual();
        String usuario_id = usuario_actual.getId();
        Gasto.obtenerGastoBaseDatos(usuario_id);
        Ingreso.obtenerIngresosBaseDatos(usuario_id);
        PlazoFijo.obtenerPlazoFijosBaseDatos(usuario_id);
        Prestamo.obtenerPrestamosBaseDatos(usuario_id);
        Stock.obtenerStocksBaseDatos(usuario_id);
        TarjetaCredito.obtenerTarjetaCreditoBaseDatos(usuario_id);
        try{
            for (Gasto gasto : Gasto.instancias_gastos) {
                gasto.actualizarInformacion();
                usuario_actual.agregarFinanceItem(gasto);
            }
            for (Ingreso ingreso : Ingreso.instancias_ingresos) {
                ingreso.actualizarInformacion();
                usuario_actual.agregarFinanceItem(ingreso);
            }

            for (PlazoFijo plazo_fijo : PlazoFijo.instancias_plazos_fijos) {
                plazo_fijo.actualizarInformacion();
                usuario_actual.agregarFinanceItem(plazo_fijo);
            }

            for (Prestamo prestamo : Prestamo.instanciasPrestamos) {
                prestamo.actualizarInformacion();
                usuario_actual.agregarFinanceItem(prestamo);
            }

            for (Stock stock : Stock.instancias_stocks) {
                stock.actualizarInformacion();
                usuario_actual.agregarFinanceItem(stock);
            }

            for (TarjetaCredito tarjeta_credito : TarjetaCredito.instanciasTarjetas) {
                tarjeta_credito.actualizarInformacion();
                usuario_actual.agregarFinanceItem(tarjeta_credito);
            }

            for (CuentaBancaria cuenta_bancaria : CuentaBancaria.intsancias_cuentas_bancarias) {
                cuenta_bancaria.actualizarInformacion();
                usuario_actual.agregarFinanceItem(cuenta_bancaria);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    private boolean guardarDatos() {
        try {
            correo = this.tfCorreo.getText();
            String password = new String(this.pfContrasena.getPassword());

            if (correo.isEmpty() || correo == null) {
                throw new Exception("Debe ingresar el correo.");
            }

            // Validación del formato del correo
            if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                throw new Exception("El correo ingresado no es válido.");
            }

            if (password.isEmpty() || password == null) {
                throw new Exception("Debe ingresar la contraseña.");
            }

            if (!Usuario.auntentificacionIniciarSesion(correo, password)) {
                throw new Exception("Debe ingresar el usuario y la contraseña correcta.");
            }

            System.out.println("Correo: " + correo);
            System.out.println("Contraseña: " + password);

            JOptionPane.showMessageDialog(this, "Credenciales correctas. Iniciando Sesión", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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