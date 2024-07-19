package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlazoFijos extends JFrame{
    private JPanel PlazoFijosPanel;
    private JButton menuPrincipalButton;
    private JButton cuentasBancariasButton;
    private JButton plazoFijosButton;
    private JButton ingresosYGastosButton;
    private JButton stocksButton;
    private JButton tarjetasDeCreditosButton;
    private JButton prestamosButton;
    private JButton gastosButton;
    private JButton proyeccionesButton;
    private JButton agregarPlazoFijoButton;

    public PlazoFijos() {
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Plazo Fijos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(PlazoFijosPanel);

        menuPrincipalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción para el botón del menú principal
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                dispose();
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PlazoFijos frame = new PlazoFijos();
                frame.setVisible(true);
            }
        });
    }
}