//Libraries for the program
import javax.swing.*;  // Import Swing components for GUI
import java.awt.*;      // Import AWT components for GUI layout
import java.awt.event.*; // Import event handling classes
import java.io.*;       // Import file handling classes

public class PhonebookSystem extends JFrame implements ActionListener {
    private JTextField nameField, phoneField;  // Fields for user input (name and phone)
    private JTextArea displayArea;             // Area to display contacts in the GUI
    private String filename = "phonebook.txt"; // Filename to store contacts
	
    public PhonebookSystem() {
        setTitle("Phonebook System"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when window is closed
        setSize(400, 300); // Set the size of the window to 400x300 pixels
        setLayout(new BorderLayout()); // Use BorderLayout for the layout manager

        // Define background and foreground colors for consistency
        Color backgroundColor = Color.decode("#322C2B"); // Dark brown background color
        Color foregroundColor = Color.WHITE; // White text color

        // Input Panel for name and phone fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2)); // Use GridLayout for input fields
        inputPanel.setBackground(backgroundColor); // Set background color for input panel
        inputPanel.setForeground(foregroundColor); // Set foreground color for input panel

        // Name label and field
        JLabel nameLabel = new JLabel("Name:");  // Create a label for "Name"
        nameLabel.setForeground(foregroundColor); // Set label text color
        inputPanel.add(nameLabel); // Add the label to the panel
        nameField = new JTextField();  // Create a text field for entering name
        nameField.setBackground(backgroundColor); // Set background color for text field
        nameField.setForeground(foregroundColor); // Set text color for text field
        inputPanel.add(nameField); // Add the name field to the panel

        // Phone label and field
        JLabel phoneLabel = new JLabel("Phone:");  // Create a label for "Phone"
        phoneLabel.setForeground(foregroundColor); // Set label text color
        inputPanel.add(phoneLabel); // Add the label to the panel
        phoneField = new JTextField(); // Create a text field for entering phone number
        phoneField.setBackground(backgroundColor); // Set background color for text field
        phoneField.setForeground(foregroundColor); // Set text color for text field
        inputPanel.add(phoneField); // Add the phone field to the panel

        add(inputPanel, BorderLayout.NORTH); // Add input panel to the top of the window
		
        // Display Area for showing the contacts
        displayArea = new JTextArea();  // Create a text area for displaying contacts
        displayArea.setEditable(false); // Set the text area to non-editable
        displayArea.setBackground(backgroundColor); // Set background color of display area
        displayArea.setForeground(foregroundColor); // Set text color of display area
        JScrollPane scrollPane = new JScrollPane(displayArea); // Add a scroll pane to display area
        scrollPane.getViewport().setBackground(backgroundColor); // Set the background of the scroll pane
        add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the center of the window

        // Button Panel for Add, Search, Delete, Update actions
        JPanel buttonPanel = new JPanel(); // Create a panel for buttons
        buttonPanel.setBackground(backgroundColor); // Set the background color of the panel

        // Create and add action buttons (Add, Search, Delete, Update)
        createButton(buttonPanel, "Add"); // Create and add Add button
        createButton(buttonPanel, "Search"); // Create and add Search button
        createButton(buttonPanel, "Delete"); // Create and add Delete button
        createButton(buttonPanel, "Update"); // Create and add Update button

        add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom of the window
        loadContacts(); // Load existing contacts from the file when the application starts
        setVisible(true); // Make the frame visible
    }
    // Helper method to create buttons with listeners
    private void createButton(JPanel panel, String label) {
        JButton button = new JButton(label); // Create a new button with specified label
        button.setBackground(Color.decode("#322C2B")); // Set background color of button
        button.setForeground(Color.WHITE); // Set text color of button
        button.addActionListener(this); // Attach the action listener for button click events
        panel.add(button); // Add the button to the panel
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); // Get the action command of the button clicked
        switch (command) {  // Based on the button clicked, perform the corresponding action
            case "Add":
                addContact(); // Call method to add a contact
                break;
            case "Search":
                searchContact(); // Call method to search a contact
                break;
            case "Delete":
                deleteContact(); // Call method to delete a contact
                break;
            case "Update":
                updateContact(); // Call method to update a contact
                break;
        }
    }
    // Load contacts from the file and display them in the display area
    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            displayArea.setText(""); // Clear any existing content in the display area
            String line;
            while ((line = reader.readLine()) != null) { // Read the file line by line
                displayArea.append(line + "\n"); // Display each contact in the area
            }
        } catch (IOException e) {
            displayArea.setText("No contacts found."); // Show message if no contacts exist
        }
    }
    // Add a new contact to the file and display it in the display area
    private void addContact() {
        String name = nameField.getText().trim(); // Get the name from the text field
        String phone = phoneField.getText().trim(); // Get the phone number from the text field

        if (!name.isEmpty() && !phone.isEmpty()) { // Check if both name and phone are entered
            // Validate phone number format (must start with "09" and be 10-11 digits long)
            if (phone.startsWith("09") && phone.length() >= 10 && phone.length() <= 11) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                    writer.write(name + "," + phone); // Write the new contact to the file
                    writer.newLine(); // Add a new line after the contact
                    displayArea.append(name + "," + phone + "\n"); // Display the new contact
                    nameField.setText(""); // Clear the name field after adding the contact
                    phoneField.setText(""); // Clear the phone field after adding the contact
                    JOptionPane.showMessageDialog(this, "Contact added successfully."); // Show success message
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error saving contact.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
                }
            } else {
                JOptionPane.showMessageDialog(this, "Phone number must start with '09' and be 10-11 digits long.", "Warning", JOptionPane.WARNING_MESSAGE); // Validate phone number
            }
        } else {
            JOptionPane.showMessageDialog(this, "Name and phone number cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE); // Show warning if fields are empty
        }
    }
    // Search for a contact by name and display the result
    private void searchContact() {
        String name = nameField.getText().trim(); // Get the name to search
        if (!name.isEmpty()) { // Check if the name is not empty
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                displayArea.setText(""); // Clear the display area before showing search results
                String line;
                boolean found = false; // Flag to check if contact is found
                while ((line = reader.readLine()) != null) { // Read each contact line
                    if (line.toLowerCase().contains(name.toLowerCase())) { // Search for name in each contact
                        displayArea.append(line + "\n"); // Display the found contact
                        found = true; // Set the flag to true if contact is found
                    }
                }
                if (!found) {
                    displayArea.setText("Contact not found."); // Show message if no contact was found
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading contacts.", "Error", JOptionPane.ERROR_MESSAGE); // Handle file reading error
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a name to search.", "Warning", JOptionPane.WARNING_MESSAGE); // Show message if name is empty
        }
    }
    // Delete a contact by name
    private void deleteContact() {
        String name = nameField.getText().trim(); // Get the name to delete
        if (!name.isEmpty()) { // Check if the name is not empty
            try {
                File inputFile = new File(filename); // Input file (contacts file)
                File tempFile = new File("temp.txt"); // Temporary file for updating contacts

                BufferedReader reader = new BufferedReader(new FileReader(inputFile)); // Reader to read the original file
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // Writer to write to the temp file

                String line;
                boolean found = false; // Flag to check if the contact is found
                while ((line = reader.readLine()) != null) { // Read each line of the file
                    String[] contact = line.split(",", 2); // Split contact by comma
                    if (contact.length > 0 && contact[0].equalsIgnoreCase(name)) { // If name matches, skip it (delete the contact)
                        found = true; // Set flag to true
                    } else {
                        writer.write(line); // Write the line to the temp file if it's not the contact to be deleted
                        writer.newLine(); // Add a new line after writing
                    }
                }
                reader.close(); // Close the reader
                writer.close(); // Close the writer

                if (found) {
                    if (!inputFile.delete() || !tempFile.renameTo(inputFile)) { // Replace the old file with the new one
                        throw new IOException("Error updating the file."); // Throw error if file update fails
                    }
                    JOptionPane.showMessageDialog(this, "Contact deleted successfully."); // Show success message
                } else {
                    JOptionPane.showMessageDialog(this, "Contact not found."); // Show error if contact not found
                    tempFile.delete(); // Delete the temp file if contact was not found
                }
                loadContacts(); // Reload contacts after deletion
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error deleting contact.", "Error", JOptionPane.ERROR_MESSAGE); // Handle file deletion error
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter the full name to delete.", "Warning", JOptionPane.WARNING_MESSAGE); // Show warning if name is empty
        }
    }
    // Update an existing contact with a new phone number
    private void updateContact() {
        String name = nameField.getText().trim(); // Get the name of the contact to update
        String newPhone = phoneField.getText().trim(); // Get the new phone number

        if (!name.isEmpty() && !newPhone.isEmpty()) { // Check if name and new phone are provided
            if (newPhone.startsWith("09") && newPhone.length() >= 10 && newPhone.length() <= 11) { // Validate the new phone number
                try {
                    File inputFile = new File(filename); // Original contacts file
                    File tempFile = new File("temp.txt"); // Temporary file to store updated contacts
                    BufferedReader reader = new BufferedReader(new FileReader(inputFile)); // Reader for the input file
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // Writer for the temp file

                    String line;
                    boolean found = false; // Flag to check if the contact is found
                    while ((line = reader.readLine()) != null) { // Read each contact line
                        String[] contact = line.split(",", 2); // Split contact into name and phone
                        if (contact.length > 0 && contact[0].equalsIgnoreCase(name)) { // If the contact name matches
                            writer.write(name + "," + newPhone); // Update the phone number
                            writer.newLine(); // Add a newline after the updated contact
                            found = true; // Set the flag to true
                        } else {
                            writer.write(line); // Otherwise, write the unchanged contact
                            writer.newLine(); // Add a newline after the unchanged contact
                        }
                    }
                    reader.close(); // Close the reader
                    writer.close(); // Close the writer
					
                    if (found) {
                        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) { // Replace the old file with the new one
                            throw new IOException("Error updating the file."); // Handle file update error
                        }
                        JOptionPane.showMessageDialog(this, "Contact updated successfully."); // Show success message
                    } else {
                        JOptionPane.showMessageDialog(this, "Contact not found."); // Show message if contact is not found
                        tempFile.delete(); // Delete the temp file if contact was not found
                    }
                    loadContacts(); // Reload the contacts after updating
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error updating contact.", "Error", JOptionPane.ERROR_MESSAGE); // Handle error in updating contact
                }
            } else {
                JOptionPane.showMessageDialog(this, "Phone number must start with '09' and be 10-11 digits long.", "Warning", JOptionPane.WARNING_MESSAGE); // Validate phone number
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter the full name and a new phone number to update.", "Warning", JOptionPane.WARNING_MESSAGE); // Show warning if fields are empty
        }
    }
    // Main method to launch the application
    public static void main(String[] args) {
        new PhonebookSystem(); // Create and display the phonebook system frame
    }
}
