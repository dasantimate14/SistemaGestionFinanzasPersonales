package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CuentaBancaria extends JFrame {
    private JPanel CuentaBancoPanel;
    private JButton btnMenu2;
    private JButton prestamosButton;
    private JButton tarjetasDeCreditoButton;
    private JButton stocksButton;
    private JButton ingresosYGastosButton;
    private JButton plazosFijosButton;
    private JButton cuentasBancariasButton;
    private JButton btnAgregarCuenta;
    private JButton consultarMovimientosButton;
    private JList list1;
    private JPanel PastelitoPanel;

    public CuentaBancaria() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Cuentas de Banco");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(CuentaBancoPanel);

        btnAgregarCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarCuentaBanco newframe = new AgregarCuentaBanco();
                newframe.setVisible(true);
                dispose();
            }
        });
        consultarMovimientosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    ConsultarMovimientos newframe = new ConsultarMovimientos();
                    newframe.setVisible(true);
                    dispose();
            }
        });
        //Action Listeners para los botones del dashboard
        btnMenu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard newframe = new Dashboard();
                newframe.setVisible(true);
                dispose();
            }
        });
        plazosFijosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlazoFijos newframe = new PlazoFijos();
                newframe.setVisible(true);
                dispose();
            }
        });
        ingresosYGastosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
                newframe.setVisible(true);
                dispose();
            }
        });
        stocksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });
        tarjetasDeCreditoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tarjetas newframe = new Tarjetas();
                newframe.setVisible(true);
                dispose();
            }
        });
        prestamosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prestamos newframe = new Prestamos();
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
                CuentaBancaria frame = new CuentaBancaria();
                frame.setVisible(true);
            }
        });
    }
    private void validarDatosAgregarCuenta() throws Exception {
        String nombreCuenta = "nombreCuentaEjemplo";
        String numeroCuenta = "numeroCuentaEjemplo";

        if (nombreCuenta.isEmpty() || nombreCuenta == null) {
            throw new Exception("Debe ingresar el nombre de la cuenta.");
        }


        if (numeroCuenta.isEmpty() || numeroCuenta == null) {
            throw new Exception("Debe ingresar el número de la cuenta.");
        }
        if (!numeroCuenta.matches("[0-9-]+")) {
            throw new Exception("El número de cuenta solo puede contener números y guiones.");
        }
        if (numeroCuenta.length() > 20) {
            throw new Exception("El número de cuenta no puede tener más de 20 caracteres.");
        }
    }

    private void validarDatosConsultarMovimientos() throws Exception {

        String numeroCuenta = "numeroCuentaEjemplo";
        if (numeroCuenta.isEmpty() || numeroCuenta == null) {
            throw new Exception("Debe ingresar el número de la cuenta.");
        }
        if (!numeroCuenta.matches("[0-9-]+")) {
            throw new Exception("El número de cuenta solo puede contener números y guiones.");
        }
        if (numeroCuenta.length() > 20) {
            throw new Exception("El número de cuenta no puede tener más de 20 caracteres.");
        }
    }

}