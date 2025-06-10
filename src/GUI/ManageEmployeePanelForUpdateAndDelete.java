/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class ManageEmployeePanelForUpdateAndDelete {
    //This will open a new panel to manage an employee records(update and delete)
    public static void displayManageEmployeesPanel(JTable employeeTable, DefaultTableModel model) {
        
        //This will make sure that the user selects an employee before managing the data
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee first!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //We create a new frame for managing an employee data base on the selected row in the table
        JFrame manageFrame = new JFrame("Manage Employee");
        manageFrame.setSize(400, 500);
        manageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        manageFrame.setLocationRelativeTo(null);
        
        //We use again the GridBagLayout to set up the panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); //We add padding for the spacing of the panel
        
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        
        //Now we define each labels and their corresponding text fields
        String[] labels = {"Employee Number", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"};
        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            textFields[i] = new JTextField(20);
            textFields[i].setText(employeeTable.getValueAt(selectedRow, i).toString());
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.EAST; //This will allign the labels to the right
            detailsPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST; //And aligns the text field to the left
            detailsPanel.add(textFields[i], gbc);
        }
        
        //We define the buttons for updating and deleting employes
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        detailsPanel.add(updateButton, gbc);

        gbc.gridy = labels.length + 1;
        detailsPanel.add(deleteButton, gbc);

        manageFrame.add(detailsPanel, BorderLayout.WEST);
        manageFrame.setVisible(true);

        //This will ensure that the update button modifies the correct employee record
        updateButton.addActionListener(e -> {
            String oldEmployeeNumber = employeeTable.getValueAt(selectedRow, 0).toString(); //Getting the old ID
            String[] updatedEmployee = new String[employeeTable.getColumnCount()];

            for (int i = 0; i < employeeTable.getColumnCount(); i++) {
                updatedEmployee[i] = textFields[i].getText().trim();//This will store the updated data
            }
            UpdateEmployee.updateEmployeeInCSV(updatedEmployee, oldEmployeeNumber, model); //This updates the csv records
            RefreshTable.refreshEmployeeTable(model); //Then refresh the table with the new data
            
            JOptionPane.showMessageDialog(manageFrame, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            manageFrame.dispose(); //Then close the management window after the update
        });

        //This will make sure that the delete button will remove the selected employee in the table
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(manageFrame, "Are you sure you want to delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String employeeNumber = textFields[0].getText();
                DeleteEmployee.deleteEmployee(employeeNumber, model); //Removing the data in the csv file
                RefreshTable.refreshEmployeeTable(model); //Then updates the table after the deletion

                JOptionPane.showMessageDialog(manageFrame, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                manageFrame.dispose(); //This will only close the popup window and not the employee table
            }
        });
    }
}
