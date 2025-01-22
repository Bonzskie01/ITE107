package payroll;

import calculator.Calculator;
import phonebook.PhonebookSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PayrollSystem extends JFrame {
    private JTextField txtEmpId, txtEmpName, txtHourlyRate, txtHoursWorked;
    private JTextArea textArea;
    private final String filePath = "D:/TEST!/payroll/payroll.txt";
    private HashMap<String, String> employeeInfo = new HashMap<>();

    public PayrollSystem() {
        // Setup main JFrame
        setTitle("Payroll System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color for the main frame
        Color backgroundColor = Color.decode("#685752");  // RGB values for #685752
        Color foregroundColor = Color.WHITE;

        // Panels for organizing components
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(backgroundColor); // Background color for input panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(backgroundColor); // Background color for button panel
        JScrollPane scrollPane;

        // Input labels and text fields
        JLabel lblEmpId = new JLabel("Employee ID:");
        lblEmpId.setForeground(Color.WHITE);
        inputPanel.add(lblEmpId);
        txtEmpId = new JTextField();
        inputPanel.add(txtEmpId);

        // For Employee Name
        JLabel lblEmpName = new JLabel("Employee Name:");
        lblEmpName.setForeground(Color.WHITE);
        inputPanel.add(lblEmpName);
        txtEmpName = new JTextField();
        inputPanel.add(txtEmpName);

        // For Hourly Rate
        JLabel lblHourlyRate = new JLabel("Hourly Rate:");
        lblHourlyRate.setForeground(Color.WHITE);
        inputPanel.add(lblHourlyRate);
        txtHourlyRate = new JTextField();
        inputPanel.add(txtHourlyRate);

        // For Hours Worked
        JLabel lblHoursWorked = new JLabel("Hours Worked:");
        lblHoursWorked.setForeground(Color.WHITE);
        inputPanel.add(lblHoursWorked);
        txtHoursWorked = new JTextField();
        inputPanel.add(txtHoursWorked);

        // Buttons
        JButton btnAddEmployee = new JButton("Add Employee");
        btnAddEmployee.setBackground(backgroundColor);
        btnAddEmployee.setForeground(foregroundColor);
        JButton btnCalculatePay = new JButton("Calculate Pay");
        btnCalculatePay.setBackground(backgroundColor);
        btnCalculatePay.setForeground(foregroundColor);
        JButton btnSaveRecord = new JButton("Save Record");
        btnSaveRecord.setBackground(backgroundColor);
        btnSaveRecord.setForeground(foregroundColor);
        JButton btnDisplayRecords = new JButton("Display Records");
        btnDisplayRecords.setForeground(foregroundColor);
        btnDisplayRecords.setBackground(backgroundColor);
        JButton btnUpdateEmployee = new JButton("Update Employee");
        btnUpdateEmployee.setForeground(foregroundColor);
        btnUpdateEmployee.setBackground(backgroundColor);
        JButton btnDeleteRecord = new JButton("Delete Record");
        btnDeleteRecord.setBackground(backgroundColor);
        btnDeleteRecord.setForeground(foregroundColor);

        buttonPanel.add(btnAddEmployee);
        buttonPanel.add(btnCalculatePay);
        buttonPanel.add(btnSaveRecord);
        buttonPanel.add(btnDisplayRecords);
        buttonPanel.add(btnUpdateEmployee); // Add the update button
        buttonPanel.add(btnDeleteRecord); // Add the delete button

        // Open Calculator Button
        JButton btnOpenCalculator = new JButton("Open Calculator");
        btnOpenCalculator.setForeground(foregroundColor);
        btnOpenCalculator.setBackground(backgroundColor);
        btnOpenCalculator.addActionListener(e -> SwingUtilities.invokeLater(Calculator::new));

        // Add to button panel
        buttonPanel.add(btnOpenCalculator);

        // Open PhonebookSystem Button
        JButton btnOpenPhonebookSystem = new JButton("Open PhonebookSystem");
        btnOpenPhonebookSystem.setForeground(foregroundColor);
        btnOpenPhonebookSystem.setBackground(backgroundColor);
        btnOpenPhonebookSystem.addActionListener(e -> {
            try {
                SwingUtilities.invokeLater(PhonebookSystem::new);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Phonebook System: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add to button panel
        buttonPanel.add(btnOpenPhonebookSystem);

        // Create Refresh Button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(backgroundColor);
        btnRefresh.setForeground(foregroundColor);
        buttonPanel.add(btnRefresh);

        // Add Refresh Button Action
        btnRefresh.addActionListener(e -> refreshFields());

		// Text Area for displaying records
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(new Color(104, 87, 82)); // Background color for the text area
		textArea.setForeground(Color.WHITE); // Change text color to white for visibility on dark background
		textArea.setLineWrap(true); // Wrap lines to prevent horizontal scrolling
		textArea.setWrapStyleWord(true); // Wrap at word boundaries

		// Set preferred size for the JTextArea
		textArea.setPreferredSize(new Dimension(500, 150)); // You can adjust this size as necessary

		// Add the JTextArea inside a JScrollPane
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(300, 100)); // You can adjust this size as necessary


        // Add components to JFrame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Load existing records on startup
        loadRecords();

        // Add Employee Button Action
        btnAddEmployee.addActionListener(e -> addEmployee());

        // Calculate Pay Button Action
        btnCalculatePay.addActionListener(e -> calculatePay());

        // Save Record Button Action
        btnSaveRecord.addActionListener(e -> saveRecord());

        // Display Records Button Action
        btnDisplayRecords.addActionListener(e -> displayRecords());

        // Update Employee Button Action
        btnUpdateEmployee.addActionListener(e -> updateEmployee());

        // Delete Record Button Action
        btnDeleteRecord.addActionListener(e -> deleteRecord());
    }

    private void refreshFields() {
        // Clear input fields
        txtEmpId.setText("");
        txtEmpName.setText("");
        txtHourlyRate.setText("");
        txtHoursWorked.setText("");

        // Clear the text area (if desired)
        textArea.setText("");
    }

    private void addEmployee() {
        String empId = txtEmpId.getText();
        String empName = txtEmpName.getText();
        String hourlyRate = txtHourlyRate.getText();
        String hoursWorked = txtHoursWorked.getText();

        // Check if any field is empty
        if (empId.isEmpty() || empName.isEmpty() || hourlyRate.isEmpty() || hoursWorked.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add to employeeInfo HashMap
        employeeInfo.put(empId, empName);

        // Append to the text area to show that the employee was added
        textArea.append(String.format("Employee Added: %s, %s\n", empId, empName));

        // Show a success message that the employee was added
        JOptionPane.showMessageDialog(this, "Employee was added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveRecord() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            ArrayList<String> records = new ArrayList<>();
            String line;
            boolean recordUpdated = false;

            // Read existing records
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(txtEmpId.getText())) {
                    // Update the record if the employee ID matches
                    double hourlyRate = Double.parseDouble(txtHourlyRate.getText());
                    double hoursWorked = Double.parseDouble(txtHoursWorked.getText());
                    double grossPay = hourlyRate * hoursWorked;
                    double deductions = grossPay * (0 / 100); // Default to 0% tax
                    double netPay = grossPay - deductions;
                    records.add(String.format("%s,%s,%.2f,%.2f,%.2f", txtEmpId.getText(), txtEmpName.getText(), hourlyRate, hoursWorked, netPay));
                    recordUpdated = true;
                } else {
                    records.add(line);
                }
            }

            // If record was not updated, add a new record
            if (!recordUpdated) {
                double hourlyRate = Double.parseDouble(txtHourlyRate.getText());
                double hoursWorked = Double.parseDouble(txtHoursWorked.getText());
                double grossPay = hourlyRate * hoursWorked;
                double deductions = grossPay * (0 / 100); // Default to 0% tax
                double netPay = grossPay - deductions;
                records.add(String.format("%s, %s, %.2f, %.2f, %.2f", txtEmpId.getText(), txtEmpName.getText(), hourlyRate, hoursWorked, netPay));
            }

            // Write updated records back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String record : records) {
                    writer.write(record + "\n");
                }
                JOptionPane.showMessageDialog(this, "Record saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error saving record!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculatePay() {
        try {
            // Get hourly rate and hours worked from input fields
            double hourlyRate = Double.parseDouble(txtHourlyRate.getText());
            double hoursWorked = Double.parseDouble(txtHoursWorked.getText());

            // Ask the user to input the tax deduction percentage
            String taxInput = JOptionPane.showInputDialog(this, "Enter tax deduction percentage:", "Tax Deduction", JOptionPane.QUESTION_MESSAGE);

            // Validate user input
            if (taxInput == null || taxInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tax deduction percentage is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double taxPercentage = Double.parseDouble(taxInput);
            if (taxPercentage < 0 || taxPercentage > 100) {
                JOptionPane.showMessageDialog(this, "Please enter a valid tax percentage (0-100)!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculate gross pay, deductions, and net pay
            double grossPay = hourlyRate * hoursWorked;
            double deductions = grossPay * (taxPercentage / 100);
            double netPay = grossPay - deductions;

            // Display the calculated results in the text area
            textArea.append(String.format("Gross Pay: %.2f , Deductions: %.2f , Net Pay: %.2f\n", grossPay, deductions, netPay));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input for hourly rate, hours worked, or tax percentage!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            textArea.setText(""); // Clear the text area before displaying records
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error displaying records!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void updateEmployee() {
    String empId = txtEmpId.getText();
    String empName = txtEmpName.getText();
    String hourlyRate = txtHourlyRate.getText();
    String hoursWorked = txtHoursWorked.getText();

    if (empId.isEmpty() || empName.isEmpty() || hourlyRate.isEmpty() || hoursWorked.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields to update!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        ArrayList<String> records = new ArrayList<>();
        String line;
        boolean recordUpdated = false;

        // Read all records and update the matching one
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields[0].equals(empId)) {
                // Employee found, update the record
                double hourlyRateValue = Double.parseDouble(hourlyRate);
                double hoursWorkedValue = Double.parseDouble(hoursWorked);
                double grossPay = hourlyRateValue * hoursWorkedValue;
                double deductions = grossPay * (0 / 100); // Default 0% tax
                double netPay = grossPay - deductions;

                // Create the updated record
                String updatedRecord = String.format("%s, %s, %.2f, %.2f, %.2f", empId, empName, hourlyRateValue, hoursWorkedValue, netPay);
                records.add(updatedRecord); // Add the updated record
                recordUpdated = true;
            } else {
                records.add(line); // Add the unchanged record
            }
        }

        // If the employee wasn't found, show an error
        if (!recordUpdated) {
            JOptionPane.showMessageDialog(this, "Employee ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Write the updated records back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String record : records) {
                writer.write(record + "\n");
            }
            JOptionPane.showMessageDialog(this, "Employee record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (IOException | NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error updating record!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void deleteRecord() {
        String empId = txtEmpId.getText();

        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            ArrayList<String> records = new ArrayList<>();
            String line;
            boolean recordFound = false;

            // Read records and filter out the one to delete
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (!fields[0].equals(empId)) {
                    records.add(line);
                } else {
                    recordFound = true;
                }
            }

            if (!recordFound) {
                JOptionPane.showMessageDialog(this, "Employee ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Write remaining records back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String record : records) {
                    writer.write(record + "\n");
                }
                JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting record!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to load existing records
    private void loadRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                employeeInfo.put(fields[0], fields[1]);  // Store empId and empName
            }
        } catch (IOException e) {
            // If file doesn't exist, no need to handle exception
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollSystem app = new PayrollSystem();
            app.setVisible(true);
        });
    }
}
