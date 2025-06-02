package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class AdminAccess extends JFrame {
    private static final String EMPLOYEE_DETAILS_CSV = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
    private static final String ATTENDANCE_RECORD_CSV = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";

    public AdminAccess() {
        setTitle("Admin Access");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with button formatting consistent with PayrollSystemGUI
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JButton showEmployeesButton = ButtonStyle1("Show All Employees");
        JButton logoutButton = ButtonStyle1("Log Out");

        showEmployeesButton.addActionListener(e -> displayEmployeeTable());
        logoutButton.addActionListener(e -> logOut());

        mainPanel.add(showEmployeesButton);
        mainPanel.add(logoutButton);
        add(mainPanel);
    }

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

    private void displayEmployeeTable() {
        JFrame employeeFrame = new JFrame("Employee Records");
        employeeFrame.setSize(900, 500);
        employeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        employeeFrame.setLocationRelativeTo(null);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 4, 10, 10));

        JButton viewEmployeeButton = ButtonStyle2("View Employee");
        JButton newEmployeeButton = ButtonStyle2("New Employee");
        JButton backToAdminButton = ButtonStyle2("Back to Admin Menu");
        JButton logoutButton = ButtonStyle2("Log Out");

        menuPanel.add(viewEmployeeButton);
        menuPanel.add(newEmployeeButton);
        menuPanel.add(backToAdminButton);
        menuPanel.add(logoutButton);

        String[] columnNames = {"Employee Number", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
            return false;}
        };
        JTable employeeTable = new JTable(model);

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 20) {  
                    model.addRow(new Object[]{record[0], record[1], record[2], record[6], record[7], record[8], record[9]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(employeeFrame, "Error loading employee records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        employeeFrame.setLayout(new BorderLayout());
        employeeFrame.add(menuPanel, BorderLayout.NORTH);
        employeeFrame.add(scrollPane, BorderLayout.CENTER);
        employeeFrame.setVisible(true);

        viewEmployeeButton.addActionListener(e -> viewEmployeeDetails(employeeTable));
        backToAdminButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(backToAdminButton);
            parentFrame.dispose();  // Close current window
            new AdminAccess().setVisible(true);  // Reopen Admin menu
        });

        logoutButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
            parentFrame.dispose();  // Close current window
            SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen);  // Return to login screen
        });
        newEmployeeButton.addActionListener(e -> displayNewEmployeeForm(employeeFrame, model));


        dispose();
    }

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

        StringBuilder detailsBuilder = new StringBuilder("<html><h2>Employee Details</h2><p>");
        for (int i = 0; i < table.getColumnCount(); i++) {
            detailsBuilder.append("<b>").append(table.getColumnName(i)).append(":</b> ")
                .append(table.getValueAt(selectedRow, i)).append("<br>");
        }

        detailsBuilder.append("</p></html>");

        final String employeeDetails = detailsBuilder.toString();

        JLabel label = new JLabel(employeeDetails);
        label.setHorizontalAlignment(SwingConstants.CENTER);

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

        JPanel selectionPanel = new JPanel();
        selectionPanel.add(new JLabel("Select Month:"));
        selectionPanel.add(monthSelector);
        selectionPanel.add(computeSalaryButton);

        detailsFrame.setLayout(new BorderLayout());
        detailsFrame.add(label, BorderLayout.CENTER);
        detailsFrame.add(selectionPanel, BorderLayout.SOUTH);
        detailsFrame.setVisible(true);
    }

    private void displayComputedSalary(JFrame frame, String employeeDetails, String month, double totalHoursWorked, double hourlyRate, double salary) {
        String salaryInfo = "<html><h2>Salary Details for " + month + "</h2><p>"
                          + "<b>Total Hours Worked:</b> " + totalHoursWorked + " hrs<br>"
                          + "<b>Hourly Rate:</b> ₱ " + hourlyRate + "/hr<br>"
                          + "<b>Gross Salary:</b> ₱ " + salary + "</p></html>";

        JLabel detailsLabel = new JLabel(employeeDetails);
        JLabel salaryLabel = new JLabel(salaryInfo);
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        salaryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(detailsLabel);
        panel.add(salaryLabel);

        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }
    
    private double computeTotalMonthlyHours(String employeeID, String selectedMonth) {
        double totalHours = 0.0;
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_RECORD_CSV))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 6 && record[0].equals(employeeID)) {
                    String date = record[3];
                    LocalDate recordDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    if (recordDate.getMonth().toString().equalsIgnoreCase(selectedMonth)) {
                        LocalTime loginTime = LocalTime.parse(record[4]);
                        LocalTime logoutTime = LocalTime.parse(record[5]);
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

    private double getHourlyRate(String employeeID) {
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 20 && record[0].equals(employeeID)) {
                    return Double.parseDouble(record[19]); // Hourly rate is at index 19
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading employee salary data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0.0;
    }
    
    private void displayNewEmployeeForm(JFrame parentFrame, DefaultTableModel model) {
        JFrame formFrame = new JFrame("Add New Employee");
        formFrame.setSize(500, 700);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {
            "Employee Number:", "Last Name:", "First Name:", "Birthday:", "Address:", "Phone Number:",
            "SSS Number:", "PhilHealth Number:", "TIN Number:", "Pag-IBIG Number:", "Status:",
            "Position:", "Immediate Supervisor:", "Basic Salary:", "Rice Subsidy:", "Phone Allowance:",
            "Clothing Allowance:", "Gross Semi-Monthly Rate:", "Hourly Rate:"
        };

        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            textFields[i] = new JTextField(20);
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(textFields[i], gbc);
        }

        JButton submitButton = ButtonStyle2("Submit");
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String[] newEmployee = new String[labels.length];

            // Ensure all text fields are properly read into the array
            for (int i = 0; i < labels.length; i++) {
                newEmployee[i] = textFields[i].getText().trim(); // Remove leading/trailing spaces
            }

            // Debugging check: Print the values before writing
            System.out.println("Appending Employee Data: " + String.join(",", newEmployee));

            // Append to CSV
            appendNewEmployeeToCSV(newEmployee);

            // Refresh JTable with only relevant fields
            model.addRow(new Object[]{newEmployee[0], newEmployee[1], newEmployee[2], newEmployee[6], 
                                      newEmployee[7], newEmployee[8], newEmployee[9]});
            
            formFrame.dispose();
        });


        formFrame.add(panel);
        formFrame.setVisible(true);
    }


    private void appendNewEmployeeToCSV(String[] employeeData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DETAILS_CSV, true))) {
            bw.write(String.join(",", employeeData));
            bw.newLine();
            bw.flush(); // Ensure data is written before closing
            JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving new employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   

    private void logOut() {
        dispose();
        SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen);
    }

    private static String[] parseCSVLine(String line) {
        return line.replaceAll("\"", "").split(",");
    }
}
