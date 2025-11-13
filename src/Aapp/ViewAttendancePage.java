package Aapp;

import DB.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ViewAttendancePage extends JFrame {

    // --- GUI Components ---
    private JComboBox<String> courseComboBox;
    private JSpinner dateSpinner;
    private JButton viewButton;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public ViewAttendancePage() {
        // --- Frame Setup ---
        setTitle("View Attendance Records");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Top Panel for Filters ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Select Course and Date to View"));

        filterPanel.add(new JLabel("Course:"));
        courseComboBox = new JComboBox<>();
        filterPanel.add(courseComboBox);

        filterPanel.add(new JLabel("Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        filterPanel.add(dateSpinner);

        viewButton = new JButton("View Attendance");
        filterPanel.add(viewButton);

        add(filterPanel, BorderLayout.NORTH);

        // --- Table for Displaying Attendance ---
        String[] columnNames = {"Student ID", "Student Name", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make table cells non-editable
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);
        attendanceTable.setRowHeight(25);
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 14));

        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);
        
        // --- Bottom Panel for Close Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton closeButton = new JButton("Close");
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        viewButton.addActionListener(e -> loadAttendanceData());
        closeButton.addActionListener(e -> dispose());
        
        // Load courses into the dropdown when the page is created
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
                courseComboBox.addItem(rs.getString("course_id") + " - " + rs.getString("course_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Fetches and displays attendance data based on the selected course and date.
     */
    private void loadAttendanceData() {
        tableModel.setRowCount(0); // Clear previous results

        String selectedCourseItem = (String) courseComboBox.getSelectedItem();
        if (selectedCourseItem == null) {
            JOptionPane.showMessageDialog(this, "Please select a course.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String courseId = selectedCourseItem.split(" - ")[0];
        
        Date selectedDate = (Date) dateSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String attendanceDate = sdf.format(selectedDate);
        
        Connection conn = DBConnection.getConnection();
        // This SQL query joins the 'attendance' table with the 'students' table
        // to retrieve the student's name along with their attendance status.
        String sql = "SELECT a.student_id, s.student_name, a.status " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "WHERE a.course_id = ? AND a.attendance_date = ? " +
                     "ORDER BY s.student_name";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.setString(2, attendanceDate);
            
            ResultSet rs = pstmt.executeQuery();
            
            boolean foundData = false;
            while (rs.next()) {
                foundData = true;
                Vector<String> row = new Vector<>();
                row.add(rs.getString("student_id"));
                row.add(rs.getString("student_name"));
                row.add(rs.getString("status"));
                tableModel.addRow(row);
            }

            if (!foundData) {
                JOptionPane.showMessageDialog(this, "No attendance records found for the selected course and date.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading attendance data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Main method for testing this page independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAttendancePage().setVisible(true));
    }
}

