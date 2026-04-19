Calculator System - README
Overview
The Calculator System is a simple Java application that provides a graphical user interface (GUI) for performing basic arithmetic operations. It supports addition, subtraction, multiplication, and division. The application also includes a history feature to track the calculations performed, which are stored in a text file (calculator_history.txt). Built using Java Swing, the interface is intuitive and user-friendly.

Features:
	- Basic Arithmetic Operations: Perform addition, subtraction, multiplication, and division.
	- History Tracking: All calculations are recorded and saved in a text file (calculator_history.txt).
	- User Interface: A simple and clean GUI with buttons for numbers, operators, and additional functionalities like clearing the input.
	- Persistent Data: The history of calculations is stored in a file, allowing users to see past results even after restarting the application.

Requirements:
	- Java Version: The program is built using Java and requires Java 8 or higher to run.
	- IDE: You can use any Java IDE (e.g., IntelliJ IDEA, Eclipse) or compile from the command line using javac and run using java.

How to Run:
1. Clone or Download the Project:
	- Clone the repository or download the source code to your local machine.
2. Compile the Code:
	- If you're using an IDE, open the project and compile it.
	- If you're compiling from the command line, navigate to the directory containing the source code and run:
		
		CODE: javac Calculator.java

3. Run the Application:
	- If you're using an IDE, run the Calculator class directly.
	- If you're running from the command line, use:

		CODE: java Calculator

4. Using the Application:
	- The main window will appear with a display at the top to show the current input or result.
	- Buttons for digits (0-9), operators (+, -, *, /), and other functions like =, C, and . will be available.
	- The right panel will display the history of calculations performed, which is updated after each calculation.
	- You can clear the display by clicking the C button, and the calculation history is saved in the calculator_history.txt file.