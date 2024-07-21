package Grafico;

import sistemagestionfinanzas.*;
import sistemagestionfinanzas.Stock;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Dashboard extends JFrame {
    private Usuario usuario;
    private JPanel DashboardPanel;
    private JButton btnMenu1;
    private JButton btnCuentaBancaria;
    private JButton plazosFijosButton;
    private JButton btnIngresos;
    private JButton btnStocks;
    private JButton btnTarjetas;
    private JButton prestamosButton;
    private JTextField tfNombreUsuario;  //Se utilizara para asignar el nombre del usuario en hola nombre

    public Dashboard() {
        this.usuario = Usuario.getUsuarioActual();
        // Configuración de la ventana de inicio de sesión
        setSize(930, 920);
        setTitle("Menu Principal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(DashboardPanel);

        btnCuentaBancaria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CuentaBancariaG newframe = new CuentaBancariaG();
                newframe.setVisible(true);
                dispose();
            }
        });

        // Añade ActionListeners
        plazosFijosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlazoFijos newframe = new PlazoFijos();
                newframe.setVisible(true);
                dispose();
            }
        });
        btnIngresos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
                newframe.setVisible(true);
                dispose();
            }
        });
        btnStocks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                newframe.setVisible(true);
                dispose();
            }
        });

        btnTarjetas.addActionListener(new ActionListener() {
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
                Dashboard frame = new Dashboard();
                frame.setVisible(true);
            }
        });
    }
}
