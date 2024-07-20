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
                PlazoFijo.obtenerPlazoFijosBaseDatos(usuario.getId());
                for (PlazoFijo plazo_fijo : PlazoFijo.instancias_plazos_fijos) {
                    try {
                        plazo_fijo.actualizarInformacion();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    usuario.agregarFinanceItem(plazo_fijo);
                }

                newframe.setVisible(true);
                dispose();
            }
        });
        btnIngresos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresoYGastos newframe = new IngresoYGastos();
                Ingreso.obtenerIngresosBaseDatos(usuario.getId());
                for (Ingreso ingreso : Ingreso.instancias_ingresos) {
                    try {
                        ingreso.actualizarInformacion();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    usuario.agregarFinanceItem(ingreso);
                }
                Gasto.obtenerGastoBaseDatos(usuario.getId());
                for (Gasto gasto : Gasto.instancias_gastos) {
                    gasto.actualizarInformacion();
                    usuario.agregarFinanceItem(gasto);
                }

                newframe.setVisible(true);
                dispose();
            }
        });
        btnStocks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stocks newframe = new Stocks();
                Stock.obtenerStocksBaseDatos(usuario.getId());
                for (Stock stock : Stock.instancias_stocks) {
                    try {
                        stock.actualizarInformacion();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    usuario.agregarFinanceItem(stock);
                }
                newframe.setVisible(true);
                dispose();
            }
        });
        btnTarjetas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tarjetas newframe = new Tarjetas();
                TarjetaCredito.obtenerTarjetaCreditoBaseDatos(usuario.getId());
                for (TarjetaCredito tarjeta_credito : TarjetaCredito.instanciasTarjetas) {
                    try {
                        tarjeta_credito.actualizarInformacion();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    usuario.agregarFinanceItem(tarjeta_credito);
                }

                newframe.setVisible(true);
                dispose();
            }
        });
        prestamosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prestamos newframe = new Prestamos();
                Prestamo.obtenerPrestamosBaseDatos(usuario.getId());
                for (Prestamo prestamo : Prestamo.instanciasPrestamos) {
                    try {
                        prestamo.actualizarInformacion();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    usuario.agregarFinanceItem(prestamo);
                }
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
