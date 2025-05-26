/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Nichie
 */

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminAccess extends JFrame { 
    private String adminUser;

    public AdminAccess(String username) { 
        this.adminUser = username;

        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel welcomeLabel = new JLabel("Welcome, Admin " + username);
        JButton manageUsersButton = new JButton("Manage Employees");
        JButton payrollConfigButton = new JButton("Payroll Settings");
        JButton logOutButton = new JButton("Log Out");

        logOutButton.addActionListener(e -> logOut());

        panel.add(welcomeLabel);
        panel.add(manageUsersButton);
        panel.add(payrollConfigButton);
        panel.add(logOutButton);

        add(panel);
    }

    private void logOut() {
        dispose(); // Close the admin dashboard
        SwingUtilities.invokeLater(AdminAccess::showLoginScreen); // Return to login screen
    }

    public static void showLoginScreen() { 
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setSize(400, 250);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());

            if (authenticateAdmin(username, password)) {  
                loginFrame.dispose();
                SwingUtilities.invokeLater(() -> new AdminAccess(username).setVisible(true)); // Load admin page
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials, please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passLabel);
        panel.add(passText);
        panel.add(new JLabel());
        panel.add(loginButton);

        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }

    private static boolean authenticateAdmin(String username, String password) {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Admin.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] credentials = parseCSVLine(line);

                if (credentials.length >= 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; 
    }

    private static String[] parseCSVLine(String line) {
        return line.split(",");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminAccess::showLoginScreen);
    }
}
