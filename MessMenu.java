package hostelmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MessMenu extends JFrame {

    private JPanel menuPanel;
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton exitButton;
    private JButton updateButton;
    private JPasswordField passwordField;
    private Connection con;

    public MessMenu() {
        initUI();
        displayMenu();
    }

    private void initUI() {
        setTitle("Great Student Hostel");
        setSize(900, 660);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(44, 62, 80)); // Set background color
        add(menuPanel);
        menuPanel.setLayout(null);

        backButton = createStyledButton("Back", 100);
        menuPanel.add(backButton);

        exitButton = createStyledButton("Exit", 550);
        menuPanel.add(exitButton);

        updateButton = createStyledButton("Edit", 300);
        menuPanel.add(updateButton);

        JLabel passwordLabel = new JLabel("Admin Password:");
        passwordLabel.setBounds(410, 10, 150, 30);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.white);
        menuPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(560, 10, 150, 30);
        menuPanel.add(passwordField);

        // Center the frame on the screen
        setLocationRelativeTo(null);

        setVisible(true);

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

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                if (checkAdminPassword(password)) {
                    editMenu();
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect password! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JButton createStyledButton(String text, int xPosition) {
        JButton button = new JButton(text);
        button.setBounds(xPosition, 590, 100, 30);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(44, 62, 80)); // Purple color
        button.setForeground(Color.white);
        button.setFocusPainted(false);
        return button;
    }

    private void displayMenu() {
        tableModel = new DefaultTableModel();
        menuTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBounds(50, 50, 800, 500);
        menuPanel.add(scrollPane);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Menu");
            tableModel.addColumn("Menu ID");
            tableModel.addColumn("Day");
            tableModel.addColumn("Item Name");
            tableModel.addColumn("Price");
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("menu_id"), rs.getString("day"), rs.getString("item"), rs.getDouble("price")});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editMenu() {
        JDialog editDialog = new JDialog(this, "Edit Menu", true);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());

        DefaultTableModel editTableModel = new DefaultTableModel();
        JTable editTable = new JTable(editTableModel);
        JScrollPane editScrollPane = new JScrollPane(editTable);
        editPanel.add(editScrollPane, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        editPanel.add(buttonPanel, BorderLayout.SOUTH);

        editDialog.getContentPane().add(editPanel);
        editDialog.setSize(400, 300);

        // Center the dialog on the screen
        editDialog.setLocationRelativeTo(null);

        editDialog.setVisible(true);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object[] rowData = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                rowData[j] = tableModel.getValueAt(i, j);
            }
            editTableModel.addRow(rowData);
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
                    PreparedStatement pstmt = con.prepareStatement("UPDATE Menu SET price = ? WHERE day = ? AND item = ?");
                    for (int row = 0; row < editTableModel.getRowCount(); row++) {
                        String day = (String) editTableModel.getValueAt(row, 1);
                        String itemName = (String) editTableModel.getValueAt(row, 2);
                        Double price = (Double) editTableModel.getValueAt(row, 3);
                        pstmt.setDouble(1, price);
                        pstmt.setString(2, day);
                        pstmt.setString(3, itemName);
                        pstmt.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "Menu updated successfully!");
                    editDialog.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to update menu items: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });
    }

    private boolean checkAdminPassword(String password) {
        boolean isAdmin = false;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Admin WHERE password = ?");
            pstmt.setString(1, password);
            ResultSet rs = pstmt.executeQuery();
            isAdmin = rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return isAdmin;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MessMenu::new);
    }
}
