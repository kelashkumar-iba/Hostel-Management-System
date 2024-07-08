package hostelmanagementsystem;

import hostelmanagementsystem.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Admin extends JFrame {

    private JTextField userNameField;
    private JPasswordField passwordField;
    private JLabel securityLabel;

    public Admin() {
        initUI();
    }

    private void initUI() {
        setTitle("Admin Login");
        setSize(900, 660);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        // Top Panel (Header)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(52, 152, 219)); // Set header color
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Use modern font
        titleLabel.setForeground(Color.white);
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel (Form)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setBackground(new Color(52, 73, 94)); // Set background color
        add(centerPanel, BorderLayout.CENTER);

        JLabel userNameLabel = new JLabel("User Name:");
        userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userNameLabel.setForeground(Color.white);
        userNameLabel.setBounds(250, 200, 150, 30);
        centerPanel.add(userNameLabel);

        userNameField = new JTextField();
        userNameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userNameField.setBounds(400, 200, 200, 30);
        centerPanel.add(userNameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.white);
        passwordLabel.setBounds(250, 260, 150, 30);
        centerPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBounds(400, 260, 200, 30);
        centerPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(155, 89, 182)); // Purple color
        loginButton.setForeground(Color.white);
        loginButton.setBounds(400, 320, 100, 40);
        centerPanel.add(loginButton);

        securityLabel = new JLabel("Incorrect username or password");
        securityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        securityLabel.setForeground(Color.red);
        securityLabel.setBounds(300, 380, 300, 30);
        securityLabel.setVisible(false); // Initially hide security message
        centerPanel.add(securityLabel);

        // Bottom Panel (Footer)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(44, 62, 80)); // Dark blue-gray
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(footerPanel, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBackground(new Color(231, 76, 60)); // Red color
        backButton.setForeground(Color.white);
        footerPanel.add(backButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        exitButton.setBackground(new Color(231, 76, 60)); // Red color
        exitButton.setForeground(Color.white);
        footerPanel.add(exitButton);

        // Event Listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String password = new String(passwordField.getPassword());

                if (!userName.isEmpty() && !password.isEmpty()) {
                    if (validateUser(userName, password)) {
                        setVisible(false);
                        // Open ManageStudents or other appropriate screen upon successful login
                        ManageStudents manageStudents = new ManageStudents();
                        manageStudents.setVisible(true);
                    } else {
                        securityLabel.setVisible(true);
                    }
                } else {
                    securityLabel.setVisible(true);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainScreen mainScreen = new MainScreen();
                mainScreen.setVisible(true);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private boolean validateUser(String userName, String password) {
        // Simulated database username and password (replace with actual database logic)
        String dbUserName = "kelash";
        String dbPasswordHash = hashPassword("12345678"); // Simulated hashed password in database

        // Hash the input password for comparison
        String hashedInputPassword = hashPassword(password);

        // Compare hashed password with stored hashed password
        return dbUserName.equals(userName) && dbPasswordHash.equals(hashedInputPassword);
    }

    // Method to hash a password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Handle hashing failure
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Admin::new);
    }
}
