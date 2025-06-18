package GUI;

/**
 *
 * @author Nichie
 */

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;


public class AdminAccess extends JFrame {
    //File paths for our CSV files
    public static final String EMPLOYEE_DETAILS_CSV = "C:\\Users\\Mow\\OneDrive\\Documents\\NetBeansProjects\\CP2\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
    public static final String ATTENDANCE_RECORD_CSV = "C:\\Users\\Mow\\OneDrive\\Documents\\NetBeansProjects\\CP2\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";
    
    //This is the constructor, it will set up the admin window
    public AdminAccess() {
        setTitle("Admin Access");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // This creates a main panel with formatted buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        
        //This is the main buttons, we use button style number 1 since we created 2 buttons styles
        JButton adminAccess = ButtonsStyle.ButtonStyle1("Admin Access");
        //We added here a new button just incase the admin user wants to go back to employee mode.
        JButton showEmployeeAccess = ButtonsStyle.ButtonStyle1("Employee Access");
        JButton logoutButton = ButtonsStyle.ButtonStyle1("Log Out");
        
        //This is the action listeners for the buttons
        adminAccess.addActionListener(e -> {
            dispose();
            EmployeeTable.displayEmployeeTable();
        });
        //In this action listener, I want the admin user to be redirected to the employee mode of the program
        //This invoke their admin access if they opt to switch in employee mode
        showEmployeeAccess.addActionListener(e -> {
            dispose();
        String loggedInUser  = "DefaultUser";    
        SwingUtilities.invokeLater(() -> new PayrollSystemGUI(loggedInUser ).setVisible(true));
        });
        
        logoutButton.addActionListener(e -> logOut());
        
        //It adds buttons to our panel
        mainPanel.add(adminAccess);
        mainPanel.add(showEmployeeAccess);
        mainPanel.add(logoutButton);
        add(mainPanel);
    }
   
    //It display an employee data when a row is selected in the table
    public static void viewEmployeeDetails(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String employeeID = table.getValueAt(selectedRow, 0).toString(); //Get ID from selected table row

        // Use existing method to retrieve full employee record from CSV
        String[] fullDetails = GetEmployeeDetails.getEmployeeDetails(employeeID);//Be sure this method is complete
        
        if (fullDetails == null) {
            JOptionPane.showMessageDialog(null, "Could not find complete employee data.", "Data Missing", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        String[] headers = {
            "Employee ID", "Last Name", "First Name", "Birthday",
            "Address", "Phone Number", "SSS Number", "PhilHealth Number", "TIN",
            "Pag-IBIG Number", "Status", "Position", "Immediate Supervisor",
            "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance",
            "Gross Semi-monthly Rate", "Hourly Rate"
        };

        //Building the employee data for display
        StringBuilder detailsBuilder = new StringBuilder("<html><h2>Employee Details</h2><p>");
        for (int i = 0; i < Math.min(headers.length, fullDetails.length); i++) {
            detailsBuilder.append("<b>").append(headers[i]).append(":</b> ")
                .append(fullDetails[i]).append("<br>");
        }
        

        detailsBuilder.append("</p></html>");

        final String employeeDetails = detailsBuilder.toString();
        JLabel label = new JLabel(employeeDetails);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        JFrame detailsFrame = new JFrame("Employee Information");
        detailsFrame.setSize(600, 800);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);

        
        //We make a drop down menu wherein the user can choose a month then compute the salary of the selected employee
        String[] months = {"January", "February", "March", "April", "May", "June", 
                           "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthSelector = new JComboBox<>(months);
        JButton computeSalaryButton = ButtonsStyle.ButtonStyle2("Compute Salary");
        
        //This is the action listener for Salary computation and we call the methods to get the total hours worked, hourly rate and salary.
        computeSalaryButton.addActionListener(e -> {
            String selectedMonth = (String) monthSelector.getSelectedItem();
            double totalHoursWorked = TotalMonthlyHours.computeTotalMonthlyHours(employeeID, selectedMonth);
            double hourlyRate = HourlyRate.getHourlyRate(employeeID);
            double salary = totalHoursWorked * hourlyRate;

            displayComputedSalary(detailsFrame, employeeDetails, selectedMonth, totalHoursWorked, hourlyRate, salary);
        });
        
        //It adds the components to the panel
        JPanel selectionPanel = new JPanel();
        selectionPanel.add(new JLabel("Select Month:"));
        selectionPanel.add(monthSelector);
        selectionPanel.add(computeSalaryButton);

        detailsFrame.setLayout(new BorderLayout());
        detailsFrame.add(label, BorderLayout.CENTER);
        detailsFrame.add(selectionPanel, BorderLayout.SOUTH);
        detailsFrame.setVisible(true);
    }
    
    //It will now display the computed salary
    private static void displayComputedSalary(JFrame frame, String employeeDetails, String month, double totalHoursWorked, double hourlyRate, double salary) {
        //We use html for formatting when showing the computed salary
        String salaryInfo = "<html><h2>Salary Details for " + month + "</h2><p>"
                          + "<b>Total Hours Worked:</b> " + totalHoursWorked + " hrs<br>"
                          + "<b>Hourly Rate:</b> ₱ " + hourlyRate + "/hr<br>"
                          + "<b>Gross Salary:</b> ₱ " + salary + "</p></html>";
        
        //Here, we create the lables for employee details and their salary
        JLabel detailsLabel = new JLabel(employeeDetails);
        JLabel salaryLabel = new JLabel(salaryInfo);
        
        //We set the text in the center for better representation
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        salaryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Here, it arranges the details in a panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(detailsLabel);
        panel.add(salaryLabel);
        
        //We want to refresh the frame to show the updated salary details
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }
    
    
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
 
    //This is the method for the log out button
    private void logOut() {
        dispose();
        SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen);
    }
    
    //We make this to read the data in the csv file correctly
    public static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); //Preserves fields correctly
    }
  
}
