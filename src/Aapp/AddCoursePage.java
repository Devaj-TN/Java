package Aapp;

import DB.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddCoursePage extends JFrame {

    // --- GUI Components ---
    private JTextField courseIdField;
    private JTextField courseNameField;
    private JComboBox<String> teacherComboBox;
    private JButton saveButton;
    private JButton closeButton;

    // A map to store teacher names and their corresponding IDs from the database
    private Map<String, Integer> teacherIdMap = new HashMap<>();

    public AddCoursePage() {
        // --- Frame Setup ---
        setTitle("Add New Course");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Title Panel ---
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Add Course Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel courseIdLabel = new JLabel("Course ID:");
        JLabel courseNameLabel = new JLabel("Course Name:");
        JLabel teacherLabel = new JLabel("Assigned Teacher:");

        courseIdField = new JTextField();
        courseNameField = new JTextField();
        teacherComboBox = new JComboBox<>();

        formPanel.add(courseIdLabel);
        formPanel.add(courseIdField);
        formPanel.add(courseNameLabel);
        formPanel.add(courseNameField);
        formPanel.add(teacherLabel);
        formPanel.add(teacherComboBox);

        add(formPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save Course");
        closeButton = new JButton("Close");
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        saveButton.addActionListener(e -> saveCourse());
        closeButton.addActionListener(e -> dispose());

        // Load teachers into the dropdown when the page is created
        loadTeachers();
    }

    /**
     * Fetches teacher data from the 'users' table and populates the JComboBox.
     */
    private void loadTeachers() {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT id, full_name FROM users ORDER BY full_name";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int teacherId = rs.getInt("id");
                    String teacherName = rs.getString("full_name");
                    // Add the teacher's name to the dropdown
                    teacherComboBox.addItem(teacherName);
                    // Store the name and ID pair in our map for later
                    teacherIdMap.put(teacherName, teacherId);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading teachers: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Handles the logic to save the course data into the database.
     */
    private void saveCourse() {
        String courseId = courseIdField.getText().trim();
        String courseName = courseNameField.getText().trim();
        String selectedTeacherName = (String) teacherComboBox.getSelectedItem();

        // --- Input Validation ---
        if (courseId.isEmpty() || courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course ID and Course Name are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedTeacherName == null) {
            JOptionPane.showMessageDialog(this, "Please select a teacher.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the teacher's ID from our map
        Integer teacherId = teacherIdMap.get(selectedTeacherName);

        // --- Database Logic ---
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "INSERT INTO courses (course_id, course_name, teacher_id) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, courseId);
                pstmt.setString(2, courseName);
                pstmt.setInt(3, teacherId);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                }

            } catch (SQLException ex) {
                 if (ex.getErrorCode() == 1062) { // MySQL "Duplicate entry" error
                    JOptionPane.showMessageDialog(this, "Error: A course with this ID already exists.", "Duplicate Course ID", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                ex.printStackTrace();
            }
        }
    }

    private void clearForm() {
        courseIdField.setText("");
        courseNameField.setText("");
        if (teacherComboBox.getItemCount() > 0) {
            teacherComboBox.setSelectedIndex(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddCoursePage().setVisible(true));
    }
}

