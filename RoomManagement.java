package hostelmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RoomManagement extends JFrame {

    private JPanel roomPanel;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton exitButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private Connection con;

    public RoomManagement() {
        initUI();
        displayRooms();
    }

    private void initUI() {
        setBounds(0, 0, 900, 660);
        setTitle("Room Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the JFrame on screen

        roomPanel = new JPanel();
        roomPanel.setBackground(new Color(52, 73, 94));
        add(roomPanel);
        roomPanel.setLayout(null);

        // Space for picture
        // Please add your picture here

        backButton = new JButton("Back");
        backButton.setBounds(100, 590, 100, 30);
        backButton.setFont(backButton.getFont().deriveFont(16f));

        exitButton = new JButton("Exit");
        exitButton.setBounds(550, 590, 100, 30);
        exitButton.setFont(exitButton.getFont().deriveFont(16f));

        addButton = new JButton("Add");
        addButton.setBounds(200, 590, 100, 30);
        addButton.setFont(addButton.getFont().deriveFont(16f));

        editButton = new JButton("Edit");
        editButton.setBounds(300, 590, 100, 30);
        editButton.setFont(editButton.getFont().deriveFont(16f));

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(400, 590, 100, 30);
        deleteButton.setFont(deleteButton.getFont().deriveFont(16f));

        roomPanel.add(backButton);
        roomPanel.add(exitButton);
        roomPanel.add(addButton);
        roomPanel.add(editButton);
        roomPanel.add(deleteButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                RoomManagement.this.setVisible(false);
                MainScreen obj = new MainScreen();
                obj.setVisible(true);
            }
        });


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e1) {
                System.exit(0);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoom();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRoom();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom();
            }
        });

        setVisible(true);
    }

    private void displayRooms() {
        tableModel = new DefaultTableModel();
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBounds(50, 50, 800, 500);
        roomPanel.add(scrollPane);

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Room");
            // Populate the tableModel with room data
            tableModel.addColumn("Room Number");
            tableModel.addColumn("Hostel ID");
            tableModel.addColumn("Type");
            tableModel.addColumn("Capacity");
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("room_number"), rs.getInt("hostel_id"), rs.getString("type"), rs.getInt("capacity")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoom() {
        JTextField roomNumberField = new JTextField();
        JTextField hostelIdField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField capacityField = new JTextField();

        Object[] message = {
                "Room Number:", roomNumberField,
                "Hostel ID:", hostelIdField,
                "Type:", typeField,
                "Capacity:", capacityField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Room", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                int hostelId = Integer.parseInt(hostelIdField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                String type = typeField.getText();

                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO Room (room_number, hostel_id, type, capacity) VALUES (?, ?, ?, ?)");
                pstmt.setInt(1, roomNumber);
                pstmt.setInt(2, hostelId);
                pstmt.setString(3, type);
                pstmt.setInt(4, capacity);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Room added successfully!");
                displayRooms(); // Refresh the room table
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to add room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editRoom() {
        int selectedRowIndex = roomTable.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField roomNumberField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRowIndex, 0)));
        JTextField hostelIdField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRowIndex, 1)));
        JTextField typeField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRowIndex, 2)));
        JTextField capacityField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRowIndex, 3)));

        Object[] message = {
                "Room Number:", roomNumberField,
                "Hostel ID:", hostelIdField,
                "Type:", typeField,
                "Capacity:", capacityField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Edit Room", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                int hostelId = Integer.parseInt(hostelIdField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                String type = typeField.getText();

                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
                PreparedStatement pstmt = con.prepareStatement("UPDATE Room SET room_number = ?, hostel_id = ?, type = ?, capacity = ? WHERE room_number = ?");
                pstmt.setInt(1, roomNumber);
                pstmt.setInt(2, hostelId);
                pstmt.setString(3, type);
                pstmt.setInt(4, capacity);
                pstmt.setInt(5, (int) tableModel.getValueAt(selectedRowIndex, 0));
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Room updated successfully!");
                displayRooms(); // Refresh the room table
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to update room: " +

                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteRoom() {
        int selectedRowIndex = roomTable.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmDialog = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this room?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirmDialog == JOptionPane.YES_OPTION) {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "Inter=79%");
                PreparedStatement pstmt = con.prepareStatement("DELETE FROM Room WHERE room_number = ?");
                pstmt.setInt(1, (int) tableModel.getValueAt(selectedRowIndex, 0));
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Room deleted successfully!");
                displayRooms(); // Refresh the room table
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to delete room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoomManagement::new);
    }
}
