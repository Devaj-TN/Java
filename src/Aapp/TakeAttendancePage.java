package Aapp;

import DB.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class TakeAttendancePage extends JFrame {

    // --- GUI Components ---
    private JComboBox<String> courseComboBox;
    private JSpinner dateSpinner;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JButton saveButton;

    public TakeAttendancePage() {
        // --- Frame Setup ---
        setTitle("Take Attendance");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Top Panel for Filters ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Select Course and Date"));

        filterPanel.add(new JLabel("Course:"));
        courseComboBox = new JComboBox<>();
        filterPanel.add(courseComboBox);

        filterPanel.add(new JLabel("Date:"));
        // Create a date spinner that defaults to today's date and uses a standard format
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        filterPanel.add(dateSpinner);

        add(filterPanel, BorderLayout.NORTH);

        // --- Table for Students ---
        String[] columnNames = {"Student ID", "Student Name", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentsTable = new JTable(tableModel);
        studentsTable.setRowHeight(25);
        studentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // --- Add a dropdown editor for the 'Status' column ---
        TableColumn statusColumn = studentsTable.getColumnModel().getColumn(2);
        JComboBox<String> statusEditorComboBox = new JComboBox<>(new String[]{"Present", "Absent"});
        statusColumn.setCellEditor(new DefaultCellEditor(statusEditorComboBox));

        add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        // --- Bottom Panel for Save Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save Attendance");
        bottomPanel.add(saveButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        // When a course is selected, load the students for that course
        courseComboBox.addActionListener(e -> loadStudents());
        saveButton.addActionListener(e -> saveAttendance());
        
        // Initial data load for courses
        loadCourses();
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
                // Add course in a "ID - Name" format for clarity
                courseComboBox.addItem(rs.getString("course_id") + " - " + rs.getString("course_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Fetches all students and populates the JTable.
     */
    private void loadStudents() {
        tableModel.setRowCount(0); // Clear existing students from the table
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT student_id, student_name FROM students ORDER BY student_name";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("student_id"));
                row.add(rs.getString("student_name"));
                row.add("Present"); // Default status is 'Present'
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Saves the attendance data from the table into the database.
     */
    private void saveAttendance() {
        String selectedCourseItem = (String) courseComboBox.getSelectedItem();
        if (selectedCourseItem == null) {
            JOptionPane.showMessageDialog(this, "Please select a course.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Extract the course ID from the "ID - Name" string
        String courseId = selectedCourseItem.split(" - ")[0];
        
        // Format the date from the spinner into a SQL-friendly string (yyyy-MM-dd)
        Date selectedDate = (Date) dateSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String attendanceDate = sdf.format(selectedDate);
        
        int rowCount = tableModel.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No students loaded to save attendance for.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = DBConnection.getConnection();
        // This SQL command inserts a new record. If a record with the same primary key
        // (student_id, course_id, attendance_date) already exists, it updates the status instead.
        // This prevents errors and allows for correcting attendance.
        String sql = "INSERT INTO attendance (student_id, course_id, attendance_date, status) VALUES (?, ?, ?, ?)" +
                     "ON DUPLICATE KEY UPDATE status = VALUES(status)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Loop through every row in the JTable
            for (int i = 0; i < rowCount; i++) {
                String studentId = (String) tableModel.getValueAt(i, 0);
                String status = (String) tableModel.getValueAt(i, 2);

                pstmt.setString(1, studentId);
                pstmt.setString(2, courseId);
                pstmt.setString(3, attendanceDate);
                pstmt.setString(4, status);
                pstmt.addBatch(); // Add the statement to a batch for more efficient execution
            }
            pstmt.executeBatch(); // Execute all the saved statements at once
            JOptionPane.showMessageDialog(this, "Attendance records saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving attendance: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Main method for testing this page independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TakeAttendancePage().setVisible(true));
    }
}

