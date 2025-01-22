package phonebook;

import javax.swing.*;  
import java.awt.*;      
import java.awt.event.*; 
import java.io.*;       

public class PhonebookSystem extends JFrame implements ActionListener {
    private JTextField nameField, phoneField, groupField;  // Fields for user input (name, phone, group)
    private JTextArea displayArea;                         

    private String filename = "D:/TEST!/phonebook/phonebook.txt"; 

    public PhonebookSystem() {
        setTitle("Phonebook System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLayout(new BorderLayout());

        Color backgroundColor = Color.decode("#322C2B"); 
        Color foregroundColor = Color.WHITE; 

        // Input Panel for name, phone, and group fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2)); 
        inputPanel.setBackground(backgroundColor);
        inputPanel.setForeground(foregroundColor);

        // Name label and field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(foregroundColor);
        inputPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setBackground(backgroundColor);
        nameField.setForeground(foregroundColor);
        inputPanel.add(nameField);

        // Phone label and field
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(foregroundColor);
        inputPanel.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBackground(backgroundColor);
        phoneField.setForeground(foregroundColor);
        inputPanel.add(phoneField);

        // Group label and field
        JLabel groupLabel = new JLabel("Group:");
        groupLabel.setForeground(foregroundColor);
        inputPanel.add(groupLabel);
        groupField = new JTextField();
        groupField.setBackground(backgroundColor);
        groupField.setForeground(foregroundColor);
        inputPanel.add(groupField);

        add(inputPanel, BorderLayout.NORTH); 

        // Display Area for showing the contacts
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(backgroundColor);
        displayArea.setForeground(foregroundColor);
        JScrollPane scrollPane = new JScrollPane(displayArea); 
        scrollPane.getViewport().setBackground(backgroundColor);
        add(scrollPane, BorderLayout.CENTER);

        // Menu bar with File -> Exit option
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close only the PhonebookSystem window
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Button Panel for Add, Search, Delete, Update, Refresh actions
        JPanel buttonPanel = new JPanel(); 
        buttonPanel.setBackground(backgroundColor);

        createButton(buttonPanel, "Add");
        createButton(buttonPanel, "Search");
        createButton(buttonPanel, "Delete");
        createButton(buttonPanel, "Update");
        createButton(buttonPanel, "Refresh");

        add(buttonPanel, BorderLayout.SOUTH); 
        loadContacts(); 
        setVisible(true); 
    }

    // Helper method to create buttons with listeners
    private void createButton(JPanel panel, String label) {
        JButton button = new JButton(label);
        button.setBackground(Color.decode("#322C2B"));
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        panel.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Add":
                addContact();
                break;
            case "Search":
                searchContact();
                break;
            case "Delete":
                deleteContact();
                break;
            case "Update":
                updateContact();
                break;
            case "Refresh":
                loadContacts();
                break;
        }
    }

    // Load contacts from the file and display them in the display area
    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            displayArea.setText(""); 
            String line;
            while ((line = reader.readLine()) != null) {
                displayArea.append(line + "\n");
            }
        } catch (IOException e) {
            displayArea.setText("No contacts found.");
        }
    }

    // Add a new contact to the file and display it
    private void addContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String group = groupField.getText().trim();

        if (!name.isEmpty() && !phone.isEmpty() && !group.isEmpty()) {
            if (phone.startsWith("09") && phone.length() >= 10 && phone.length() <= 11) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                    writer.write(name + " : " + phone + " : " + group); 
                    writer.newLine();
                    displayArea.append(name + " : " + phone + " : " + group + "\n");
                    nameField.setText(""); 
                    phoneField.setText(""); 
                    groupField.setText(""); 
                    JOptionPane.showMessageDialog(this, "Contact added successfully.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error saving contact.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Phone number must start with '09' and be 10-11 digits long.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Name, phone number, and group cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Search for a contact by name and display the result
    private void searchContact() {
        String name = nameField.getText().trim();
        String group = groupField.getText().trim();

        if (!name.isEmpty() || !group.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                displayArea.setText(""); 
                String line;
                boolean found = false; 
                while ((line = reader.readLine()) != null) {
                    // Check if the line contains the name or group
                    if ((!name.isEmpty() && line.toLowerCase().contains(name.toLowerCase())) || 
                        (!group.isEmpty() && line.toLowerCase().contains(group.toLowerCase()))) {
                        displayArea.append(line + "\n");
                        found = true; 
                    }
                }
                if (!found) {
                    displayArea.setText("No contacts found matching the search criteria.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading contacts.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a name or group to search.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Delete a contact by name
    private void deleteContact() {
        String name = nameField.getText().trim();  
        if (!name.isEmpty()) {
            try {
                File tempFile = new File("tempfile.txt");
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String line;
                boolean found = false;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains(name.toLowerCase())) {
                        found = true;
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                reader.close();
                writer.close();

                if (found) {
                    if (new File(filename).delete() && tempFile.renameTo(new File(filename))) {
                        JOptionPane.showMessageDialog(this, "Contact deleted successfully.");
                        loadContacts();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error deleting contact.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Contact not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading contacts.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a name to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Update a contact
	private void updateContact() {
		String name = nameField.getText().trim();
		if (!name.isEmpty()) {
			try {
				File updateFile = new File("updatefile.txt");
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				BufferedWriter writer = new BufferedWriter(new FileWriter(updateFile));

				String line;
				boolean found = false;
				while ((line = reader.readLine()) != null) {
					if (line.toLowerCase().contains(name.toLowerCase())) {
						// Assuming the format is "name : phone : group"
						String[] parts = line.split(" : "); // Split by " : " instead of comma

						if (parts.length == 3) {  // Ensure the split worked properly
							String newName = JOptionPane.showInputDialog(this, "Enter new name:", parts[0]);
							String newPhone = JOptionPane.showInputDialog(this, "Enter new phone number:", parts[1]);
							String newGroup = JOptionPane.showInputDialog(this, "Enter new group:", parts[2]);

							// Validate new values
							if (newName != null && !newName.isEmpty() && newPhone != null && !newPhone.isEmpty() && newGroup != null && !newGroup.isEmpty()) {
								// Update the line with new values
								line = newName + " : " + newPhone + " : " + newGroup;
								found = true;
							}
						}
					}
					writer.write(line);
					writer.newLine();
				}
				reader.close();
				writer.close();

				if (new File(filename).delete() && updateFile.renameTo(new File(filename))) {
					if (found) {
						JOptionPane.showMessageDialog(this, "Contact updated successfully.");
					} else {
						JOptionPane.showMessageDialog(this, "Contact not found.");
					}
					loadContacts(); // Reload the contacts after updating
				} else {
					JOptionPane.showMessageDialog(this, "Error updating contact.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error reading contacts.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please enter a name to update.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}


    public static void main(String[] args) {
        new PhonebookSystem();  
    }
}
