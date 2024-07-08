package hostelmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;
import java.util.Vector;

public class Student extends JFrame {

    private static final Student std = new Student();

    private int count = 0;

    private String name;
    private String fatherName;
    private int cnic;
    private Date dateOfBirth;
    private String educationLevel;
    private String collegeName;
    private double securityFee;

    private JPanel recordPanel;

    private Student() {
        count++;
        if (count == 1) {
            initUI();
        }
    }

    public static Student getStudent() {
        return std;
    }

    private void initUI() {
        setBounds(0, 0, 900, 660);
        setLocationRelativeTo(null); // Center the JFrame on screen
        setName("mainFrame");
        setTitle("Great Student Hostel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel Registration = new JPanel();
        Registration.setBackground(new Color(52, 73, 94));
        add(Registration);
        Registration.setLayout(null);

        JLabel title = new JLabel();
        title.setBounds(330, 0, 500, 50);
        title.setText("Student Records");
        title.setFont(title.getFont().deriveFont(25f));
        title.setForeground(Color.white);
        Registration.add(title);

        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 100, 30);
        backButton.setFont(backButton.getFont().deriveFont(16f));

        JButton homeButton = new JButton("Home");
        homeButton.setBounds(760, 10, 100, 30);
        homeButton.setFont(homeButton.getFont().deriveFont(16f));

        Registration.add(homeButton);
        Registration.add(backButton);

        recordPanel = new JPanel();
        recordPanel.setLayout(new BorderLayout());
        recordPanel.setBounds(10, 100, 880, 400);
        Registration.add(recordPanel);

        fetchStudentRecords();

        this.setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                Student.this.setVisible(false);
                MainScreen obj = new MainScreen();
                obj.setVisible(true);
            }
        });
    }

    private void fetchStudentRecords() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM student_records";
            ResultSet rs = stmt.executeQuery(query);

            Vector<Vector<String>> data = new Vector<>();
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Record ID");
            columnNames.add("Student ID");
            columnNames.add("Name");
            columnNames.add("Action");
            columnNames.add("Record Date");

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("record_id"));
                row.add(rs.getString("student_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("action"));
                row.add(rs.getString("record_date"));
                data.add(row);
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            recordPanel.removeAll();
            recordPanel.add(scrollPane);
            recordPanel.revalidate();
            recordPanel.repaint();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
        String user = "root";
        String password = "Inter=79%";
        return DriverManager.getConnection(url, user, password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public int getCnic() {
        return cnic;
    }

    public void setCnic(int cnic) {
        this.cnic = cnic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public double getSecurityFee() {
        return securityFee;
    }

    public void setSecurityFee(double securityFee) {
        this.securityFee = securityFee;
    }

    // Header and Footer additions
    public JPanel createHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 152, 219));

        JLabel title = new JLabel("Student Records");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);

        return header;
    }

    public JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(new Color(52, 73, 94));

        JButton backButton = new JButton("Back");
        JButton homeButton = new JButton("Home");

        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        homeButton.setFont(new Font("Arial", Font.PLAIN, 14));

        footer.add(backButton);
        footer.add(homeButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle back button action
                Student.this.setVisible(false);
                MainScreen mainScreen = new MainScreen();
                mainScreen.setVisible(true);
            }
        });

        return footer;
    }

    public void addHeaderAndFooter() {
        JPanel header = createHeader();
        JPanel footer = createFooter();

        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(footer, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        Student student = Student.getStudent();
        student.addHeaderAndFooter();
    }
}
