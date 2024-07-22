package Grafico;

import sistemagestionfinanzas.CuentaBancaria;
import sistemagestionfinanzas.PlazoFijo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CuentaBancariaG extends JFrame {
    private JPanel CuentaBancoPanel;
    private JButton btn_menu2;
    private JButton prestamosButton;
    private JButton tarjetasDeCreditoButton;
    private JButton stocksButton;
    private JButton ingresosYGastosButton;
    private JButton plazos_fijos_button;
    private JButton cuentas_bancarias_button;
    private JButton btnAgregarCuenta;
    private JButton consultarMovimientosButton;
    private JPanel pastelitoPanel;
    private JTable table_cuentas_banco;
    private JScrollPane spCuentaBanco;
    private DefaultTableModel tableModel;

    public CuentaBancariaG() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Cuentas de Banco");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(CuentaBancoPanel);

        // Configurar el modelo de la tabla
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Descripción");
        tableModel.addColumn("Tipo");
        tableModel.addColumn("Número de Cuenta");
        tableModel.addColumn("Banco de origen");
        tableModel.addColumn("Saldo inicial");
        tableModel.addColumn("Tasa de interés");
        tableModel.addColumn("Fecha de inicio");
        tableModel.addColumn("Promedio mensual");
        tableModel.addColumn("Promedio anual");
        tableModel.addColumn("Balance actual");
        tableModel.addColumn("Interés acumulado");

        table_cuentas_banco.setModel(tableModel);

        btnAgregarCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarCuentaBanco newframe = new AgregarCuentaBanco(CuentaBancariaG.this);
                newframe.setVisible(true);
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
        btn_menu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard newframe = new Dashboard();
                newframe.setVisible(true);
                dispose();
            }
        });
        plazos_fijos_button.addActionListener(new ActionListener() {
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



    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CuentaBancariaG frame = new CuentaBancariaG();
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