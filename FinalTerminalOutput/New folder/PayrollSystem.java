import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class PayrollSystem extends JFrame {
    private JTextField txtEmpId, txtEmpName, txtHourlyRate, txtHoursWorked;
    private JTextArea textArea;
    private final String filePath = "payroll.txt";

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
		JLabel lblEmpId = new JLabel("Employee ID:");  // Create a JLabel instance
		lblEmpId.setForeground(Color.WHITE);  // Set the text color to white
		inputPanel.add(lblEmpId);  // Add it to the inputPanel
		txtEmpId = new JTextField();  // Create the JTextField
		inputPanel.add(txtEmpId);  // Add it to the inputPanel

		// For Employee Name
		JLabel lblEmpName = new JLabel("Employee Name:");
		lblEmpName.setForeground(Color.WHITE);  // Set the text color to white
		inputPanel.add(lblEmpName);  // Add it to the inputPanel
		txtEmpName = new JTextField();  // Create the JTextField
		inputPanel.add(txtEmpName);  // Add it to the inputPanel

		// For Hourly Rate
		JLabel lblHourlyRate = new JLabel("Hourly Rate:");
		lblHourlyRate.setForeground(Color.WHITE);  // Set the text color to white
		inputPanel.add(lblHourlyRate);  // Add it to the inputPanel
		txtHourlyRate = new JTextField();  // Create the JTextField
		inputPanel.add(txtHourlyRate);  // Add it to the inputPanel

		// For Hours Worked
		JLabel lblHoursWorked = new JLabel("Hours Worked:");
		lblHoursWorked.setForeground(Color.WHITE);  // Set the text color to white
		inputPanel.add(lblHoursWorked);  // Add it to the inputPanel
		txtHoursWorked = new JTextField();  // Create the JTextField
		inputPanel.add(txtHoursWorked);  // Add it to the inputPanel

        // Buttons
        JButton btnAddEmployee = new JButton("Add Employee");
        JButton btnCalculatePay = new JButton("Calculate Pay");
        JButton btnSaveRecord = new JButton("Save Record");
        JButton btnDisplayRecords = new JButton("Display Records");
        JButton btnUpdateEmployee = new JButton("Update Employee"); // New button
        JButton btnDeleteRecord = new JButton("Delete Record"); // New Delete button

        buttonPanel.add(btnAddEmployee);
        buttonPanel.add(btnCalculatePay);
        buttonPanel.add(btnSaveRecord);
        buttonPanel.add(btnDisplayRecords);
        buttonPanel.add(btnUpdateEmployee); // Add the update button
        buttonPanel.add(btnDeleteRecord); // Add the delete button

        // Text Area for displaying records
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(104, 87, 82)); // Background color for the text area
        textArea.setForeground(Color.WHITE); // Change text color to white for visibility on dark background
        scrollPane = new JScrollPane(textArea);

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

    private void addEmployee() {
        String empId = txtEmpId.getText();
        String empName = txtEmpName.getText();
        String hourlyRate = txtHourlyRate.getText();
        String hoursWorked = txtHoursWorked.getText();

        if (empId.isEmpty() || empName.isEmpty() || hourlyRate.isEmpty() || hoursWorked.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        textArea.append(String.format("Employee Added: %s, %s\n", empId, empName));
    }

    private void calculatePay() {
        try {
            double hourlyRate = Double.parseDouble(txtHourlyRate.getText());
            double hoursWorked = Double.parseDouble(txtHoursWorked.getText());
            double grossPay = hourlyRate * hoursWorked;
            double deductions = grossPay * 0.2; // Assuming 20% tax/deductions
            double netPay = grossPay - deductions;

            textArea.append(String.format("Gross Pay: %.2f, Net Pay: %.2f\n", grossPay, netPay));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input for hourly rate or hours worked!", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                    double deductions = grossPay * 0.2;
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
                double deductions = grossPay * 0.2;
                double netPay = grossPay - deductions;
                records.add(String.format("%s,%s,%.2f,%.2f,%.2f", txtEmpId.getText(), txtEmpName.getText(), hourlyRate, hoursWorked, netPay));
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

    private void displayRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            textArea.setText("");
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading records!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            // Ignore if file not found, as it will be created later
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading records!", "Error", JOptionPane.ERROR_MESSAGE);
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

        // Update the employee record when the employee ID is found
        saveRecord();
    }

    // Delete Record Functionality
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollSystem app = new PayrollSystem();
            app.setVisible(true);
        });
    }
}
