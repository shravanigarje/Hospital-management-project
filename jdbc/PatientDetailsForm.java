package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class PatientDetailsForm extends JFrame {
    private Connection conn;

    public PatientDetailsForm() {
        setTitle("Patient Details");
        setSize(800,600); // Adjusted window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a Panel for the Patient Details Form
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
        formPanel.setPreferredSize(new Dimension(600, 500)); // Adjusted form size
        formPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(20, 20, 20, 20) // Inner padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Patient ID
        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(patientIdLabel, gbc);

        JTextField patientIdField = new JTextField(15); // Increased size
        // Set preferred size
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(patientIdField, gbc);

        // Medical History
        JLabel medicalHistoryLabel = new JLabel("Medical History:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(medicalHistoryLabel, gbc);

        JTextField medicalHistoryArea = new JTextField(15);  ; // Increased size
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(medicalHistoryArea, gbc);

        // Medications
        JLabel medicationsLabel = new JLabel("Medications:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(medicationsLabel, gbc);

        JTextField medicationsArea = new JTextField(15); // Increased size
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(medicationsArea, gbc);

        // Allergies
        JLabel allergiesLabel = new JLabel("Allergies:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(allergiesLabel, gbc);

        JTextField  allergiesArea = new JTextField(15);  // Increased size
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(allergiesArea, gbc);

        // Family Medical History
        JLabel familyHistoryLabel = new JLabel("Family Medical History:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(familyHistoryLabel, gbc);

        JTextField  familyHistoryArea = new JTextField(15);  // Increased size
       
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(familyHistoryArea, gbc);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String patientId = patientIdField.getText().trim();
            if (isValidPatientId(patientId)) {
                savePatientDetails(
                    patientId,
                    medicalHistoryArea.getText(),
                    medicationsArea.getText(),
                    allergiesArea.getText(),
                    familyHistoryArea.getText()
                );
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please enter a valid Patient ID.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
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

    private void savePatientDetails(String patientId, String medicalHistory, String medications, String allergies, String familyHistory) {
        try {
            String query = "INSERT INTO patient_details (patient_id, medical_history, medications, allergies, family_medical_history) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setString(2, medicalHistory);
            pstmt.setString(3, medications);
            pstmt.setString(4, allergies);
            pstmt.setString(5, familyHistory);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient details saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving patient details: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new PatientDetailsForm();
    }
}