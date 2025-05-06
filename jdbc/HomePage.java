package jdbc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HomePage extends JFrame {
    public HomePage(String role, JFrame parentFrame) {
        setTitle("Home Page - " + role);
        setSize(1200, 800); // Increased window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a Panel for the Home Page
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());

        // Panel to display images
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(2, 3, 20, 20)); // Increased spacing between boxes

        // Add images and labels for each role
        addRoleButton(contentPanel, "Admin", "images/newadmin.png", "Admin");
        addRoleButton(contentPanel, "Patients", "images/newpat.png", "Patients");
        addRoleButton(contentPanel, "Nurses", "images/newnurse.jpeg", "Nurses");
        addRoleButton(contentPanel, "Doctors", "images/newdoctor.png", "Doctors");
        addRoleButton(contentPanel, "Insurance Staff", "images/newinsurance.png", "Insurance Staff");
        addRoleButton(contentPanel, "Pharmacy", "images/new1pharmacy.png","Pharmacy");

        // Add content panel to the home page
        homePanel.add(contentPanel, BorderLayout.CENTER);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            parentFrame.setVisible(true); // Show the login window
            this.dispose(); // Close the home page
        });

        // Add logout button to the home page
        homePanel.add(logoutButton, BorderLayout.SOUTH);

        // Add the home panel to the home page frame
        add(homePanel);

        // Show home page window
        setVisible(true);
    }

    private void addRoleButton(JPanel panel, String roleName, String imagePath, String pageName) {
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

        // Scale the image to fit the button size
        Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH); // Increased image size
        button.setIcon(new ImageIcon(image));

        // Add label
        JLabel label = new JLabel(roleName, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18)); // Increased font size
        button.add(label, BorderLayout.SOUTH);

        // Add action listener
        button.addActionListener(e -> openRolePage(pageName));

        panel.add(button);
    }

    private void openRolePage(String pageName) {
        switch (pageName) {
            case "Admin":
                new Admin();
                break;
            case "Patients":
                new Patients();
                break;
            case "Nurses":
                new Nurses();
                break;
            case "Doctors":
                new Doctors();
                break;
            case "Insurance Staff":
                new InsuranceStaff();
                break;
            case "Pharmacy":
                new Pharmacy();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Page not found!");
        }
    }
}