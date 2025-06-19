
import javax.swing.*;
import java.awt.*;
import GUI.ButtonsStyle; // make sure this file exists in GUI package

public class MotorPHWindow extends JFrame {

    public MotorPHWindow() {
        setTitle("MotorPH Payroll System");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel with light gray background
        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 230, 230)); // Light gray
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

        // Add MotorPH styled buttons
        panel.add(ButtonsStyle.ButtonStyle2("Save"));
        panel.add(ButtonsStyle.ButtonStyle2("View"));
        panel.add(ButtonsStyle.ButtonStyle2("Delete"));

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MotorPHWindow().setVisible(true));
    }
}
