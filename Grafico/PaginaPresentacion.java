package Grafico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaPresentacion extends JFrame {

    private JPanel pagina_inicio_panel;
    private JButton btn_iniciar;

    public PaginaPresentacion() {
        setContentPane(pagina_inicio_panel);
        setTitle("Página de PResentación");
        setSize(930, 920);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        btn_iniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InicioSesion newframe = new InicioSesion();
                newframe.setVisible(true);
                dispose();
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PaginaPresentacion frame = new PaginaPresentacion();
                frame.setVisible(true);
            }
        });
    }
}
