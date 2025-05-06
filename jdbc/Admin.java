package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Admin extends JFrame {
    private Connection conn;

    public Admin() {
        setTitle("Admin Authentication");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
            createAdminTable(); // Ensure the table exists
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
                ImageIcon imageIcon = new ImageIcon("images/patBackground3.jpeg"); // Replace with your image path
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
                registerAdmin(name, email, password);
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
                if (loginAdmin(email, password)) {
                    openAdminDashboard(); // Open the admin dashboard
                    dispose(); // Close the login/registration window
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        return panel;
    }

    private void createAdminTable() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS admin (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL" +
                ")";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Admin table created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating admin table: " + e.getMessage());
        }
    }

    private void registerAdmin(String name, String email, String password) {
        try {
            String query = "INSERT INTO admin (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Admin registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering admin: " + e.getMessage());
        }
    }

    private boolean loginAdmin(String email, String password) {
        try {
            String query = "SELECT * FROM admin WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login: " + e.getMessage());
            return false;
        }
    }

    private void openAdminDashboard() {
        JFrame dashboardFrame = new JFrame("Admin Dashboard");
        dashboardFrame.setSize(1200, 800);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setLocationRelativeTo(null);

        // Create a main panel with a tabbed layout
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("View Patient Details", createPatientDetailsPanel());
        tabbedPane.addTab("View Room Allocations", createRoomAllocationsPanel());
        tabbedPane.addTab("Generate Bills", createBillsPanel());
        tabbedPane.addTab("View Doctors", createDoctorsPanel());
        tabbedPane.addTab("Delete Patient", createDeletePatientPanel());

        // Add the tabbed pane to the frame
        dashboardFrame.add(tabbedPane);

        // Show the window
        dashboardFrame.setVisible(true);
    }

    private JPanel createPatientDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a table to display patient details
        JTable patientTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(patientTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch and display patient details
        try {
            String query = "SELECT * FROM patient_registration";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            patientTable.setModel(resultSetToTableModel(rs)); // Use custom method
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching patient details: " + e.getMessage());
        }

        return panel;
    }

    private JPanel createRoomAllocationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a table to display room allocations
        JTable roomTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(roomTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch and display room allocations
        try {
            String query = "SELECT patient_id, name, room_number FROM patient_registration WHERE room_number IS NOT NULL";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            roomTable.setModel(resultSetToTableModel(rs)); // Use custom method
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching room allocations: " + e.getMessage());
        }

        return panel;
    }

    private JPanel createBillsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        // Bill Amount
        JLabel billAmountLabel = new JLabel("Bill Amount:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(billAmountLabel, gbc);

        JTextField billAmountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(billAmountField, gbc);

        // Generate Bill Button
        JButton generateButton = new JButton("Generate Bill");
        generateButton.addActionListener(e -> {
            String patientId = patientIdField.getText();
            String billAmount = billAmountField.getText();

            if (isValidPatientId(patientId)) {
                generateBill(patientId, billAmount);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please enter a valid Patient ID.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(generateButton, gbc);

        return panel;
    }

    private JPanel createDoctorsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a table to display doctor details
        JTable doctorTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch and display doctor details
        try {
            String query = "SELECT * FROM doctors";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            doctorTable.setModel(resultSetToTableModel(rs)); // Use custom method
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching doctor details: " + e.getMessage());
        }

        return panel;
    }

    private JPanel createDeletePatientPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        // Delete Button
        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.addActionListener(e -> {
            String patientId = patientIdField.getText();
            if (isValidPatientId(patientId)) {
                deletePatient(patientId);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please enter a valid Patient ID.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(deleteButton, gbc);

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

    private void generateBill(String patientId, String billAmount) {
        try {
            String query = "INSERT INTO bills (patient_id, bill_amount) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setDouble(2, Double.parseDouble(billAmount));
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Bill generated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating bill: " + e.getMessage());
        }
    }

    private void deletePatient(String patientId) {
        try {
            // Start a transaction
            conn.setAutoCommit(false);

            // Step 1: Delete related records from the diagnosis table
            String deleteDiagnosisQuery = "DELETE FROM diagnosis WHERE patient_id = ?";
            PreparedStatement deleteDiagnosisStmt = conn.prepareStatement(deleteDiagnosisQuery);
            deleteDiagnosisStmt.setInt(1, Integer.parseInt(patientId));
            deleteDiagnosisStmt.executeUpdate();

            // Step 2: Delete the patient from the patient_registration table
            String deletePatientQuery = "DELETE FROM patient_registration WHERE patient_id = ?";
            PreparedStatement deletePatientStmt = conn.prepareStatement(deletePatientQuery);
            deletePatientStmt.setInt(1, Integer.parseInt(patientId));
            int rowsDeleted = deletePatientStmt.executeUpdate();

            // Commit the transaction
            conn.commit();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Patient and related records deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete patient!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback the transaction in case of an error
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage());
        } finally {
            try {
                // Restore auto-commit mode
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // Custom method to convert ResultSet to DefaultTableModel
    private DefaultTableModel resultSetToTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        DefaultTableModel tableModel = new DefaultTableModel();

        // Add column names
        for (int column = 1; column <= columnCount; column++) {
            tableModel.addColumn(metaData.getColumnName(column));
        }

        // Add rows
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            tableModel.addRow(row);
        }

        return tableModel;
    }

    public static void main(String[] args) {
        new Admin();
    }
}