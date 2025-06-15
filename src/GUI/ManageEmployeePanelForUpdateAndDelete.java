package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ManageEmployeePanelForUpdateAndDelete {

    // This will open a new panel to manage employee records (update and delete)
    public static void displayManageEmployeesPanel(JTable employeeTable, DefaultTableModel model) {

        // Ensure the user selects an employee before managing the data
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee first!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String employeeNumber = employeeTable.getValueAt(selectedRow, 0).toString();

        // Retrieve full employee details from the CSV instead of JTable data
        String[] employeeDetails = GetEmployeeDetails.getEmployeeDetails(employeeNumber);

        if (employeeDetails == null) {
            JOptionPane.showMessageDialog(null, "Error: Employee record not found in CSV!", "Data Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new frame for managing employee data
        JFrame manageFrame = new JFrame("Manage Employee");
        manageFrame.setSize(600, 700);
        manageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        manageFrame.setLocationRelativeTo(null);
        
        //Create the main panel using GridBagLayout to make it flexible
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        
        //We make it scrollable because the textfields or employee details is too many
        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scroll

        // Define labels and corresponding text fields
        String[] labels = {
            "Employee Number", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
            "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number", "Status",
            "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
            "Clothing Allowance", "Gross Semi-Monthly Rate", "Hourly Rate"
        };
        
        //Here we create an array of textfields that equals to the number of labels
        JTextField[] textFields = new JTextField[labels.length];
        
        //It's a for loop that go through each label that creates and position the label field pairs
        for (int i = 0; i < labels.length; i++) {
            textFields[i] = new JTextField(19);
            textFields[i].setText(employeeDetails[i]);
            //We position the labels in the left side
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.EAST;
            detailsPanel.add(new JLabel(labels[i]), gbc);
            //Then the textfields in the right side
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            detailsPanel.add(textFields[i], gbc);
        }

        // Define update and delete buttons
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        //We set the position of these buttons under the form fields
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        detailsPanel.add(updateButton, gbc);
        //It positions the delete button on the next row after the update button
        gbc.gridy = labels.length + 1;
        detailsPanel.add(deleteButton, gbc);
        //Then add the scrollable panel to the main frame and display the em,ployee window
        manageFrame.add(scrollPane, BorderLayout.CENTER);
        manageFrame.setVisible(true);

        // Update action listener
        updateButton.addActionListener(e -> {

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(manageFrame, "Please select an employee first!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //It collects updated field values from textFields
            String[] updatedEmployee = new String[textFields.length];
            for (int i = 0; i < textFields.length; i++) {
                updatedEmployee[i] = textFields[i].getText().trim();
            }

            //Update the CSV using values from updatedEmployee
            UpdateEmployee.updateEmployeeInCSV(updatedEmployee, model);

            //Update the JTable visually based on changes, this is a specif method that we did to fix rewriting a wrong index in the csv file
            EmployeeTable.updateJTable(employeeTable, textFields);

            // Fully reload the model to ensure consistency
            RefreshTable.refreshEmployeeTable2(model);

            JOptionPane.showMessageDialog(manageFrame, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            manageFrame.dispose(); // Close window
        });

        // Delete action listener, it refreshes the table as well after the deletion
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(manageFrame, "Are you sure you want to delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DeleteEmployee.deleteEmployee(employeeNumber, model);
                RefreshTable.refreshEmployeeTable2(model);
                JOptionPane.showMessageDialog(manageFrame, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                manageFrame.dispose();
            }
        });
    }
}
