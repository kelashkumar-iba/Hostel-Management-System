package hostelmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManageStudents extends JFrame {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private Connection con;
    private Statement stmt;

    public ManageStudents() {
        initUI();
        displayStudentData();
    }

    private void initUI() {
        setTitle("Manage Students");
        setSize(900, 660);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(52, 152, 219)); // Set background color similar to Admin page
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JLabel titleLabel = new JLabel("Manage Students");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(52, 73, 94)); // Set darker background for form elements
        centerPanel.setLayout(null); // Use null layout for absolute positioning
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchLabel.setForeground(Color.white);
        searchLabel.setBounds(50, 20, 80, 30);
        centerPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(140, 20, 200, 30);
        centerPanel.add(searchField);

        JButton searchButton = createStyledButton("Search Student", 360);
        centerPanel.add(searchButton);

        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(50, 70, 800, 400);
        centerPanel.add(scrollPane);

        JButton addButton = createStyledButton("Add Student", 50);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        centerPanel.add(addButton);

        JButton editButton = createStyledButton("Edit Student", 200);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    int studentID = (int) studentTable.getValueAt(selectedRow, 0);
                    openEditWindow(studentID);
                }
            }
        });
        centerPanel.add(editButton);

        JButton deleteButton = createStyledButton("Delete Student", 510);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    int studentID = (int) studentTable.getValueAt(selectedRow, 0);
                    deleteStudent(studentID);
                    tableModel.removeRow(selectedRow);
                }
            }
        });
        centerPanel.add(deleteButton);

        JButton backButton = createStyledButton("Back", 700);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Admin adminFrame = new Admin();
                adminFrame.setVisible(true);
            }
        });
        centerPanel.add(backButton);

        setVisible(true);
    }

    private JButton createStyledButton(String text, int xPosition) {
        JButton button = new JButton(text);
        button.setBounds(xPosition, 500, 160, 30); // Adjust Y position to align buttons at the bottom
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(52, 152, 219)); // Blue background
        button.setForeground(Color.white);
        button.setFocusPainted(false); // Remove focus border
        return button;
    }

    private void displayStudentData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students");
            tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Address", "Room Number", "Hostel ID"});
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("student_id"), rs.getString("name"), rs.getString("address"), rs.getInt("room_number"), rs.getInt("hostel_id")});
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidHostelID(int hostelID) {
        boolean isValid = false;
        try {
            String query = "SELECT * FROM hostel WHERE hostel_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, hostelID);
            ResultSet rs = pstmt.executeQuery();
            isValid = rs.next();  // Check if any row exists with the given hostel_id
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    private void addStudent() {
        JFrame addFrame = new JFrame("Add Student");
        addFrame.setSize(600, 400);
        addFrame.setLocationRelativeTo(null);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel addPanel = new JPanel(new GridLayout(0, 2));
        addFrame.add(addPanel);

        addPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        addPanel.add(nameField);

        addPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField();
        addPanel.add(addressField);

        addPanel.add(new JLabel("Room Number:"));
        JTextField roomNumberField = new JTextField();
        addPanel.add(roomNumberField);

        addPanel.add(new JLabel("Hostel ID:"));
        JTextField hostelIDField = new JTextField();
        addPanel.add(hostelIDField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hostelID = Integer.parseInt(hostelIDField.getText());
                if (isValidHostelID(hostelID)) {
                    try {
                        String insertQuery = "INSERT INTO Students (name, address, room_number, hostel_id) VALUES (?, ?, ?, ?)";
                        PreparedStatement pstmt = con.prepareStatement(insertQuery);
                        pstmt.setString(1, nameField.getText());
                        pstmt.setString(2, addressField.getText());
                        pstmt.setInt(3, Integer.parseInt(roomNumberField.getText()));
                        pstmt.setInt(4, hostelID);
                        pstmt.executeUpdate();
                        pstmt.close();
                        tableModel.setRowCount(0);
                        displayStudentData();
                        addFrame.dispose();
                        System.out.println("Student added successfully.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to add student. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Hostel ID. Please provide a valid Hostel ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addPanel.add(addButton);

        addFrame.setVisible(true);
    }

    private void deleteStudent(int studentID) {
        try {
            String deleteQuery = "DELETE FROM Students WHERE student_id = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteQuery);
            pstmt.setInt(1, studentID);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student with ID " + studentID + " deleted successfully.");
            } else {
                System.out.println("No student found with ID " + studentID);
            }
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openEditWindow(int studentID) {
        try {
            String query = "SELECT * FROM Students WHERE student_id = " + studentID;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                JFrame editFrame = new JFrame("Edit Student");
                editFrame.setSize(600, 400);
                editFrame.setLocationRelativeTo(null);
                editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel editPanel = new JPanel(new GridLayout(0, 2));
                editFrame.add(editPanel);

                editPanel.add(new JLabel("Name:"));
                JTextField nameField = new JTextField(rs.getString("name"));
                editPanel.add(nameField);

                editPanel.add(new JLabel("Address:"));
                JTextField addressField = new JTextField(rs.getString("address"));
                editPanel.add(addressField);

                editPanel.add(new JLabel("Room Number:"));
                JTextField roomNumberField = new JTextField(String.valueOf(rs.getInt("room_number")));
                editPanel.add(roomNumberField);

                editPanel.add(new JLabel("Hostel ID:"));
                JTextField hostelIDField = new JTextField(String.valueOf(rs.getInt("hostel_id")));
                editPanel.add(hostelIDField);

                JButton saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int newHostelID = Integer.parseInt(hostelIDField.getText());
                            if (isValidHostelID(newHostelID)) {
                                String updateQuery = "UPDATE Students SET name = ?, address = ?, room_number = ?, hostel_id = ? WHERE student_id = ?";
                                PreparedStatement pstmt = con.prepareStatement(updateQuery);
                                pstmt.setString(1, nameField.getText());
                                pstmt.setString(2, addressField.getText());
                                pstmt.setInt(3, Integer.parseInt(roomNumberField.getText()));
                                pstmt.setInt(4, newHostelID);
                                pstmt.setInt(5, studentID);
                                pstmt.executeUpdate();
                                pstmt.close();
                                tableModel.setRowCount(0);
                                displayStudentData();
                                editFrame.dispose();
                                System.out.println("Student updated successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Hostel ID. Please provide a valid Hostel ID.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to update student. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                editPanel.add(saveButton);

                editFrame.setVisible(true);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ManageStudents().setVisible(true);
    }
}
