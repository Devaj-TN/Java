package Aapp;

import DB.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ViewCoursesPage extends JFrame {

    private JTable coursesTable;
    private DefaultTableModel tableModel;

    public ViewCoursesPage() {
        setTitle("View All Courses");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Title Panel ---
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Course Catalog");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columnNames = {"Course ID", "Course Name", "Assigned Teacher"};
        tableModel = new DefaultTableModel(columnNames, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                // This makes the table cells not editable
                return false;
            }
        };
        coursesTable = new JTable(tableModel);
        coursesTable.setFillsViewportHeight(true);
        coursesTable.setRowHeight(25);
        coursesTable.setFont(new Font("Arial", Font.PLAIN, 14));

        add(new JScrollPane(coursesTable), BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton refreshButton = new JButton("Refresh Data");
        JButton closeButton = new JButton("Close");
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        refreshButton.addActionListener(e -> loadCourseData());
        closeButton.addActionListener(e -> dispose());
        
        // Load data initially when the page opens
        loadCourseData();
    }

    /**
     * Fetches course data and the associated teacher's name from the database.
     */
    private void loadCourseData() {
        // Clear existing data before loading new data
        tableModel.setRowCount(0);
        
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            // This SQL query joins the 'courses' table with the 'users' table
            // to get the teacher's full_name based on the teacher_id.
            String sql = "SELECT c.course_id, c.course_name, u.full_name " +
                         "FROM courses c " +
                         "JOIN users u ON c.teacher_id = u.id " +
                         "ORDER BY c.course_name";

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                // Loop through all the results from the query
                while (rs.next()) {
                    Vector<String> row = new Vector<>();
                    row.add(rs.getString("course_id"));
                    row.add(rs.getString("course_name"));
                    row.add(rs.getString("full_name")); // Teacher's name from the JOIN
                    tableModel.addRow(row);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading course data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Main method to allow running this page by itself for testing.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewCoursesPage().setVisible(true));
    }
}

