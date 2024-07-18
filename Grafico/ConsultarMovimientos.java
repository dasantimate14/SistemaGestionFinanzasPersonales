package Grafico;

import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class ConsultarMovimientos extends JFrame {
    private JPanel datePanelContainer;
    private JPanel MovPanel;
    private JComboBox cbID;
    private JComboBox cbNombreCuenta;
    private JList<String> list1;
    private JButton btnBuscar;
    private JDatePickerImpl datePicker;

    public ConsultarMovimientos() {
        // Configuración de la ventana
        setSize(960, 920);
        setTitle("Consultar Movimiento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(MovPanel);

        //Implementación del JDatePicker dentro del GUI
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanelImpl, new DateLabelFormatter());

        datePanelContainer.setLayout(new BorderLayout());
        datePanelContainer.add(datePicker, BorderLayout.CENTER);
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

    // Clase interna utilizada para formatear las fechas
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "dd/MM/yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
