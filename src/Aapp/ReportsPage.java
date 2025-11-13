package Aapp;

import DB.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsPage extends JFrame {

    // --- GUI Components ---
    private JComboBox<String> studentComboBox;
    private JComboBox<String> courseComboBox;
    private JButton generateButton;
    private JLabel resultLabel;

    public ReportsPage() {
        // --- Frame Setup ---
        setTitle("Generate Attendance Report");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Top Panel for Selections ---
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Student and Course"));
        
        selectionPanel.add(new JLabel("Select Student:"));
        studentComboBox = new JComboBox<>();
        selectionPanel.add(studentComboBox);

        selectionPanel.add(new JLabel("Select Course:"));
        courseComboBox = new JComboBox<>();
        selectionPanel.add(courseComboBox);

        generateButton = new JButton("Generate Report");
        selectionPanel.add(new JLabel()); // Placeholder for alignment
        selectionPanel.add(generateButton);

        add(selectionPanel, BorderLayout.NORTH);

        // --- Center Panel for Displaying the Result ---
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Attendance Percentage"));
        resultLabel = new JLabel("Please select a student and course to generate a report.");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultPanel.add(resultLabel);
        
        add(resultPanel, BorderLayout.CENTER);
        
        // --- Bottom Panel for Close Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton closeButton = new JButton("Close");
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // --- Action Listeners ---
        generateButton.addActionListener(e -> generateReport());
        closeButton.addActionListener(e -> dispose());

        // Load initial data for dropdowns
        loadStudents();
        loadCourses();
    }

    /**
     * Fetches students from the database and populates the student JComboBox.
     */
    private void loadStudents() {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT student_id, student_name FROM students ORDER BY student_name";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                studentComboBox.addItem(rs.getString("student_id") + " - " + rs.getString("student_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Fetches courses from the database and populates the course JComboBox.
     */
    private void loadCourses() {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT course_id, course_name FROM courses ORDER BY course_name";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                courseComboBox.addItem(rs.getString("course_id") + " - " + rs.getString("course_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Calculates and displays the attendance percentage for the selected student and course.
     */
    private void generateReport() {
        String selectedStudentItem = (String) studentComboBox.getSelectedItem();
        String selectedCourseItem = (String) courseComboBox.getSelectedItem();

        if (selectedStudentItem == null || selectedCourseItem == null) {
            JOptionPane.showMessageDialog(this, "Please select both a student and a course.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = selectedStudentItem.split(" - ")[0];
        String courseId = selectedCourseItem.split(" - ")[0];

        Connection conn = DBConnection.getConnection();
        
        try {
            // Query 1: Get the count of 'Present' days
            String presentSql = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course_id = ? AND status = 'Present'";
            int presentCount = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(presentSql)) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, courseId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    presentCount = rs.getInt(1);
                }
            }

            // Query 2: Get the total number of recorded attendance days
            String totalSql = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course_id = ?";
            int totalCount = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(totalSql)) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, courseId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
            }

            // Calculate and display the percentage
            if (totalCount == 0) {
                resultLabel.setText("No attendance records found for this selection.");
            } else {
                double percentage = ((double) presentCount / totalCount) * 100;
                resultLabel.setText(String.format("Attendance: %.2f%% (%d / %d days)", percentage, presentCount, totalCount));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Main method for testing this page independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportsPage().setVisible(true));
    }
}

