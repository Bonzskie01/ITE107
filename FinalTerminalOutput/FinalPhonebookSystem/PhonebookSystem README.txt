Phonebook System - README
Overview
The Phonebook System is a simple Java application that allows users to manage their contacts. It provides functionalities such as adding, searching, deleting, and updating contacts. The contact data is stored in a text file (phonebook.txt) on the system. The application uses a graphical user interface (GUI) built with Java Swing to facilitate interaction with users.

Features:
- Add Contact: Allows users to input a name and phone number, and store them in a file.
- Search Contact: Enables users to search for a contact by name.
- Delete Contact: Allows users to delete a contact by name.
- Update Contact: Enables users to update the phone number of an existing contact.
- Data Persistence: Contacts are stored in a text file (phonebook.txt), so data is retained even when the program is closed.

Requirements
- Java Version: The program is built using Java and requires Java 8 or higher to run.
- IDE: You can use any Java IDE (e.g., IntelliJ IDEA, Eclipse) or compile from the command line using javac and run using java.

How to Run:
1. Clone or Download the Project:
	-	Clone the repository or download the source code to your local machine.

2. Compile the Code:
	-	If you're using an IDE, open the project and compile it.
	- 	If you're compiling from the command line, navigate to the directory containing the source code and run:

		CODE: javac PhonebookSystem.java

3. Run the Application:
	-	If you're using an IDE, run the PhonebookSystem class directly.
	-	If you're running from the command line, use:

		CODE: java PhonebookSystem

4. Using the Application:
	-	The main window will appear with fields to input a contact's name and phone number.
	-	There are buttons at the bottom for adding, searching, deleting, and updating contacts.
	-	Contacts will be displayed in the middle of the window, and they are also stored in the phonebook.txt file.