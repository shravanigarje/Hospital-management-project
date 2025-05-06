package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Nurses extends JFrame {
    private Connection conn;

    public Nurses() {
        setTitle("Nurses Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
            createNurseTable(); // Ensure the table exists
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a main panel with a split layout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2)); // Split into two equal parts

        // Left side: Background image
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon("images/pharmacy.jpeg"); // Replace with your image path
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        leftPanel.setPreferredSize(new Dimension(500, 600)); // Equal width for left and right panels
        mainPanel.add(leftPanel);

        // Right side: Registration and Login forms
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a tabbed pane for Registration and Login
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Register", createRegistrationPanel());
        tabbedPane.addTab("Login", createLoginPanel());

        // Add the tabbed pane to the right panel
        rightPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add the right panel to the main panel
        mainPanel.add(rightPanel);

        // Add the main panel to the frame
        add(mainPanel);

        // Show the window
        setVisible(true);
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20); // Normal size text field
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20); // Normal size text field
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20); // Normal size password field
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(20); // Normal size password field
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(confirmPasswordField, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
            } else {
                registerNurse(name, email, password);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.anchor = GridBagConstraints.WEST;

        // Email
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20); // Normal size text field
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(emailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20); // Normal size password field
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            } else {
                loginNurse(email, password);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        return panel;
    }

    private void createNurseTable() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS nurses (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL" +
                ")";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Nurses table created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating nurses table: " + e.getMessage());
        }
    }

    private void registerNurse(String name, String email, String password) {
        try {
            String query = "INSERT INTO nurses (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Nurse registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering nurse: " + e.getMessage());
        }
    }

    private void loginNurse(String email, String password) {
        try {
            String query = "SELECT * FROM nurses WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                openNurseDashboard(); // Open the nurse dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login: " + e.getMessage());
        }
    }

    private void openNurseDashboard() {
        // Create a new frame for the nurse dashboard
        JFrame dashboard = new JFrame("Nurse Dashboard");
        dashboard.setSize(1000, 600);
        dashboard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboard.setLocationRelativeTo(null);

        // Create a tabbed pane for the dashboard
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Schedule Appointments", createAppointmentPanel());
        tabbedPane.addTab("Allocate Rooms", createRoomAllocationPanel());
        tabbedPane.addTab("View Patient Details", createPatientDetailsPanel());

        // Add the tabbed pane to the dashboard
        dashboard.add(tabbedPane);

        // Show the dashboard
        dashboard.setVisible(true);
        this.dispose(); // Close the login/registration window
    }

    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.anchor = GridBagConstraints.WEST;

        // Patient ID
        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(patientIdLabel, gbc);

        JTextField patientIdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(patientIdField, gbc);

        // Doctor Name
        JLabel doctorLabel = new JLabel("Doctor Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(doctorLabel, gbc);

        JTextField doctorField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(doctorField, gbc);

        // Appointment Date
        JLabel dateLabel = new JLabel("Appointment Date:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(dateLabel, gbc);

        JTextField dateField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(dateField, gbc);

        // Schedule Button
        JButton scheduleButton = new JButton("Schedule Appointment");
        scheduleButton.addActionListener(e -> {
            String patientId = patientIdField.getText();
            String doctorName = doctorField.getText();
            String date = dateField.getText();

            if (isValidPatientId(patientId)) {
                scheduleAppointment(patientId, doctorName, date);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please enter a valid Patient ID.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(scheduleButton, gbc);

        return panel;
    }

    private JPanel createRoomAllocationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.anchor = GridBagConstraints.WEST;

        // Patient ID
        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(patientIdLabel, gbc);

        JTextField patientIdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(patientIdField, gbc);

        // Room Number
        JLabel roomLabel = new JLabel("Room Number:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(roomLabel, gbc);

        JTextField roomField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(roomField, gbc);

        // Allocate Button
        JButton allocateButton = new JButton("Allocate Room");
        allocateButton.addActionListener(e -> {
            String patientId = patientIdField.getText();
            String roomNumber = roomField.getText();

            if (isValidPatientId(patientId)) {
                allocateRoom(patientId, roomNumber);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please enter a valid Patient ID.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(allocateButton, gbc);

        return panel;
    }

    private JPanel createPatientDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.anchor = GridBagConstraints.WEST;

        // Patient ID
        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(patientIdLabel, gbc);

        JTextField patientIdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(patientIdField, gbc);

        // View Button
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> {
            String patientId = patientIdField.getText();
            if (isValidPatientId(patientId)) {
                viewPatientDetails(patientId);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please enter a valid Patient ID.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(viewButton, gbc);

        return panel;
    }

    private boolean isValidPatientId(String patientId) {
        try {
            String query = "SELECT patient_id FROM patient_registration WHERE patient_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if patient_id exists
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void scheduleAppointment(String patientId, String doctorName, String date) {
        try {
            String query = "INSERT INTO appointments (patient_id, doctor_name, date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setString(2, doctorName);
            pstmt.setString(3, date);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error scheduling appointment: " + e.getMessage());
        }
    }

    private void allocateRoom(String patientId, String roomNumber) {
        try {
            String query = "UPDATE patient_registration SET room_number = ? WHERE patient_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, roomNumber);
            pstmt.setInt(2, Integer.parseInt(patientId));
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Room allocated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to allocate room!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error allocating room: " + e.getMessage());
        }
    }

    private void viewPatientDetails(String patientId) {
        try {
            String query = "SELECT * FROM patient_registration WHERE patient_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                StringBuilder details = new StringBuilder();
                details.append("Patient ID: ").append(rs.getInt("patient_id")).append("\n");
                details.append("Name: ").append(rs.getString("name")).append("\n");
                details.append("Age: ").append(rs.getInt("age")).append("\n");
                details.append("Email: ").append(rs.getString("email")).append("\n");
                details.append("Phone Number: ").append(rs.getString("phone_number")).append("\n");
                details.append("Address: ").append(rs.getString("address")).append("\n");
                details.append("Room Number: ").append(rs.getString("room_number")).append("\n");

                JOptionPane.showMessageDialog(this, details.toString(), "Patient Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching patient details: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Nurses();
    }
}