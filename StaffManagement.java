package hostelmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StaffManagement extends JFrame {

    private JPanel staffPanel;
    private JTable staffTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton exitButton;
    private JButton editButton;
    private Connection con;

    public StaffManagement() {
        initUI();
        displayStaff();
    }

    private void initUI() {
        setBounds(0, 0, 900, 660);
        setTitle("Staff Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the JFrame on screen

        staffPanel = new JPanel();
        staffPanel.setBackground(new Color(52, 73, 94));
        add(staffPanel);
        staffPanel.setLayout(null);

        backButton = new JButton("Back");
        backButton.setBounds(100, 590, 100, 30);
        backButton.setFont(backButton.getFont().deriveFont(16f));

        exitButton = new JButton("Exit");
        exitButton.setBounds(550, 590, 100, 30);
        exitButton.setFont(exitButton.getFont().deriveFont(16f));

        editButton = new JButton("Edit");
        editButton.setBounds(300, 590, 100, 30);
        editButton.setFont(editButton.getFont().deriveFont(16f));

        staffPanel.add(backButton);
        staffPanel.add(exitButton);
        staffPanel.add(editButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                setVisible(false);
                new MainScreen().setVisible(true);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e1) {
                System.exit(0);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = JOptionPane.showInputDialog(null, "Enter password:", "Password", JOptionPane.PLAIN_MESSAGE);
                if (password != null && !password.isEmpty() && checkAdminPassword(password)) {
                    openEditDialog();
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect password! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    private void displayStaff() {
        tableModel = new DefaultTableModel();
        staffTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBounds(50, 50, 800, 500);
        staffPanel.add(scrollPane);

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Staff");
            // Populate the tableModel with staff data
            tableModel.addColumn("Staff ID");
            tableModel.addColumn("Name");
            tableModel.addColumn("Position");
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("staff_id"), rs.getString("name"), rs.getString("position")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEditDialog() {
        // Get the selected row index
        int selectedRowIndex = staffTable.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the selected staff information
        String staffId = (String) tableModel.getValueAt(selectedRowIndex, 0);
        String name = (String) tableModel.getValueAt(selectedRowIndex, 1);
        String position = (String) tableModel.getValueAt(selectedRowIndex, 2);

        // Create a dialog window for editing staff information
        JDialog editDialog = new JDialog(this, "Edit Staff Information", true);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());

        // Create a table for editing staff information
        DefaultTableModel editTableModel = new DefaultTableModel();
        JTable editTable = new JTable(editTableModel);
        JScrollPane editScrollPane = new JScrollPane(editTable);
        editPanel.add(editScrollPane, BorderLayout.CENTER);

        // Add buttons for saving and canceling edits
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        editPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add editPanel to the dialog window
        editDialog.getContentPane().add(editPanel);
        editDialog.setSize(400, 300);
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);

        // Populate the editTableModel with the selected staff information
        editTableModel.addColumn("Staff ID");
        editTableModel.addColumn("Name");
        editTableModel.addColumn("Position");
        editTableModel.addRow(new Object[]{staffId, name, position});

        // Add ActionListener to saveButton
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save changes to the database
                try {
                    // Establish database connection
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
                    PreparedStatement pstmt = con.prepareStatement("UPDATE Staff SET name = ?, position = ? WHERE staff_id = ?");
                    pstmt.setString(1, (String) editTableModel.getValueAt(0, 1)); // 'name' column
                    pstmt.setString(2, (String) editTableModel.getValueAt(0, 2)); // 'position' column
                    pstmt.setInt(3, Integer.parseInt((String) editTableModel.getValueAt(0, 0))); // 'staff_id' column
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Staff information updated successfully!");
                    editDialog.dispose(); // Close the dialog window
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to update staff information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add ActionListener to cancelButton
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose(); // Close the dialog window without saving changes
            }
        });
    }

    // Method to check admin password against the database
    private boolean checkAdminPassword(String password) {
        boolean isAdmin = false;
        try {
            // Establish database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Admin WHERE password = ?");
            pstmt.setString(1, password);
            ResultSet rs = pstmt.executeQuery();
            isAdmin = rs.next(); // If a row is returned, the password is correct
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return isAdmin;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StaffManagement::new);
    }
}
