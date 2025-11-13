package Aapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import DB.DBConnection;

public class ViewStudentsPage extends JFrame {

    // --- GUI Components ---
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton closeButton;

    public ViewStudentsPage() {
        // --- Frame Setup ---
        setTitle("View All Students");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Title Panel ---
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Student Roster");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Table Setup ---
        // Define column headers for the table
        String[] columnNames = {"Student ID", "Full Name", "Email", "Phone Number"};
        
        // Create a table model that is not editable by the user
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentsTable = new JTable(tableModel);
        studentsTable.setFillsViewportHeight(true); // Makes the table use the entire height of the scroll pane
        studentsTable.setRowHeight(25);
        studentsTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the table to a scroll pane to handle many students
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        refreshButton = new JButton("Refresh Data");
        closeButton = new JButton("Close");
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        refreshButton.addActionListener(e -> loadStudentData());
        closeButton.addActionListener(e -> dispose());
        
        // Load the initial data when the window is created
        loadStudentData();
    }

    /**
     * Fetches student data from the database and populates the JTable.
     */
    private void loadStudentData() {
        // Clear any existing rows from the table to avoid duplication on refresh
        tableModel.setRowCount(0);
        
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT student_id, student_name, email, phone_number FROM students ORDER BY student_name";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                // Loop through the results and add each student as a row in the table
                while (rs.next()) {
                    Vector<String> row = new Vector<>();
                    row.add(rs.getString("student_id"));
                    row.add(rs.getString("student_name"));
                    row.add(rs.getString("email"));
                    row.add(rs.getString("phone_number"));
                    tableModel.addRow(row);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading student data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewStudentsPage().setVisible(true));
    }
}

