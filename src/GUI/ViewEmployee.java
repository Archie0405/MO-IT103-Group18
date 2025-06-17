package GUI;

/**
 *
 * @author Nichie
 */

import javax.swing.*;
import java.awt.*;


public class ViewEmployee extends JFrame {
    //File paths for our CSV files
    public static final String EMPLOYEE_DETAILS_CSV = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
    public static final String ATTENDANCE_RECORD_CSV = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";
   
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
    
    
     //This is the method for the log out button
    public void logOut() {
        dispose();
        SwingUtilities.invokeLater(() -> LogIn.showLoginScreen());
    }
    
    //We make this to read the data in the csv file correctly
    public static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); //Preserves fields correctly
    }  
}
