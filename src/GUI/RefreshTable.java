/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import static GUI.AdminAccess.EMPLOYEE_DETAILS_CSV;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class RefreshTable {
    //This method updates a specific employee data in the Jtable after the updates or any modifications
    public static void refreshEmployeeTable(DefaultTableModel model, String employeeNumber, String[] updatedEmployee) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).toString().equals(employeeNumber)) {
                
                //This for loop will update individual fields in the table row
                for (int col = 0; col < updatedEmployee.length; col++) {
                    model.setValueAt(updatedEmployee[col], row, col);
                }
                model.fireTableDataChanged(); //This ensure that the JTable updates visually
                return; //Then stops after updating the row
            }
        }
    }
    
    //We make a seperate method when refreshing the jtable since in delete method we only delete a row
    public static void refreshEmployeeTable(DefaultTableModel model) {
        
        //This will clear the Jtable before it reloads the data to prevent duplication of the rows
        model.setRowCount(0);

        List<String[]> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split(",");
                
                //It make sure the employee records length and also check if the employee number is numeric
                if (record.length >= 19 && record[0].matches("\\d+")) {
                    employees.add(record);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing table!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //It shows the table with all valid employee rocords
        for (String[] employee : employees) {
            model.addRow(new Object[]{employee[0], employee[1], employee[2], employee[6], employee[7], employee[8], employee[9]});
        }

        model.fireTableDataChanged(); // Ensure JTable updates visually
    }
}
