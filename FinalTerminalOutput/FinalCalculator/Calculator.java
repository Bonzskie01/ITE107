import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Calculator extends JFrame implements ActionListener {

    private JTextField textDisplay;
    private JTextArea historyDisplay;
    private double input1, input2, resultingValue;
    private String operator;
    private boolean done;

    public Calculator() {
        // Mainframe
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLayout(new BorderLayout());

        // Define background and foreground colors
        Color backgroundColor = Color.decode("#322C2B");  // Dark brown
        Color foregroundColor = Color.WHITE;
        Color buttonColor = Color.decode("#6C4F37");  // Light brown for buttons
        Color buttonTextColor = Color.WHITE;

        // Set background color for the main frame
        getContentPane().setBackground(backgroundColor);

        // Text Display
        textDisplay = new JTextField();
        textDisplay.setEditable(false);
        textDisplay.setBackground(backgroundColor);
        textDisplay.setForeground(foregroundColor);
        textDisplay.setFont(new Font("Arial", Font.BOLD, 40));
        add(textDisplay, BorderLayout.NORTH);

        // GUI Buttons 
        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new GridLayout(5, 4));
		buttonGroup.setBackground(backgroundColor);

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C"
        };

        for (String button : buttons) {
            JButton b = new JButton(button);
            b.addActionListener(this);
            b.setBackground(buttonColor);
            b.setForeground(buttonTextColor);
            b.setFont(new Font("Arial", Font.PLAIN, 17));  // Adjust font size for buttons
            buttonGroup.add(b);
        }

        add(buttonGroup, BorderLayout.CENTER);

        // Right History Text Area
        JPanel history = new JPanel();
        history.setLayout(new BorderLayout());

        historyDisplay = new JTextArea(10, 10);
        historyDisplay.setEditable(false);
        historyDisplay.setBackground(backgroundColor);
        historyDisplay.setForeground(foregroundColor);
        historyDisplay.setText("History:\n");

        JScrollPane scrollPane = new JScrollPane(historyDisplay);
        history.add(scrollPane, BorderLayout.CENTER);

        // Add the history panel to the right side
        add(history, BorderLayout.EAST);

        // Set the GUI frame visibility
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String calcuInput = e.getActionCommand();

        // No Input And The First Input is 0
        if (textDisplay.getText().length() <= 0 && calcuInput.equals("0")) {
            return;
        }

        // Reset Text Area And Values of Input1 And Input2 Variables
        if (calcuInput.equals("C")) {
            input1 = 0;
            input2 = 0;
            textDisplay.setText("");
        }
        // If Input is Digit or Dot (.)
        else if (Character.isDigit(calcuInput.charAt(0)) || calcuInput.equals(".")) {
            // To Avoid Writing The Next Input Into The Previous Output
            if (done) {
                textDisplay.setText("");
                done = false;
            }

            // If input is Dot (.) but there is already a Dot
            if (calcuInput.equals(".") && textDisplay.getText().contains(".")) {
                return;
            }

            // Add the NEW Number or Dot (.) Input Into The Previous
            textDisplay.setText(textDisplay.getText() + calcuInput);
        }
        // Input Is Equal Sign (=)
        else if (calcuInput.equals("=")) {
            input2 = Double.parseDouble(textDisplay.getText());
            calculate();
            done = true;

            // Update the history display
            try {
                String history = historyRecorder(
                    String.valueOf(input1) + " " +
                    operator + " " +
                    String.valueOf(input2) + " = " +
                    (resultingValue == (long) resultingValue ? String.valueOf((long) resultingValue) : String.valueOf(resultingValue))
                );
                historyDisplay.setText("History:\n" + history);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // Input is NOT A Digit, Dot (.) or Equal Sign (=)
        else {
            operator = calcuInput;
            input1 = Double.parseDouble(textDisplay.getText());
            textDisplay.setText("");
            done = false;
        }
    }

    public void calculate() {
        switch (operator) {
            case "+":
                resultingValue = input1 + input2;
                break;
            case "-":
                resultingValue = input1 - input2;
                break;
            case "*":
                resultingValue = input1 * input2;
                break;
            case "/":
                resultingValue = input1 / input2;
                break;
        }

        // Format the resulting value to display without `.0` if it's a whole number
        if (resultingValue == (long) resultingValue) {
            textDisplay.setText(String.valueOf((long) resultingValue)); // Convert to long for whole numbers
        } else {
            textDisplay.setText(String.valueOf(resultingValue)); // Show decimal for fractional values
        }
    }

    public String historyRecorder(String record) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("calculator_history.txt"));
        StringBuilder history = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            history.append(line).append(System.lineSeparator());
        }

        history.append(record);

        BufferedWriter writer = new BufferedWriter(new FileWriter("calculator_history.txt"));
        writer.write(history.toString());

        reader.close();
        writer.close();

        return history.toString();
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("calculator_history.txt"));
        writer.write("");
        writer.close();

        new Calculator();
    }
}
