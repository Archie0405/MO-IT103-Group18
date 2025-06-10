/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class UpdateEmployee {
    //This method updates an employee records in the csv file while preserving the old ID for look up
    public static void updateEmployeeInCSV(String[] updatedEmployee, String oldEmployeeNumber, DefaultTableModel model) {
        List<String[]> employees = new ArrayList<>();
        boolean employeeUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(AdminAccess.EMPLOYEE_DETAILS_CSV))) {
            String header = br.readLine();
            employees.add(header.split(","));

            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length >= employees.get(0).length) {
                    if (record[0].equals(oldEmployeeNumber)) { 
                        //We make this if the user wants to change the employee ID, it replaces the old Id with the new ID while keeping all the records intact
                        record[0] = updatedEmployee[0]; // New Employee ID
                        System.arraycopy(updatedEmployee, 1, record, 1, updatedEmployee.length - 1); // Update other fields
                        employeeUpdated = true;
                    }
                    employees.add(record);
                } else {
                    System.err.println("Skipping malformed record: " + line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading employee records!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!employeeUpdated) {
            JOptionPane.showMessageDialog(null, "Error: Employee record not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //This will rewrite the csv file with the updated employee rocords
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AdminAccess.EMPLOYEE_DETAILS_CSV))) {
            for (String[] employee : employees) {
                bw.write(String.join(",", employee));
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving updated employee details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RefreshTable.refreshEmployeeTable(model, updatedEmployee[0], updatedEmployee); // 🔹 Use NEW ID for refresh
        
    }
}
