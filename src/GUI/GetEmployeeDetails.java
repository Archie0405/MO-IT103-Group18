/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author USER
 */
public class GetEmployeeDetails {
    
    //We did this specific method to get all the employee details, we used this in the manage employee panel
    //We encounter an issue wherein the program rewrites an incorrect index in the csv file because we use the data in the jtable instead of csv file. Fixed
    public static String[] getEmployeeDetails(String employeeID) {
    String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        String line;
        
        while ((line = br.readLine()) != null) {
            String[] details = PayrollSystemGUI.parseCSVLine(line); //Ensure correct parsing

            if (details.length > 0 && details[0].trim().equals(employeeID.trim())) {
                return details; //This return the full employee record as a String array
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    return null; //Return null if no matching employee is found
}
}
