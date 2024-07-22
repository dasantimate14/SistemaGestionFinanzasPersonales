package Grafico;

import sistemagestionfinanzas.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static sistemagestionfinanzas.Usuario.usuario_actual;

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
    private String correo;

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
                    System.out.println("correo: " + correo);
                    try {
                        Usuario.cargarUsuariosDesdeBaseDeDatos(correo);
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    String id_usuario = usuario_actual.getId();
                    obtenerDatosBaseDatos(id_usuario);
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

    private void obtenerDatosBaseDatos(String id_usuario) {
        //Se obtienen las cuentas bancarias primero porque de ellas dependen varios de los activos y pasivos dentro del sistema
        try {
            CuentaBancaria.obtenerCuentasBancariasBaseDatos(id_usuario);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        //Se obtiene y actualiza la información de cada uno de los activos y pasivos
        try {
            Gasto.obtenerGastoBaseDatos(id_usuario);
            List<Gasto> gastos_actuales = new ArrayList<>(Gasto.instancias_gastos);
            if(!Gasto.instancias_gastos.isEmpty()){
                for(Gasto gasto: gastos_actuales){
                    gasto.actualizarInformacion();
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        try {
            Ingreso.obtenerIngresosBaseDatos(id_usuario);
            List<Ingreso> ingresos_actuales = new ArrayList<>(Ingreso.instancias_ingresos);
            if(!Ingreso.instancias_ingresos.isEmpty()){
                for(Ingreso ingreso: ingresos_actuales){
                    ingreso.actualizarInformacion();
                }
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            PlazoFijo.obtenerPlazoFijosBaseDatos(id_usuario);
            List<PlazoFijo> plazos_fijos_actuales = new ArrayList<>(PlazoFijo.instancias_plazos_fijos);
            if(!plazos_fijos_actuales.isEmpty()){
                for(PlazoFijo plazo_fijo: plazos_fijos_actuales){
                    plazo_fijo.actualizarInformacion();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try{
            Prestamo.obtenerPrestamosBaseDatos(id_usuario);
            List<Prestamo> prestamos_actuales = new ArrayList<>(Prestamo.instancias_prestamos);
            if(!prestamos_actuales.isEmpty()){
                for (Prestamo prestamo : prestamos_actuales) {
                    prestamo.actualizarInformacion();
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try{
            Stock.obtenerStocksBaseDatos(id_usuario);
            List<Stock> stocks_actuales = new ArrayList<>(Stock.instancias_stocks);
            if(!stocks_actuales.isEmpty()){
                for (Stock stock : stocks_actuales) {
                    stock.actualizarInformacion();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            TarjetaCredito.obtenerTarjetaCreditoBaseDatos(id_usuario);
            List<TarjetaCredito> tarjetas_actuales = new ArrayList<>(TarjetaCredito.instanciasTarjetas);
            if(!tarjetas_actuales.isEmpty()){
                for(TarjetaCredito tarjeta_credito : tarjetas_actuales){
                    tarjeta_credito.actualizarInformacion();
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        //Se actualiza la información de la cuenta bancaria al final ya que este proceso incluye el calculo de interes a partir de balances mensuales
        try{
            List<CuentaBancaria> cuentas_bancarias_actuales = new ArrayList<>(CuentaBancaria.intsancias_cuentas_bancarias);
            if(!cuentas_bancarias_actuales.isEmpty()){
                for(CuentaBancaria cuenta_bancaria: cuentas_bancarias_actuales){
                    cuenta_bancaria.actualizarInformacion();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Se agregan cada uno de los activos y pasivos actualizados a los arreglos del usuario
        for(CuentaBancaria cuenta_bancaria : CuentaBancaria.intsancias_cuentas_bancarias){
            usuario_actual.agregarFinanceItem(cuenta_bancaria);
        }
        for(PlazoFijo plazo_fijo: PlazoFijo.instancias_plazos_fijos){
            usuario_actual.agregarFinanceItem(plazo_fijo);
        }
        for(Stock stock : Stock.instancias_stocks){
            usuario_actual.agregarFinanceItem(stock);
        }
        for(Prestamo prestamo : Prestamo.instancias_prestamos){
            usuario_actual.agregarFinanceItem(prestamo);
        }
        for(TarjetaCredito tarjeta_credito: TarjetaCredito.instanciasTarjetas){
            usuario_actual.agregarFinanceItem(tarjeta_credito);
        }
        JOptionPane.showMessageDialog(this, "Información Obtenida Correctamente de la Base de Datos", "Éxito", JOptionPane.INFORMATION_MESSAGE);

    }

    private boolean guardarDatos() {
        try {
            correo = tfCorreo.getText();
            String password = new String(pfContrasena.getPassword());

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