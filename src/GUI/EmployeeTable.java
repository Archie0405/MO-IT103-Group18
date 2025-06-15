/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Nichie
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class EmployeeTable {
    //This will display the employee table in a new window
    public static void displayEmployeeTable() {
        JFrame employeeFrame = new JFrame("Employee Records");
        employeeFrame.setSize(900, 500);
        employeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        employeeFrame.setLocationRelativeTo(null);
        
        //This is the navigation buttons and their layout and style
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 4, 10, 10));
        JButton viewEmployeeButton = ButtonsStyle.ButtonStyle2("View Employee");
        JButton newEmployeeButton = ButtonsStyle.ButtonStyle2("New Employee");
        JButton manageEmployeeButton = ButtonsStyle.ButtonStyle2("Manage Employees");
        JButton backToAdminButton = ButtonsStyle.ButtonStyle2("Back to Admin Menu");
        JButton logoutButton = ButtonsStyle.ButtonStyle2("Log Out");
        
        //It will add the navigation buttons to the window
        menuPanel.add(viewEmployeeButton);
        menuPanel.add(newEmployeeButton);
        menuPanel.add(manageEmployeeButton);
        menuPanel.add(backToAdminButton);
        menuPanel.add(logoutButton);
        
        //This will define the table model with headers
        String[] columnNames = {"Employee Number", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
            return false;}
        };
        JTable employeeTable = new JTable(model);
        
        //It will now try to read the records from the csv file and show the table
        try (BufferedReader br = new BufferedReader(new FileReader(AdminAccess.EMPLOYEE_DETAILS_CSV))) {
            br.readLine(); //skipping the header row (br)
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = AdminAccess.parseCSVLine(line);
                if (record.length >= 19) {  //this ensure that the index is >=19, a right format
                    model.addRow(new Object[]{record[0], record[1], record[2], record[6], record[7], record[8], record[9]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(employeeFrame, "Error loading employee records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //We use a scrollbar in the table, it also set the layout
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        employeeFrame.setLayout(new BorderLayout());
        employeeFrame.add(menuPanel, BorderLayout.NORTH);
        employeeFrame.add(scrollPane, BorderLayout.CENTER);
        employeeFrame.setVisible(true);
        
        //Here we define the actions listeners for our buttons
        viewEmployeeButton.addActionListener(e -> AdminAccess.viewEmployeeDetails(employeeTable));
        backToAdminButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(backToAdminButton);
            parentFrame.dispose();  //This closes the current window (dispose)
            new AdminAccess().setVisible(true);  //And this will reopen the admin menu
        });
        manageEmployeeButton.addActionListener(e -> ManageEmployeePanelForUpdateAndDelete.displayManageEmployeesPanel(employeeTable, model));

        logoutButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
            parentFrame.dispose();  //It will close the existing window
            SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen);  //Then return to main screen
        });
        newEmployeeButton.addActionListener(e -> AdminAccess.displayNewEmployeeForm(employeeFrame, model));

 
        
    }
    
    //We make this method just to update the table visually
    public static void updateJTable(JTable employeeTable, JTextField[] textFields) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();

        //Ensure table isn't empty before updating
        if (model.getRowCount() == 0 || textFields.length < model.getColumnCount()) {
            return;
        }

        //Mapping JTable columns (0–6) to their corresponding textField indices
        int[] columnIndexes = {0, 1, 2, 3, 4, 5, 6};
        for (int row = 0; row < columnIndexes.length; row++) {
            for (int i = 0; i < columnIndexes.length; i++) {
                employeeTable.setValueAt(textFields[i].getText().trim(), row, columnIndexes[i]);
            }
        }

        ((DefaultTableModel) employeeTable.getModel()).fireTableDataChanged(); //Refresh JTable
        JOptionPane.showMessageDialog(null, "Employee table updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    
}
