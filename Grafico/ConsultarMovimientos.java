package Grafico;

import javax.swing.*;

public class ConsultarMovimientos extends JFrame {

    private JPanel ConsultaMovPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JList list1;
    private JTextField textField1;
    private JButton buscarButton;

    public ConsultarMovimientos(){
        // Configuración de la ventana
        setSize(930, 920);
        setTitle("Consiltar Movimientos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(ConsultaMovPanel);
    }
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConsultarMovimientos frame = new ConsultarMovimientos();
                frame.setVisible(true);
            }
        });
    }
}
