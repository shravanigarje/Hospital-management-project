package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class PatientRegistrationForm extends JFrame {
    private Connection conn;

    public PatientRegistrationForm() {
        setTitle("Patient Registration");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
            createPatientTable(); // Ensure the table exists
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a Panel for the Patient Registration Form
        JPanel registrationPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon("images/background.jpeg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        registrationPanel.setOpaque(false); // Make the panel transparent

        // Create a sub-panel for the form with a border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(600, 500)); // Increased size of the form
        formPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(20, 20, 20, 20) // Inner padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Shift the form to the right
        gbc.anchor = GridBagConstraints.EAST;

        // Patient ID
        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(patientIdLabel, gbc);

        JTextField patientIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(patientIdField, gbc);

        // Name
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(nameField, gbc);

        // Age
        JLabel ageLabel = new JLabel("Age:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(ageLabel, gbc);

        JTextField ageField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(ageField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(emailField, gbc);

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone Number:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(phoneField, gbc);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(addressLabel, gbc);

        JTextField addressField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(addressField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(passwordField, gbc);

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(confirmPasswordField, gbc);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            savePatientRegistration(
                patientIdField.getText(),
                nameField.getText(),
                ageField.getText(), // Pass age input
                emailField.getText(),
                phoneField.getText(),
                addressField.getText(),
                new String(passwordField.getPassword()),
                new String(confirmPasswordField.getPassword())
            );
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(saveButton, gbc);

        // Add the form panel to the registration panel
        registrationPanel.add(formPanel);

        // Add the registration panel to the frame
        add(registrationPanel);

        // Show the form window
        setVisible(true);
    }

    private void createPatientTable() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS patient_registration (" +
                "patient_id INT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "age INT," + // Add age column
                "email VARCHAR(255) NOT NULL," +
                "phone_number VARCHAR(15) NOT NULL," +
                "address VARCHAR(255)," +
                "password VARCHAR(255) NOT NULL" +
                ")";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating table: " + e.getMessage());
        }
    }

    private void savePatientRegistration(String patientId, String name, String ageInput, String email, String phone, String address, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        try {
            // Handle empty age input
            Integer age = null;
            if (!ageInput.isEmpty()) {
                age = Integer.parseInt(ageInput); // Parse the input
            }

            // Insert into the database
            String query = "INSERT INTO patient_registration (patient_id, name, age, email, phone_number, address, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setString(2, name);
            if (age != null) {
                pstmt.setInt(3, age); // Insert the age if provided
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER); // Insert NULL if age is empty
            }
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setString(6, address);
            pstmt.setString(7, password);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient registration saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving patient registration: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid age format. Please enter a valid number.");
        }
    }

    public static void main(String[] args) {
        new PatientRegistrationForm();
    }
}