/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tacp1;

/**
 *
 * @author USER
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayrollSystemGUI extends JFrame {

    public PayrollSystemGUI() {
        setTitle("Payroll System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Title label
        JLabel titleLabel = new JLabel("Payroll System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(Color.BLACK);

        JButton profileButton = createStyledButton("View Profile");
        JButton attendanceButton = createStyledButton("View Attendance");
        JButton payrollButton = createStyledButton("View Payroll");

        buttonPanel.add(profileButton);
        buttonPanel.add(attendanceButton);
        buttonPanel.add(payrollButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Event listeners
        profileButton.addActionListener(e -> showMessage("Profile Section"));
        attendanceButton.addActionListener(e -> showMessage("Attendance Section"));
        payrollButton.addActionListener(e -> showMessage("Payroll Section"));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        return button;
    }

    private void showMessage(String section) {
        JOptionPane.showMessageDialog(this, "Navigating to " + section, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollSystemGUI gui = new PayrollSystemGUI();
            gui.setVisible(true);
        });
    }
}
