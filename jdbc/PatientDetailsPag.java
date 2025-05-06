package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class PatientDetailsPag extends JFrame {
    private Connection conn;

    public PatientDetailsPag() {
        setTitle("Patient Details");
        setSize(1200, 800); // Window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a Panel for the Patient Details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Patient Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        detailsPanel.add(titleLabel, gbc);

        // Table to display patient details
        DefaultTableModel model = new DefaultTableModel();
        JTable patientTable = new JTable(model);
        patientTable.setPreferredScrollableViewportSize(new Dimension(1000, 500));
        JScrollPane scrollPane = new JScrollPane(patientTable);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        detailsPanel.add(scrollPane, gbc);

        // Load patient details
        loadPatientDetails(model);

        // Add the details panel to the frame
        add(detailsPanel);

        // Show the form window
        setVisible(true);
    }

    private void loadPatientDetails(DefaultTableModel model) {
        try {
            String query = "SELECT * FROM patient_details";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Add columns to the table
            model.setColumnIdentifiers(new String[]{"Patient ID", "Medical History", "Medications", "Allergies", "Family Medical History"});

            // Add rows to the table
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("patient_id"),
                    rs.getString("medical_history"),
                    rs.getString("medications"),
                    rs.getString("allergies"),
                    rs.getString("family_medical_history")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient details: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new PatientDetailsPag();
    }
}