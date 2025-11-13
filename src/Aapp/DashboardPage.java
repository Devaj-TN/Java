package Aapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPage extends JFrame {

    public DashboardPage() {
        setTitle("Dashboard - Attendance Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel headerLabel = new JLabel("Main Menu");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Menu Grid Panel ---
        JPanel menuPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Menu Buttons ---
        String[] buttonLabels = {
            "Take Attendance", "View Attendance",
            "Add Student", "View Students",
            "Add Course", "View Courses",
            "Generate Reports", "Logout"
        };
        
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.setFocusPainted(false);
            button.addActionListener(new MenuButtonListener(label));
            menuPanel.add(button);
        }

        add(menuPanel, BorderLayout.CENTER);
    }

    // --- Action Listener for Menu Buttons ---
    private class MenuButtonListener implements ActionListener {
        private String buttonName;

        public MenuButtonListener(String buttonName) {
            this.buttonName = buttonName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // This is a simple way to handle navigation.
            // In a larger app, you might use a more advanced pattern.
            switch (buttonName) {
                case "Take Attendance":
                    new TakeAttendancePage().setVisible(true);
                    break;
                case "View Attendance":
                    new ViewAttendancePage().setVisible(true);
                    break;
                case "Add Student":
                    new AddStudentPage().setVisible(true);
                    break;
                case "View Students":
                    new ViewStudentsPage().setVisible(true);
                    break;
                case "Add Course":
                     new AddCoursePage().setVisible(true);
                    break;
                case "View Courses":
                     new ViewCoursesPage().setVisible(true);
                    break;
                case "Generate Reports":
                    new ReportsPage().setVisible(true);
                    break;
                case "Logout":
                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        dispose(); // Closes the dashboard window
                        new LoginPage().setVisible(true); // Opens the login page
                    }
                    break;
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardPage().setVisible(true));
    }
}

