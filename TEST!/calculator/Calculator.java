package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private double num1, num2, result;
    private String operator;
    private final String historyFile = "D:/TEST!/calculator/calculator_history.txt"; // File to store history

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);

        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 24));
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5)); // Updated layout to fit Clear button

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C" // Clear button
        };

        for (String button : buttons) {
            JButton b = new JButton(button);
            b.setFont(new Font("Arial", Font.BOLD, 18));
            b.addActionListener(this);
            buttonPanel.add(b);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // Add menu for history and exit
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        
        JMenuItem viewHistory = new JMenuItem("View History");
        viewHistory.addActionListener(e -> displayHistory());
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> exitCalculator()); // Exit action
        
        menu.add(viewHistory);
        menu.add(exitMenuItem); // Add exit option to menu
        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
            display.setText(display.getText() + command);
        } else if (command.equals("=")) {
            try {
                num2 = Double.parseDouble(display.getText());
                calculate();
                display.setText(String.valueOf(result));
                saveToHistory(num1, operator, num2, result); // Save calculation to history
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        } else if (command.equals("C")) { // Clear button logic
            display.setText("");
            num1 = 0;
            num2 = 0;
            result = 0;
            operator = null;
        } else {
            try {
                operator = command;
                num1 = Double.parseDouble(display.getText());
                display.setText("");
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        }
    }

    private void calculate() {
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    display.setText("Error");
                    result = 0;
                }
                break;
        }
    }

    private void saveToHistory(double num1, String operator, double num2, double result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile, true))) {
            writer.write(num1 + " " + operator + " " + num2 + " = " + result);
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving to history file.");
        }
    }

    private void displayHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            StringBuilder history = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
            JTextArea textArea = new JTextArea(history.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Calculation History", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading history file.");
        }
    }

    private void exitCalculator() {
        dispose(); // Close the calculator window
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
