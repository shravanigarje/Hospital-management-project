package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class DiagnosisReportForm extends JFrame {
    private Connection conn;

    public DiagnosisReportForm() {
        setTitle("Diagnosis Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a Panel for the Diagnosis Report Form
        JPanel registrationPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon("images/patBackground3.jpeg");
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

        // Patient ID
        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(patientIdLabel, gbc);

        JTextField patientIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(patientIdField, gbc);

        // Diagnosis Date
        JLabel diagnosisDateLabel = new JLabel("Diagnosis Date (YYYY-MM-DD):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(diagnosisDateLabel, gbc);

        JTextField diagnosisDateField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(diagnosisDateField, gbc);

        // Diagnosis Details
        JLabel diagnosisDetailsLabel = new JLabel("Diagnosis Details:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(diagnosisDetailsLabel, gbc);

        JTextArea diagnosisDetailsField = new JTextArea(5, 15);
        diagnosisDetailsField.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(diagnosisDetailsField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(scrollPane, gbc);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String patientId = patientIdField.getText().trim();
            if (isValidPatientId(patientId)) {
                saveDiagnosis(
                    patientId,
                    diagnosisDateField.getText(),
                    diagnosisDetailsField.getText()
                );
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Patient ID. Please try again.");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(saveButton, gbc);

        // Add the form panel to the registration panel
        registrationPanel.add(formPanel);

        // Add the registration panel to the frame
        add(registrationPanel);

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

    private void saveDiagnosis(String patientId, String diagnosisDate, String diagnosisDetails) {
        try {
            String query = "INSERT INTO diagnosis (patient_id, diagnosis_date, diagnosis_details) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setDate(2, java.sql.Date.valueOf(diagnosisDate));
            pstmt.setString(3, diagnosisDetails);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Diagnosis saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving diagnosis: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    public static void main(String[] args) {
        new DiagnosisReportForm().setVisible(true); // For testing
    }
}