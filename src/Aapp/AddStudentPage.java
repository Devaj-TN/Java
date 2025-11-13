package Aapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import DB.DBConnection;

public class AddStudentPage extends JFrame {

    // --- GUI Components ---
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton saveButton;
    private JButton closeButton;

    public AddStudentPage() {
        // --- Frame Setup ---
        setTitle("Add New Student");
        setSize(450, 350);
        // Use DISPOSE_ON_CLOSE so that closing this window doesn't exit the entire application
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout(10, 10));

        // --- Title Panel (Top) ---
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Add Student Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Form Panel (Center) ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        // Add some padding around the form
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Field Labels
        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel studentNameLabel = new JLabel("Full Name:");
        JLabel emailLabel = new JLabel("Email Address:");
        JLabel phoneLabel = new JLabel("Phone Number:");

        // Field Inputs
        studentIdField = new JTextField();
        studentNameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();

        // Add labels and fields to the form panel in order
        formPanel.add(studentIdLabel);
        formPanel.add(studentIdField);
        formPanel.add(studentNameLabel);
        formPanel.add(studentNameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);

        add(formPanel, BorderLayout.CENTER);

        // --- Button Panel (Bottom) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save Student");
        closeButton = new JButton("Close");
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners for Buttons ---
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudent();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Closes this "Add Student" window, returning to the dashboard.
                dispose();
            }
        });
    }

    /**
     * Handles the logic to save the student data into the database.
     */
    private void saveStudent() {
        String studentId = studentIdField.getText().trim();
        String studentName = studentNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // --- Input Validation ---
        // Ensure that the most important fields are not empty
        if (studentId.isEmpty() || studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID and Full Name are required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop the method if validation fails
        }

        // --- Database Logic ---
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            // The SQL query to insert a new record into the students table
            String sql = "INSERT INTO students (student_id, student_name, email, phone_number) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set the values for the placeholders (?) in the SQL query
                pstmt.setString(1, studentId);
                pstmt.setString(2, studentName);
                pstmt.setString(3, email);
                pstmt.setString(4, phone);

                // Execute the query to insert the data
                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear the form fields for the next entry
                    clearForm();
                }
            } catch (SQLException ex) {
                // Specifically check for a duplicate primary key error (e.g., same student ID)
                if (ex.getErrorCode() == 1062) { // MySQL error code for "Duplicate entry"
                    JOptionPane.showMessageDialog(this, "Error: A student with this ID already exists.", "Duplicate Student ID", JOptionPane.ERROR_MESSAGE);
                } else {
                    // For any other database-related errors
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helper method to clear all text fields on the form.
     */
    private void clearForm() {
        studentIdField.setText("");
        studentNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    /**
     * Main method to allow running this page independently for testing.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddStudentPage addStudentPage = new AddStudentPage();
            addStudentPage.setVisible(true);
        });
    }
}

