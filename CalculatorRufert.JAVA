import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class CalculatorRufert extends JFrame implements ActionListener {

	private JTextField textDisplay;
	private JTextArea historyDisplay;
	private double input1, input2, resultingValue;
	private String operator;
	private boolean done;
	
	public CalculatorRufert() {
		//mainframe
		setTitle("Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLayout(new BorderLayout());
		
		//Text Display
		textDisplay = new JTextField();
		textDisplay.setEditable(false);
		add(textDisplay, BorderLayout.NORTH);
		//GUI Buttons 
		JPanel buttonGroup = new JPanel();
		buttonGroup.setLayout(new GridLayout(5, 4));
		
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
			buttonGroup.add(b);
		}
		
		add(buttonGroup, BorderLayout.CENTER);
		//Right History Text Area
		JPanel history = new JPanel();
		history.setLayout(new BorderLayout());

		historyDisplay = new JTextArea(10, 20);
		historyDisplay.setEditable(false);
		historyDisplay.setText("History:\n");

		JScrollPane scrollPane = new JScrollPane(historyDisplay);
		history.add(scrollPane, BorderLayout.CENTER);

		// Add the history panel to the right side
		add(history, BorderLayout.EAST);

		//GUI Frame Visibility
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

        new CalculatorRufert();
    }
}