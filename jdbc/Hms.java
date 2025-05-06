package jdbc;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class Hms extends JFrame implements ActionListener {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // Components for Login Form
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;

    // Components for Registration Form
    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;

    // CardLayout to switch between login and registration forms
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Background image
    private ImageIcon backgroundImage;

    public Hms() {
        setTitle("Hospital Management System");
        setSize(1200, 800); // Increased window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Load background image
        backgroundImage = new ImageIcon("images/background.jpeg"); // Path to your background image
        if (backgroundImage.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Background image not found!");
        }

        // Initialize CardLayout and Panelovi
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Add Login Form
        cardPanel.add(createLoginForm(), "LOGIN");

        // Add Registration Form
        cardPanel.add(createRegistrationForm(), "REGISTER");

        // Add CardPanel to Frame
        add(cardPanel);

        // Show Login Form by default
        cardLayout.show(cardPanel, "LOGIN");

        setVisible(true);
    }

    private JPanel createLoginForm() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setOpaque(false); // Make the panel transparent
        panel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 50)); // Shift form to the right

        // Create a sub-panel for the login form with a border
        JPanel loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setPreferredSize(new Dimension(400, 300)); // Increased size of the form
        loginFormPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        loginFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(20, 20, 20, 20) // Inner padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginFormPanel.add(usernameLabel, gbc);

        loginUsernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginFormPanel.add(loginUsernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginFormPanel.add(passwordLabel, gbc);

        loginPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginFormPanel.add(loginPasswordField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginFormPanel.add(loginButton, gbc);

        // Switch to Registration Form
        JButton goToRegisterButton = new JButton("Register");
        goToRegisterButton.addActionListener(e -> cardLayout.show(cardPanel, "REGISTER"));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginFormPanel.add(goToRegisterButton, gbc);

        // Add the login form panel to the main panel
        panel.add(loginFormPanel);

        return panel;
    }

    private JPanel createRegistrationForm() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setOpaque(false); // Make the panel transparent
        panel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 50)); // Shift form to the right

        // Create a sub-panel for the registration form with a border
        JPanel regFormPanel = new JPanel(new GridBagLayout());
        regFormPanel.setPreferredSize(new Dimension(400, 400)); // Increased size of the form
        regFormPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        regFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(20, 20, 20, 20) // Inner padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        regFormPanel.add(usernameLabel, gbc);

        regUsernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        regFormPanel.add(regUsernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        regFormPanel.add(passwordLabel, gbc);

        regPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        regFormPanel.add(regPasswordField, gbc);

        // Role Selection
        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        regFormPanel.add(roleLabel, gbc);

        // Updated roles array
        String[] roles = {"Admin", "Doctor", "Nurse", "Patient", "Insurance Staff"};
        roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        gbc.gridy = 2;
        regFormPanel.add(roleComboBox, gbc);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        regFormPanel.add(registerButton, gbc);

        // Switch to Login Form
        JButton goToLoginButton = new JButton("Login");
        goToLoginButton.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        regFormPanel.add(goToLoginButton, gbc);

        // Add the registration form panel to the main panel
        panel.add(regFormPanel);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            loginUser();
        } else if (e.getSource() == registerButton) {
            registerUser();
        }
    }

    private void loginUser() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                // Open Home Page
                new HomePage(rs.getString("role"), this);
                this.dispose(); // Close the login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void registerUser() {
        String username = regUsernameField.getText();
        String password = new String(regPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration Successful!");
            cardLayout.show(cardPanel, "LOGIN");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Hms();
    }
}