/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import static GUI.ViewEmployee.EMPLOYEE_DETAILS_CSV;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class NewEmployee {
    //Now, this will open a form for adding a new employee
    public static void displayNewEmployeeForm(JFrame parentFrame, DefaultTableModel model) {
        JFrame formFrame = new JFrame("Add New Employee");
        formFrame.setSize(500, 700);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setLocationRelativeTo(parentFrame);
        
        //We use the GridBagLayout to organize our input fields
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //This is are the labels for the employee input fields
        String[] labels = {
            "Employee Number:", "Last Name:", "First Name:", "Birthday:", "Address:", "Phone Number:",
            "SSS Number:", "PhilHealth Number:", "TIN Number:", "Pag-IBIG Number:", "Status:",
            "Position:", "Immediate Supervisor:", "Basic Salary:", "Rice Subsidy:", "Phone Allowance:",
            "Clothing Allowance:", "Gross Semi-Monthly Rate:", "Hourly Rate:"
        };

        JTextField[] textFields = new JTextField[labels.length];
        
        //Now it will generate the form fields with labels and we set the textfields to 20.
        for (int i = 0; i < labels.length; i++) {
            textFields[i] = new JTextField(20);
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(textFields[i], gbc);
        }
        
        //This is the submit button to add the employee to the record
        JButton submitButton = ButtonsStyle.ButtonStyle2("Submit");
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);
        
        //This is the action listener when adding a new emplpoyee data to the csv file and in the table
        submitButton.addActionListener(e -> {
            String[] newEmployee = new String[labels.length];

            //We get the user's input and ensure all text fields are properly read into the array
            for (int i = 0; i < labels.length; i++) {
                newEmployee[i] = textFields[i].getText().trim(); //Remove leading/trailing spaces
            }

            //We debug the output, so we print the input data before writing it in the csv file
            System.out.println("Appending Employee Data: " + String.join(",", newEmployee));

            //This is where we append or put the new employee to the csv file
            appendNewEmployeeToCSV(newEmployee);

            //We want to refresh the Jtable or the employee table to make sure that the new employee is successfully append
            model.addRow(new Object[]{newEmployee[0], newEmployee[1], newEmployee[2], newEmployee[6], 
                                      newEmployee[7], newEmployee[8], newEmployee[9]});
            
            formFrame.dispose(); //This closes the form after the user submit it (dispose)
        });


        formFrame.add(panel);
        formFrame.setVisible(true);
    }


//This method saves the new employee data into the csv file
    private static void appendNewEmployeeToCSV(String[] employeeData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DETAILS_CSV, true))) {
            bw.write(String.join(",", employeeData)); //Append the new employee record
            bw.newLine();
            bw.flush(); //We want to make sure that the data is written in the csv file before closing
            JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving new employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
   

}
