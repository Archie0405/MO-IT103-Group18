package GUI;

import static GUI.ViewEmployee.EMPLOYEE_DETAILS_CSV;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NewEmployee {

    public static void displayNewEmployeeForm(JFrame parentFrame, DefaultTableModel model) {
        JFrame formFrame = new JFrame("Add New Employee");
        formFrame.setSize(500, 700);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {
            "Employee Number:", "Last Name:", "First Name:",
            "SSS Number:", "PhilHealth Number:", "TIN Number:", "Pag-IBIG Number:"
        };

        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            textFields[i] = new JTextField(20);

            if (i == 0) {
                textFields[i].setText(generateNewEmployeeNumber());
                textFields[i].setEditable(false); // Auto-generated employee number
            }

            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(textFields[i], gbc);
        }

        JButton submitButton = ButtonsStyle.ButtonStyle2("Submit");
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String[] newEmployee = new String[labels.length];
            for (int i = 0; i < labels.length; i++) {
                newEmployee[i] = removeQuotes(textFields[i].getText().trim());
            }

            System.out.println("Appending Employee Data: " + String.join(",", newEmployee));
            appendNewEmployeeToCSV(newEmployee);

            model.addRow(new Object[]{newEmployee[0], newEmployee[1], newEmployee[2], newEmployee[3], 
                                      newEmployee[4], newEmployee[5], newEmployee[6]});

            formFrame.dispose();
        });

        formFrame.add(panel);
        formFrame.setVisible(true);
    }

    public static String generateNewEmployeeNumber() {
        int maxEmpNum = 10000; // default starting number
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 0) {
                    try {
                        int empNum = Integer.parseInt(fields[0].replaceAll("[^0-9]", ""));
                        if (empNum > maxEmpNum) {
                            maxEmpNum = empNum;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(maxEmpNum + 1);
    }

    private static void appendNewEmployeeToCSV(String[] employeeData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DETAILS_CSV, true))) {
            for (int i = 0; i < employeeData.length; i++) {
                employeeData[i] = removeQuotes(employeeData[i]);
            }
            bw.write(String.join(",", employeeData));
            bw.newLine();
            bw.flush();
            JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving new employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String removeQuotes(String input) {
        input = input.replaceAll("^\"|\"$", "");
        if (input.contains(",") || input.contains("\"") || input.contains("'")) {
            input = "\"" + input.replace("\"", "\"\"") + "\"";
        }
        return input.trim();
    }
}
