package practice;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
    	setLocationByPlatform(true);
    	getContentPane().setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
    	getContentPane().setBackground(new Color(31, 34, 40));
    	setResizable(false);
        setTitle("Securiti.ai Login");
        setSize(680, 434);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        //------components and styling
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(new Color(255, 255, 255));
        usernameLabel.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
        usernameLabel.setBounds(283, 132, 67, 55);
        usernameField = new JTextField();
        usernameField.setBounds(284, 171, 328, 32);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(new Color(255, 255, 255));
        passwordLabel.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
        passwordLabel.setBounds(283, 197, 67, 55);
        passwordField = new JPasswordField();
        passwordField.setBounds(284, 236, 328, 32);
        JButton loginButton = new JButton("LOGIN");
        loginButton.setForeground(new Color(255, 255, 255));
        loginButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        loginButton.setBounds(283, 301, 328, 55);
        loginButton.setBackground(new Color(27, 166, 221));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String role = validateCredentials(username, password);

            if (role != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "! You are logged in as " + role + ".");
                switch (role) {
                    case "Admin":
                        new AdminFrame().setVisible(true);
                        break;
                    case "Manager":
                        new ManagerFrame().setVisible(true);
                        break;
                    case "Employee":
                        new EmployeeFrame().setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "No frame assigned for this role yet.");
                        return;
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        getContentPane().setLayout(null);

        getContentPane().add(usernameLabel);
        getContentPane().add(usernameField);
        getContentPane().add(passwordLabel);
        getContentPane().add(passwordField);
        getContentPane().add(loginButton);
        
        JLabel companyLogo = new JLabel("");
        companyLogo.setIcon(new ImageIcon(LoginFrame.class.getResource("/images/securiti-gradient.png")));
        companyLogo.setBounds(-162, 57, 436, 352);
        getContentPane().add(companyLogo);
        
        JLabel companyName = new JLabel("securiti");
        companyName.setBounds(283, 65, 197, 66);
        getContentPane().add(companyName);
        companyName.setForeground(new Color(27, 166, 221));
        companyName.setFont(new Font("Bahnschrift", Font.BOLD, 48));
        
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setForeground(new Color(255, 255, 255));
        welcomeLabel.setFont(new Font("Bahnschrift", Font.ITALIC, 16));
        welcomeLabel.setBounds(538, 84, 79, 43);
        getContentPane().add(welcomeLabel);
        
        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(43, 46, 68));
        separator.setBounds(283, 116, 329, 6);
        getContentPane().add(separator);
    }

    //-----login validation
    private String validateCredentials(String username, String password) {
        if (username.equals("admin") && password.equals("admin123")) return "Admin";
        if (username.equals("manager") && password.equals("manager123")) return "Manager";
        if (username.endsWith("@gmail.com") && password.equals("employee123")) return "Employee";
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
