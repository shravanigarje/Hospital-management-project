package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class InsuranceStaff extends JFrame {
    private Connection conn;

    public InsuranceStaff() {
        setTitle("Insurance Details");
        setSize(1000, 800); // Window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a Panel for the Insurance Details Form
        JPanel detailsPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon("images/patBackground3.jpeg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        detailsPanel.setOpaque(false); // Make the panel transparent

        // Create a sub-panel for the form with a border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(900, 700)); // Form size
        formPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Inner padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Patient ID
        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(patientIdLabel, gbc);

        JTextField patientIdField = new JTextField(30); // Increased size
        patientIdField.setPreferredSize(new Dimension(600, 30)); // Set preferred size
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(patientIdField, gbc);

        // Policy No
        JLabel policyNoLabel = new JLabel("Policy No:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(policyNoLabel, gbc);

        JTextField policyNoField = new JTextField(30); // Increased size
        policyNoField.setPreferredSize(new Dimension(600, 30)); // Set preferred size
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(policyNoField, gbc);

        // Policy Type
        JLabel policyTypeLabel = new JLabel("Policy Type:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(policyTypeLabel, gbc);

        JTextField policyTypeField = new JTextField(30); // Increased size
        policyTypeField.setPreferredSize(new Dimension(600, 30)); // Set preferred size
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(policyTypeField, gbc);

        // Policy Holder
        JLabel policyHolderLabel = new JLabel("Policy Holder:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(policyHolderLabel, gbc);

        JTextField policyHolderField = new JTextField(30); // Increased size
        policyHolderField.setPreferredSize(new Dimension(600, 30)); // Set preferred size
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(policyHolderField, gbc);

        // Coverage Details
        JLabel coverageDetailsLabel = new JLabel("Coverage Details:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(coverageDetailsLabel, gbc);

        JTextArea coverageDetailsArea = new JTextArea(10, 40); // Increased size
        coverageDetailsArea.setLineWrap(true);
        coverageDetailsArea.setPreferredSize(new Dimension(600, 150)); // Set preferred size
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(coverageDetailsArea, gbc);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String patientId = patientIdField.getText().trim();
            if (isValidPatientId(patientId)) {
                saveInsuranceDetails(
                    patientId,
                    policyNoField.getText(),
                    policyTypeField.getText(),
                    policyHolderField.getText(),
                    coverageDetailsArea.getText()
                );
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please enter a valid Patient ID.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(saveButton, gbc);

        // Add the form panel to the details panel
        detailsPanel.add(formPanel);

        // Add the details panel to the frame
        add(detailsPanel);

        // Show the form window
        setVisible(true);
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

    private void saveInsuranceDetails(String patientId, String policyNo, String policyType, String policyHolder, String coverageDetails) {
        try {
            String query = "INSERT INTO insurance_details (patient_id, policy_no, policy_type, policy_holder, coverage_details) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setString(2, policyNo);
            pstmt.setString(3, policyType);
            pstmt.setString(4, policyHolder);
            pstmt.setString(5, coverageDetails);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Insurance details saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving insurance details: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new InsuranceStaff();
    }
}