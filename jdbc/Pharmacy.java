package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class Pharmacy extends JFrame {
    private Connection conn;

    public Pharmacy() {
        setTitle("Pharmacy Portal");
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

        // Create a Panel for the Pharmacy Portal
        JPanel detailsPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon("images/pharmacyBackground.jpeg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        detailsPanel.setOpaque(false); // Make the panel transparent

        // Create a sub-panel for the form with a border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(1100, 700)); // Form size
        formPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white background
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Inner padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Title
        JLabel titleLabel = new JLabel("Pharmacy - Manage Medications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Table to display medications
        DefaultTableModel model = new DefaultTableModel();
        JTable medicationTable = new JTable(model);
        medicationTable.setPreferredScrollableViewportSize(new Dimension(1000, 500));
        JScrollPane scrollPane = new JScrollPane(medicationTable);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(scrollPane, gbc);

        // Load medications
        loadMedications(model);

        // Add the form panel to the details panel
        detailsPanel.add(formPanel);

        // Add the details panel to the frame
        add(detailsPanel);

        // Show the form window
        setVisible(true);
    }

    private void loadMedications(DefaultTableModel model) {
        try {
            String query = "SELECT patient_id, medications FROM patient_details";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Add columns to the table
            model.setColumnIdentifiers(new String[]{"Patient ID", "Medications"});

            // Add rows to the table
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("patient_id"),
                    rs.getString("medications")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading medications: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Pharmacy();
    }
}