package practice;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Securiti.ai Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Enter username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Enter password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

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

            // 🔴 Auto-detect role based on credentials
            String role = validateCredentials(username, password);

            if (role != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "! You are logged in as " + role + ".");

                // ✅ Open corresponding frame based on detected role
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

                // Close the login frame after successful login
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());  // Empty label for spacing
        add(loginButton);
    }

    /**
     * 🔹 Validates username & password and auto-detects role
     * @return the role if valid, otherwise null
     */
    private String validateCredentials(String username, String password) {
        if (username.equals("admin") && password.equals("admin123")) return "Admin";
        if (username.equals("manager") && password.equals("manager123")) return "Manager";
        if (username.endsWith("@gmail.com") && password.equals("employee123")) return "Employee";
        return null;  // Invalid credentials
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
