package GUI;

/**
 *
 * @author Nichie
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;


public class AdminAccess extends JFrame {
    //File paths for our CSV files
    private static final String EMPLOYEE_DETAILS_CSV = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
    private static final String ATTENDANCE_RECORD_CSV = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";
    
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
        JButton showEmployeesButton = ButtonStyle1("Show All Employees");
        JButton logoutButton = ButtonStyle1("Log Out");
        
        //This is the action listeners for the buttons
        showEmployeesButton.addActionListener(e -> displayEmployeeTable());
        logoutButton.addActionListener(e -> logOut());
        
        //It adds buttons to our panel
        mainPanel.add(showEmployeesButton);
        mainPanel.add(logoutButton);
        add(mainPanel);
    }
    
    //This is our custon button design for button 1
    private JButton ButtonStyle1(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        button.setPreferredSize(new Dimension(250, 50));
        return button;
    }
    
    //This is our custom button smaller buttons
    private JButton ButtonStyle2(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        button.setPreferredSize(new Dimension(150, 30));
        return button;
    }
    
    //This will display the employee table in a new window
    private void displayEmployeeTable() {
        JFrame employeeFrame = new JFrame("Employee Records");
        employeeFrame.setSize(900, 500);
        employeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        employeeFrame.setLocationRelativeTo(null);
        
        //This is the navigation buttons
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 4, 10, 10));

        JButton viewEmployeeButton = ButtonStyle2("View Employee");
        JButton newEmployeeButton = ButtonStyle2("New Employee");
        JButton manageEmployeeButton = ButtonStyle2("Manage Employees");
        JButton backToAdminButton = ButtonStyle2("Back to Admin Menu");
        JButton logoutButton = ButtonStyle2("Log Out");
        
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
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            br.readLine(); //skipping the header row (br)
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
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
        viewEmployeeButton.addActionListener(e -> viewEmployeeDetails(employeeTable));
        backToAdminButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(backToAdminButton);
            parentFrame.dispose();  //This closes the current window (dispose)
            new AdminAccess().setVisible(true);  //And this will reopen the admin menu
        });
        manageEmployeeButton.addActionListener(e -> displayManageEmployeesPanel(employeeTable, model));

        logoutButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
            parentFrame.dispose();  //It will close the existing window
            SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen);  //Then return to main screen
        });
        newEmployeeButton.addActionListener(e -> displayNewEmployeeForm(employeeFrame, model));


        setVisible(false); //It hides the window instead of closing it
    }
    
    //It display an employee data when a row is selected in the table
    private void viewEmployeeDetails(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFrame detailsFrame = new JFrame("Employee Information");
        detailsFrame.setSize(500, 500);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.setLocationRelativeTo(null);

        String employeeID = table.getValueAt(selectedRow, 0).toString();
        
        //Building the employee data for display
        StringBuilder detailsBuilder = new StringBuilder("<html><h2>Employee Details</h2><p>");
        for (int i = 0; i < table.getColumnCount(); i++) {
            detailsBuilder.append("<b>").append(table.getColumnName(i)).append(":</b> ")
                .append(table.getValueAt(selectedRow, i)).append("<br>");
        }

        detailsBuilder.append("</p></html>");

        final String employeeDetails = detailsBuilder.toString();

        JLabel label = new JLabel(employeeDetails);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        //We make a drop down menu wherein the user can choose a month then compute the salary of the selected employee
        String[] months = {"January", "February", "March", "April", "May", "June", 
                           "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthSelector = new JComboBox<>(months);
        JButton computeSalaryButton = ButtonStyle2("Compute Salary");

        computeSalaryButton.addActionListener(e -> {
            String selectedMonth = (String) monthSelector.getSelectedItem();
            double totalHoursWorked = computeTotalMonthlyHours(employeeID, selectedMonth);
            double hourlyRate = getHourlyRate(employeeID);
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
    private void displayComputedSalary(JFrame frame, String employeeDetails, String month, double totalHoursWorked, double hourlyRate, double salary) {
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
    
    //This is where we compute the total hours worked in a selected month with the use ofd the attendance records.
    private double computeTotalMonthlyHours(String employeeID, String selectedMonth) {
        double totalHours = 0.0;
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_RECORD_CSV))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                
                //This make sure the valid record format and match employee ID
                if (record.length >= 6 && record[0].equals(employeeID)) {
                    String date = record[3];
                    
                    //We convert here the csv date string into a localDate Object
                    LocalDate recordDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    
                    //Here it will check if the records is match to the selected month
                    if (recordDate.getMonth().toString().equalsIgnoreCase(selectedMonth)) {
                        LocalTime loginTime = LocalTime.parse(record[4]);
                        LocalTime logoutTime = LocalTime.parse(record[5]);
                        
                        //Now it calculates the hours worked base on the timestamp of login and logout in the csv file
                        double hoursWorked = logoutTime.getHour() - loginTime.getHour(); // Basic computation
                        totalHours += hoursWorked;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading attendance data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalHours;
    }
    
    //This method retrives the hourly rate of an employee from the csv file
    private double getHourlyRate(String employeeID) {
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 19 && record[0].equals(employeeID)) {
                    return Double.parseDouble(record[18]); // Hourly rate is at index 19 of the csv file, starts with 1
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading employee salary data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0.0; //This is a default return value if the employee is not found
    }
    
    //Now, this will open a form for adding a new employee
    private void displayNewEmployeeForm(JFrame parentFrame, DefaultTableModel model) {
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
        
        //Now it will generate the form fields with labels
        for (int i = 0; i < labels.length; i++) {
            textFields[i] = new JTextField(20);
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(textFields[i], gbc);
        }
        
        //This is the submit button to add the employee to the record
        JButton submitButton = ButtonStyle2("Submit");
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
    private void appendNewEmployeeToCSV(String[] employeeData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DETAILS_CSV, true))) {
            bw.write(String.join(",", employeeData)); //Append the new employee record
            bw.newLine();
            bw.flush(); //We want to make sure that the data is written in the csv file before closing
            JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving new employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
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
    
    //This method updates an employee records in the csv file while preserving the old ID for look up
    private void updateEmployeeInCSV(String[] updatedEmployee, String oldEmployeeNumber, DefaultTableModel model) {
        List<String[]> employees = new ArrayList<>();
        boolean employeeUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DETAILS_CSV))) {
            for (String[] employee : employees) {
                bw.write(String.join(",", employee));
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving updated employee details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshEmployeeTable(model, updatedEmployee[0], updatedEmployee); // 🔹 Use NEW ID for refresh
        
    }
    
    //This method deletes an employee record from the csv file and refreshes the jtable
    private void deleteEmployee(String employeeNumber, DefaultTableModel model) {
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

        refreshEmployeeTable(model); //This is the second method we did when refreshing the Jtable
    }
    
    //This will open a new panel to manage an employee records(update and delete)
    private void displayManageEmployeesPanel(JTable employeeTable, DefaultTableModel model) {
        
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
            updateEmployeeInCSV(updatedEmployee, oldEmployeeNumber, model); //This updates the csv records
            refreshEmployeeTable(model); //Then refresh the table with the new data
            
            JOptionPane.showMessageDialog(manageFrame, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            manageFrame.dispose(); //Then close the management window after the update
        });

        //This will make sure that the delete button will remove the selected employee in the table
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(manageFrame, "Are you sure you want to delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String employeeNumber = textFields[0].getText();
                deleteEmployee(employeeNumber, model); //Removing the data in the csv file
                refreshEmployeeTable(model); //Then updates the table after the deletion

                JOptionPane.showMessageDialog(manageFrame, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                manageFrame.dispose(); //This will only close the popup window and not the employee table
            }
        });
    }
   
    //This is the method for the log out button
    private void logOut() {
        dispose();
        SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen);
    }
    
    //We make this to read the data in the csv file correctly
    private static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // 🔹 Preserves fields correctly
    }
}
