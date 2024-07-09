package hostelmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {

    public MainScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("IBA Hostel Management");
        setSize(900, 660);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        // Top Panel (Header)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(52, 152, 219)); // Set header color
        JLabel titleLabel = new JLabel("Welcome To IBA HOSTEL 18");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Use modern font
        titleLabel.setForeground(Color.white);
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel (Image and Content)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Image on the left side
        ImageIcon hostelImage = new ImageIcon("D:\\Database Project\\iba.png");
        JLabel imageLabel = new JLabel(hostelImage);
        centerPanel.add(imageLabel, BorderLayout.WEST);

        // Content (Text or additional components) on the right side
        JTextArea contentArea = new JTextArea();
        contentArea.setText("Insert your content here...");
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false); // Make the text area read-only
        JScrollPane scrollPane = new JScrollPane(contentArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Left Panel (Menu)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 0, 20)); // Add vertical spacing between buttons
        menuPanel.setBackground(new Color(52, 73, 94)); // Set menu panel color (Dark blue-gray)
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));
        add(menuPanel, BorderLayout.WEST);

        String[] menuItems = {"Home", "Admin", "Records", "Menu", "Rooms", "Staff"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Bold and modern font
            button.setPreferredSize(new Dimension(200, 50)); // Adjust button height
            button.setBackground(new Color(155, 89, 182)); // Set button color (Purple)
            button.setForeground(Color.white); // Set text color
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align text
            menuPanel.add(button);

            // Button Actions
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String buttonText = ((JButton) e.getSource()).getText();
                    handleMenuAction(buttonText);
                }
            });
        }

        // Bottom Panel (Footer)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(44, 62, 80)); // Set footer color (Dark blue-gray)
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center-align components
        add(footerPanel, BorderLayout.SOUTH);

        JLabel footerLabel = new JLabel("<html><p><span style='font-weight: bold; color: yellow;'>Developers:</span> " +
                "<span style='color: yellow;'>Zeeshan Hyd</span>, " +
                "<span style='color: yellow;'>Kelash Kumar</span>, " +
                "<span style='color: yellow;'>Bheesham Kumar</span><br>" +
                "<span style='font-weight: bold;'>Address:</span> Akhuwat Nagar Society, Sukkur, Sindh, Pakistan.<br>" +
                "<span style='font-weight: bold;'>Mobile:</span> +923132769597, +923303141082</p></html>");
        footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Use bold font for footer
        footerLabel.setForeground(Color.white);
        footerPanel.add(footerLabel);

        setVisible(true);
    }

    private void handleMenuAction(String buttonText) {
        switch (buttonText) {
            case "Admin":
                setVisible(false);
                new Admin().setVisible(true);
                break;
            case "Records":
                setVisible(false);
                Student.getStudent().setVisible(true);
                break;
            case "Menu":
                setVisible(false);
                new MessMenu().setVisible(true);
                break;
            case "Rooms":
                setVisible(false);
                SwingUtilities.invokeLater(RoomManagement::new);
                break;
            case "Staff":
                setVisible(false);
                SwingUtilities.invokeLater(StaffManagement::new);
                break;
            default:
                // Handle Home or other buttons
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainScreen::new);
    }
}
