package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class Doctors extends JFrame {
    private Connection conn;
    private JPanel rightPanel;
    private JPanel registrationForm;
    private JPanel loginForm;

    public Doctors() {
        setTitle("Doctor's Portal");
        setSize(1200, 800); // Window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
            System.out.println("Database connection established successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a Panel for the Doctor's Portal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Left Panel for Image
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon("images/newhosp3.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        leftPanel.setPreferredSize(new Dimension(500, 800)); // Left panel size
        leftPanel.setOpaque(false); // Make the panel transparent

        // Right Panel for Forms
        rightPanel = new JPanel(new CardLayout());
        rightPanel.setPreferredSize(new Dimension(700, 800)); // Right panel size
        rightPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(20, 20, 20, 20) // Inner padding
        ));

        // Create Registration Form
        registrationForm = createRegistrationForm();
        rightPanel.add(registrationForm, "REGISTRATION");

        // Create Login Form
        loginForm = createLoginForm();
        rightPanel.add(loginForm, "LOGIN");

        // Add left and right panels to the main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Add the main panel to the frame
        add(mainPanel);

        // Show the form window
        setVisible(true);
    }

    private JPanel createRegistrationForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Title for Registration Form
        JLabel regTitleLabel = new JLabel("Doctor's Registration");
        regTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(regTitleLabel, gbc);

        // Name
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(nameField, gbc);

        // Qualification
        JLabel qualificationLabel = new JLabel("Qualification:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(qualificationLabel, gbc);

        JTextField qualificationField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(qualificationField, gbc);

        // Date of Joining
        JLabel dojLabel = new JLabel("Date of Joining:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(dojLabel, gbc);

        JTextField dojField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(dojField, gbc);

        // Specialization
        JLabel specializationLabel = new JLabel("Specialization:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(specializationLabel, gbc);

        JTextField specializationField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(specializationField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(30);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(passwordField, gbc);

        // Mobile Number
        JLabel mobileLabel = new JLabel("Mobile No:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(mobileLabel, gbc);

        JTextField mobileField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(mobileField, gbc);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(addressLabel, gbc);

        JTextField addressField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(addressField, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String qualification = qualificationField.getText().trim();
            String doj = dojField.getText().trim();
            String specialization = specializationField.getText().trim();
            String password = new String(passwordField.getPassword());
            String mobile = mobileField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || password.isEmpty() || mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            if (saveDoctorDetails(name, qualification, doj, specialization, password, mobile, address)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                clearFormFields(nameField, qualificationField, dojField, specializationField, passwordField, mobileField, addressField);
            } else {
                JOptionPane.showMessageDialog(this, "Error saving details.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(registerButton, gbc);

        // Login Button (to switch to login form)
        JButton loginButton = new JButton("Already Registered? Login Here");
        loginButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) rightPanel.getLayout();
            cardLayout.show(rightPanel, "LOGIN"); // Switch to login form
        });
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(loginButton, gbc);

        return formPanel;
    }

    private JPanel createLoginForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Title for Login Form
        JLabel loginTitleLabel = new JLabel("Doctor's Login");
        loginTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(loginTitleLabel, gbc);

        // Login Name
        JLabel loginNameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(loginNameLabel, gbc);

        JTextField loginNameField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(loginNameField, gbc);

        // Login Password
        JLabel loginPasswordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(loginPasswordLabel, gbc);

        JPasswordField loginPasswordField = new JPasswordField(30);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(loginPasswordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String name = loginNameField.getText().trim();
            String password = new String(loginPasswordField.getPassword());

            if (authenticateDoctor(name, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                openPatientDetailsPage(); // Open the patient details page
                dispose(); // Close the current window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid name or password.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(loginButton, gbc);

        // Back Button (to switch to registration form)
        JButton backButton = new JButton("Back to Registration");
        backButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) rightPanel.getLayout();
            cardLayout.show(rightPanel, "REGISTRATION"); // Switch to registration form
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(backButton, gbc);

        return formPanel;
    }

    private boolean saveDoctorDetails(String name, String qualification, String doj, String specialization, String password, String mobile, String address) {
        try {
            String query = "INSERT INTO doctors (name, qualification, date_of_joining, specialization, password, mobile, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, qualification);
            pstmt.setString(3, doj);
            pstmt.setString(4, specialization);
            pstmt.setString(5, password);
            pstmt.setString(6, mobile);
            pstmt.setString(7, address);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if rows were inserted
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage()); // Show detailed error message
            return false;
        }
    }

    private boolean authenticateDoctor(String name, String password) {
        try {
            String query = "SELECT * FROM doctors WHERE name = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if doctor exists with the given credentials
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openPatientDetailsPage() {
        new PatientDetailsPag(); // Open the patient details page
    }

    private void clearFormFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        new Doctors();
    }
}