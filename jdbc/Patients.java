package jdbc;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Patients extends JFrame {
    private Connection conn;

    public Patients() {
        setTitle("Patients Page");
        setSize(1200, 800); // Increased window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "root");
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        // Create a Panel for the Patients Page with a background image
        JPanel patientsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon("images/background.jpeg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        patientsPanel.setLayout(new BorderLayout());

        // Panel to display images
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false); // Make the panel transparent
        contentPanel.setLayout(new GridLayout(1, 3, 20, 20)); // 1 row, 3 columns

        // Add images and labels for each option
        addOptionButton(contentPanel, "Patient Registration", "images/patientReg.jpeg", "REGISTRATION");
        addOptionButton(contentPanel, "Diagnosis Report", "images/diagnosis.jpeg", "DIAGNOSIS");
        addOptionButton(contentPanel, "Patient Details", "images/patDetails.jpeg", "DETAILS");

        // Add content panel to the patients panel
        patientsPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the patients panel to the frame
        add(patientsPanel);

        // Show patients page window
        setVisible(true);
    }

    private void addOptionButton(JPanel panel, String optionName, String imagePath, String pageName) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(300, 300)); // Set preferred size for the button

        // Load image
        ImageIcon icon = new ImageIcon(imagePath);
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Image not found: " + imagePath);
        } else {
            System.out.println("Image loaded successfully: " + imagePath);
        }
        Image image = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Increased image size
        button.setIcon(new ImageIcon(image));

        // Add label
        JLabel label = new JLabel(optionName, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        button.add(label, BorderLayout.SOUTH);

        // Add action listener
        button.addActionListener(e -> openOptionPage(pageName));

        panel.add(button);
    }

    private void openOptionPage(String pageName) {
        switch (pageName) {
            case "REGISTRATION":
                new PatientRegistrationForm();
                break;
            case "DIAGNOSIS":
                new DiagnosisReportForm();
                break;
            case "DETAILS":
                new PatientDetailsForm();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Page not found!");
        }
    }

    public static void main(String[] args) {
        new Patients();
    }
}