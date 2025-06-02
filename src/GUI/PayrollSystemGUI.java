/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Nichie
 */


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import Console.TACP2; // We import this class to javbe access to deductions


public class PayrollSystemGUI extends JFrame {
    private static String loggedInUser = "";
    private static String adminID = "";
    private List<String> employeeNames = new ArrayList<>();
    private Set<String> uniqueEmployeeIDs = new HashSet<>();

    public static void main(String[] args) {
        //Here it will just launch the login screen.
        SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen);
    }
    
    //it will now display the login screen and ask for authentication.
    public static void showLoginScreen() {
        JFrame loginFrame = new JFrame("MotorPH Login");
        loginFrame.setSize(400, 250);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();
        JButton loginButton = new JButton("Login");
        
        //It adds the functionality to our login button.
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());
            
            
            
            //Here is the authentication of the credentials.
            if (authenticateUser(username, password)) {
                loginFrame.dispose();
                SwingUtilities.invokeLater(() -> {
                    PayrollSystemGUI gui = new PayrollSystemGUI(username);
                    gui.setVisible(true);
                });
            } else {
                //We also put error message if it doens't match a data.
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials, please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        //Adding components to our panel.
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passLabel);
        panel.add(passText);
        panel.add(new JLabel());
        panel.add(loginButton);
        
        //Then we add the panel to our login frame and display it in the window.
        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }
    
    //Here it checks the user's credentials through the csv file.
    private static boolean authenticateUser(String username, String password) {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
        String line;
        boolean isFirstEntry = true;
        
        //This will attempt to read the csv file and verify the data.
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] credentials = parseCSVLine(line);
                
                //Now it will check if the username and password is correct or has a match.
                if (credentials.length >= 2 && credentials[0].equals(username) && credentials[1].equals(password))
                if (isFirstEntry) {  
                        adminID = "10001";  
                        isFirstEntry = false;}
                {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static String[] parseCSVLine(String line) {
        return line.replaceAll("\"", "").split(",");
    }

    public PayrollSystemGUI(String username) {
        loggedInUser = username;
        setTitle("MotorPH Payroll System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JButton profileButton = new JButton("View Profile");
        JButton attendanceButton = new JButton("View Attendance");
        JButton payrollButton = new JButton("View Payroll");
        JButton multiplicationTableButton = new JButton("Multiplication Table");
        JButton logoutButton = new JButton("Log Out");

        profileButton.addActionListener(e -> showProfile());
        attendanceButton.addActionListener(e -> showAttendance());
        payrollButton.addActionListener(e -> showPayrollMenu());
        multiplicationTableButton.addActionListener(e -> showMultiplicationTable());
        logoutButton.addActionListener(e -> logOut());

        panel.add(profileButton);
        panel.add(attendanceButton);
        panel.add(payrollButton);
        panel.add(multiplicationTableButton);
        if (loggedInUser.equals("10001")) {  
            JButton adminPanelButton = new JButton("Admin Controls");
            adminPanelButton.addActionListener(e -> {
                dispose();  // Close PayrollSystemGUI
                new AdminAccess().setVisible(true);  // Open Admin Access
            });
            panel.add(adminPanelButton);
        }


        panel.add(logoutButton);

        add(panel);
    }
    private void showAdminPanel() {
        JOptionPane.showMessageDialog(this, "Admin Panel Access Granted!", "Admin Controls", JOptionPane.INFORMATION_MESSAGE);
        // Add admin management functions here.
    }

    private void showMultiplicationTable() {
        StringBuilder table = new StringBuilder();
        int size = 10;

        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                table.append(i * j).append("\t");
            }
            table.append("\n");
        }

        JOptionPane.showMessageDialog(this, table.toString(), "Multiplication Table", JOptionPane.INFORMATION_MESSAGE);
    }

    
    //It retrieves the information of the user from the csv file.
    private static String getUserProfile(String username) {
    String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
    String line;
    StringBuilder profileData = new StringBuilder();
    
    //It will now try to read the csv file and find the user's info.
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        while ((line = br.readLine()) != null) {
            String[] userDetails = parseCSVLine(line);
            
            //now it checks if the data exists in the csv file and show it.
            if (userDetails.length >= 5 && userDetails[0].equals(username)) {
                return "Name: " + userDetails[1] + 
                        "\nEmployee Number: " + userDetails[0] + 
                        "\nBirthday: " + userDetails[3]+ 
                        "\nAddress: " + userDetails[4] + 
                        "\nPhone Number: " + userDetails[5] + 
                        "\nStatus: " + userDetails[10];
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return profileData.length() > 0 ? profileData.toString() : "Profile not found.";
    }

    
    //It will now get all the attendance data of the user from the csv file.
    private static String getUserAttendance(String username) {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";
        String line;
        StringBuilder attendanceDetails = new StringBuilder();
        
        //It will now try to read the csv file and find the data needed.
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 4 && record[0].equals(username)) {
                    attendanceDetails.append("Date: ").append(record[1])
                        .append(" | Employee #: ").append(record[2])
                        .append(" | Hours Worked: ").append(record[3]).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attendanceDetails.length() > 0 ? attendanceDetails.toString() : "No attendance records found.";
    }
    
    private static List<String> getAttendanceHistory(String username) {
    List<String> attendanceRecords = new ArrayList<>();
    String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";
    String line;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        while ((line = br.readLine()) != null) {
            String[] record = parseCSVLine(line);
            if (record.length >= 6 && record[0].equals(username)) {
                LocalTime loginTime = LocalTime.parse(record[4], timeFormatter);
                LocalTime logoutTime = LocalTime.parse(record[5], timeFormatter);
                double hoursWorked = (logoutTime.toSecondOfDay() - loginTime.toSecondOfDay()) / 3600.0; // Convert seconds to hours
                
                attendanceRecords.add("Date: " + record[3] + " | Hours Worked: " + String.format("%.2f", hoursWorked));
            }

        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return attendanceRecords;
}

    
    // This will display an information message to the user
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    // It retrives the details of the logged in user
    private void showProfile() {
        String profileInfo = getUserProfile(loggedInUser);
        JOptionPane.showMessageDialog(this, profileInfo, "User Profile", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //This will display the options of attendance menu
    private void showAttendance() {
        String[] options = {"View Daily Attendance Record", "Calculate Hours Worked Per Week","View Attendance History", "Back to Main Menu"};
        int choice = JOptionPane.showOptionDialog(
            this, 
            "Select an option:", 
            "Attendance Menu", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            options, 
            options[0]
        );

        switch (choice) {
            case 0:
                showDailyAttendance();
                break;
            case 1:
                showWeeklyHoursWorked();
                break;
            //We add an additional option to show attendance history.
            case 2:
                showAttendanceHistory();
                break;
            case 3:
                showMessage("Returning to Main Menu.");
                break;
        }
    }
    
    //This will display the daily attendance of the user. We also amake it scrollable. 
    private void showDailyAttendance() {
    String attendanceInfo = getDailyAttendance(loggedInUser);
    
    JTextArea textArea = new JTextArea(attendanceInfo);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(600, 300));

    JOptionPane.showMessageDialog(this, scrollPane, "Daily Attendance", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //This will show the attendance history of the user.
    private void showAttendanceHistory() {
    List<String> attendanceHistory = getAttendanceHistory(loggedInUser);
    
    StringBuilder historyInfo = new StringBuilder("Attendance History:\n");
    for (String record : attendanceHistory) {
        historyInfo.append(record).append("\n");
    }

    JTextArea textArea = new JTextArea(historyInfo.toString());
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    JOptionPane.showMessageDialog(this, scrollPane, "Full Attendance History", JOptionPane.INFORMATION_MESSAGE);
}

    // This will read attendance CSV file and get the records of the current user.
    private static String getDailyAttendance(String username) {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";
        String line;
        StringBuilder attendanceDetails = new StringBuilder();
        
        //It will try to read the csv file and show the records.
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 4 && record[0].equals(username)) {
                    attendanceDetails.append("First name: ").append(record[2])
                        .append("   |   Employee #: ").append(record[0])
                        .append("   |   Date: ").append(record[3])
                        .append("   |   LogIn: ").append(record[4])
                        .append("   |   LogOut: ").append(record[5]).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attendanceDetails.length() > 0 ? attendanceDetails.toString() : "No attendance records found.";
    }
    
    //Here, it will display the total weekly hours worked by the user.
    private void showWeeklyHoursWorked() {
    String weeklyHoursInfo = getWeeklyHoursWorked(loggedInUser);

    JTextArea textArea = new JTextArea(weeklyHoursInfo);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    
    //We also make it scrollable like what we did in viewing the attendance.
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(200, 300));
    
    //It displays the weekly hours worked in a message dialog
    JOptionPane.showMessageDialog(this, scrollPane, "Weekly Hours Worked", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //It calculates the weekly hours wroked of the user from the Attendance csv file.
    private static String getWeeklyHoursWorked(String username) {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\Copy of MotorPH Employee Data - Attendance Record.csv";
        String line;
        
        //We define here the format of date and time.
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Map<Integer, Double> weeklyHoursMap = new LinkedHashMap<>();
        
        //This will attempt to read the csv file
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                
                //This ensure that the records has has enough fields and can able to determine the username.
                if (record.length >= 5 && record[0].equals(username)) {
                    LocalDate date = LocalDate.parse(record[3], dateFormatter);
                    int weekNumber = (date.getDayOfYear() - 1) / 7 + 1; // This determine which week number it is in a year.

                    LocalTime loginTime = LocalTime.parse(record[4], timeFormatter);
                    LocalTime logoutTime = LocalTime.parse(record[5], timeFormatter);
                    
                    //It calculates the difference in hours between the log in and log out. 
                    double dailyHours = ChronoUnit.MINUTES.between(loginTime, logoutTime) / 60.0;
                    //It gathers the accumulated hours work per week.
                    weeklyHoursMap.put(weekNumber, weeklyHoursMap.getOrDefault(weekNumber, 0.0) + dailyHours);
                }
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
        
        //This make the summary of weekly hours.
        StringBuilder weeklyHoursInfo = new StringBuilder("Weekly Hours Worked:\n");
        for (Map.Entry<Integer, Double> entry : weeklyHoursMap.entrySet()) {
            weeklyHoursInfo.append("Week ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append(" hours\n");
        }
        
        return weeklyHoursInfo.toString();
    }
    
    //Here, it display our payroll menu.
    private void showPayrollMenu() {
    String[] options = {"View Salary", "View Deductions", "View Payslip", "Back to Main Menu"};
    
    //It shows the user the multiple options of our menu.
    int choice = JOptionPane.showOptionDialog(
        this, 
        "Select an option:", 
        "Payroll Menu", 
        JOptionPane.DEFAULT_OPTION, 
        JOptionPane.INFORMATION_MESSAGE, 
        null, 
        options, 
        options[0] //This is the default selection.
            
    );
    
    String username = loggedInUser; 
    double totalHours = 0;
    
    //Here we make an exception for choice 3 since we just it to just back to main menu.
    //So, this logic only works if ther's a prompt needed for hours worked.
    if (choice != 3){
        boolean validInput = false;
        while (!validInput) {
            try {
                String hoursWorkedInput = JOptionPane.showInputDialog("Enter total hours worked:");
                if (hoursWorkedInput == null){
                    return; //It simple execute the promp if the user clicked the cancel button.
                }
                if (hoursWorkedInput == null || hoursWorkedInput.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Input cannot be empty. Please enter a valid number.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    totalHours = Double.parseDouble(hoursWorkedInput);
                    validInput = true; //Exit the loop if the parsing succeeds.
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number for hours worked.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //This will initiate the payroll menu options.
    switch (choice) {
        case 0:
            showSalary(totalHours);
            break;
        case 1:
            showDeductions(username, totalHours);
            break;
        case 2:
            showPayslip(username, totalHours);
            break;
        case 3:
            showMessage("Returning to Main Menu.");
            break;
        }

    }
    
    //This will show the computed salary in a message dialog.
    //We add a sample matrix of the salary tier.
    private void showSalary(double totalHours) { 
    String salaryInfo = computeSalary(loggedInUser, totalHours);
    int[][] salaryMatrix = {
        {35000, 5000, 30000},
        {42000, 6000, 36000},
        {50000, 7500, 42500}
    };

    StringBuilder salaryDetails = new StringBuilder();
    salaryDetails.append(salaryInfo).append("\n\nSample Salary Matrix:\n");
    
    for (int i = 0; i < salaryMatrix.length; i++) {
        salaryDetails.append("Tier ").append(i + 1).append(": Base = ").append(salaryMatrix[i][0])
                     .append(", Tax = ").append(salaryMatrix[i][1])
                     .append(", Net Pay = ").append(salaryMatrix[i][2]).append("\n");
    }

    JOptionPane.showMessageDialog(this, salaryDetails.toString(), "Salary Details", JOptionPane.INFORMATION_MESSAGE);
}

    
    //This will compute the gross salary based on the input in the total hours worked of the user.
    //Then get the details of the user.
    public static String computeSalary(String username, double totalHours) {
        //This is the file path of the employee database or csv file.
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
        String line;
        double hourlyRate = 0;
        String employeeName = "";
        String employeeID = "";
        
        //This will attempt to read the csv file to get the user's salary details which is in column 20 in the csv file.
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 20 && record[0].equals(username)) {
                    employeeName = record[1];
                    employeeID = record[0];
                    hourlyRate = Double.parseDouble(record[19]); // Assuming hourly rate is at index 3
                    break; //The end of the search once the data has found.
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        
        //It computes the groos salary of the user base on his input in total hours worked.
        double grossSalary = totalHours * hourlyRate;
        String formattedSalary = String.format("%.2f", grossSalary);
        return "Employee Name: " + employeeName + "\nEmployee ID: " + employeeID + "\nGross Salary: " + String.format("%.2f", grossSalary);
    }
    
 
    //This will display the salary deductions breakdown.
    //We add the feature ArrayList in this method to store deductions dynamically.
    private void showDeductions(String username, double totalHours) {
        //Here we decided to just import the calculations of the deduction from the console base code.
        //This will only make an instance of console base code (TACP2)to have access to the deduction methods.
        TACP2 tacp2 = new TACP2(); 

        //Accesing the compute salary method from TACP2.
        String salaryInfo = computeSalary(username, totalHours);

        //This will get the gross salary from the returned string.
        String[] salaryDetails = salaryInfo.split("\n");
        double grossSalary = Double.parseDouble(salaryDetails[2].replaceAll("Gross Salary: ", "").trim());

        //Here it computes the deductions using the method we use from TACP2.
        List<Double> deductionsList = new ArrayList<>();
        deductionsList.add(tacp2.computeSSS(grossSalary));
        deductionsList.add(tacp2.computePhilHealth(grossSalary));
        deductionsList.add(tacp2.computePagIbig(grossSalary));
        deductionsList.add(tacp2.computeWithholdingTax(grossSalary));


        //Here we just add all the deductions and it's sum. Then subtract it to the gross salary.
        double totalDeductions = deductionsList.stream().mapToDouble(Double::doubleValue).sum();
        double netPay = grossSalary - totalDeductions;

        //This will display the total deductions and the net pay.
        String deductionsInfo = "Gross Salary: " + String.format("%.2f", grossSalary) +
                            "\nSSS Deduction: " + String.format("%.2f", deductionsList.get(0)) +
                            "\nPhilHealth Deduction: " + String.format("%.2f", deductionsList.get(1)) +
                            "\nPag-IBIG Deduction: " + String.format("%.2f", deductionsList.get(2)) +
                            "\nWithholding Tax: " + String.format("%.2f", deductionsList.get(3)) +
                            "\nTotal Deductions: " + String.format("%.2f", totalDeductions) +
                            "\nNet Pay: " + String.format("%.2f", netPay);

        JOptionPane.showMessageDialog(null, deductionsInfo, "Salary Deductions Breakdown", JOptionPane.INFORMATION_MESSAGE);
    }


  
    //Here it will display the user's payslip. It includes the computation of gross salary, deductions and compensations (allowances).
    private void showPayslip(String username, double totalHours) {
        
        //Accessing the compute salary method from TACP2 to get the gross salary.
        String salaryInfo = computeSalary(username, totalHours);
        String[] salaryDetails = salaryInfo.split("\n");
        double grossSalary = Double.parseDouble(salaryDetails[2].replaceAll("Gross Salary: ", "").trim());

        //This will get the compensations(allowances) from the csv file.
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\MO-IT103-Group18\\src\\payroll\\hub\\databases\\MotorPH Employee Data - Employee Details.csv";
        double riceSubsidy = 0, phoneAllowance = 0, clothingAllowance = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.replace("\"", ""); //We use this to remove unwanted quotes in our csv file.
            String[] record = line.split(",");
            if (record.length >= 19 && record[0].equals(username)) {  
                //Remove quotation marks and commas, then parse as double
                riceSubsidy = Double.parseDouble(record[15].replace("\"", "").replace(",", "").trim());
                phoneAllowance = Double.parseDouble(record[16].replace("\"", "").replace(",", "").trim());
                clothingAllowance = Double.parseDouble(record[17].replace("\"", "").replace(",", "").trim());
                break;
            }
        }


        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        
        //It calculates the total compensation from all the allowances.
        double totalCompensation = riceSubsidy + phoneAllowance + clothingAllowance;

        //Here we use the deduction method from TACP2.
        TACP2 tacp2 = new TACP2();
        double sssDeduction = tacp2.computeSSS(grossSalary);
        double philHealthDeduction = tacp2.computePhilHealth(grossSalary);
        double pagIbigDeduction = tacp2.computePagIbig(grossSalary);
        double withholdingTax = tacp2.computeWithholdingTax(grossSalary);
        double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;
        double netSalary = grossSalary - totalDeductions;

        //It will display the whole payslip with gross salary, deductions, compensation and net salary.
        String payslipInfo = "**PAYSLIP**\n" +
                             "Employee: " + username +
                             "\nGross Salary: " + String.format("%.2f", grossSalary) +
                             "\n\n**Deductions**" +
                             "\nSSS Deduction: " + String.format("%.2f", sssDeduction) +
                             "\nPhilHealth Deduction: " + String.format("%.2f", philHealthDeduction) +
                             "\nPag-IBIG Deduction: " + String.format("%.2f", pagIbigDeduction) +
                             "\nWithholding Tax: " + String.format("%.2f", withholdingTax) +
                             "\nTotal Deductions: " + String.format("%.2f", totalDeductions) +
                             "\n\n**Compensations**" +
                             "\nRice Subsidy: " + String.format("%.2f", riceSubsidy) +
                             "\nPhone Allowance: " + String.format("%.2f", phoneAllowance) +
                             "\nClothing Allowance: " + String.format("%.2f", clothingAllowance) +
                             "\nTotal Compensation: " + String.format("%.2f", totalCompensation) +
                             "\n\n**Net Salary**" +
                             "\nNet Pay: " + String.format("%.2f", netSalary);

        JOptionPane.showMessageDialog(null, payslipInfo, "Generated Payslip", JOptionPane.INFORMATION_MESSAGE);
    }
    

    
    
    

    //We include a logout button and return the user to the login screen.
    private void logOut() {
        dispose(); // Close the main window
        SwingUtilities.invokeLater(PayrollSystemGUI::showLoginScreen); // Return to login screen
    }

    
}