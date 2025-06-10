/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import static GUI.AdminAccess.EMPLOYEE_DETAILS_CSV;
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
public class DeleteEmployee {
    //This method deletes an employee record from the csv file and refreshes the jtable
    public static void deleteEmployee(String employeeNumber, DefaultTableModel model) {
        List<String[]> employees = new ArrayList<>();

        //It will read all the records except to the data that the user has been deleted
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            String header = br.readLine();
            employees.add(header.split(","));

            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split(",");
                if (!record[0].equals(employeeNumber)) {
                    employees.add(record); //It keeps all the the valid records
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading employee records!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Now, it will write back the remaining employees in the csv file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DETAILS_CSV))) {
            for (String[] employee : employees) {
                bw.write(String.join(",", employee));
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving employee records!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RefreshTable.refreshEmployeeTable(model); //This is the second method we did when refreshing the Jtable
    }
}
