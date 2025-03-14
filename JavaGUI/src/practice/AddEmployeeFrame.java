package practice;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddEmployeeFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> departmentDropdown;
    private AdminFrame adminFrame;

    public AddEmployeeFrame(AdminFrame adminFrame) {
    	getContentPane().setBackground(new Color(31, 34, 40));
        this.adminFrame = adminFrame;

        setTitle("Add Employee");
        setSize(425, 381);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(109, 26, 106, 33);
        nameLabel.setForeground(new Color(255, 255, 255));
        nameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        nameField = new JTextField();
        nameField.setBounds(109, 56, 246, 33);
        
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(109, 99, 106, 33);
        emailLabel.setForeground(new Color(255, 255, 255));
        emailLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        emailField = new JTextField();
        emailField.setBounds(109, 129, 246, 33);
        
        JLabel departmentLabel = new JLabel("Department");
        departmentLabel.setBounds(109, 172, 106, 33);
        departmentLabel.setForeground(new Color(255, 255, 255));
        departmentLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        String[] departments = {"IT", "HR", "Finance", "Operations"};
        departmentDropdown = new JComboBox<>(departments);
        departmentDropdown.setBounds(109, 202, 246, 33);

        JButton addButton = new JButton("Add Employee");
        addButton.setBounds(108, 265, 247, 33);
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        addButton.setBackground(new Color(27, 166, 221));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String department = (String) departmentDropdown.getSelectedItem();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Email must end with @gmail.com!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 🔴 Check for duplicate name or email before adding
            for (Employee emp : AdminFrame.employeeList) {
                if (emp.getName().equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this, "Employee name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (emp.getEmail().equalsIgnoreCase(email)) {
                    JOptionPane.showMessageDialog(this, "Email is already in use!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // ✅ If no duplicates found, add the employee
            Employee newEmployee = new Employee(name, email, department);
            AdminFrame.addEmployee(newEmployee);

            // 📝 Add to Audit Log
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String logEntry = timestamp + " | ADD | New employee added - Name: " + name + ", Email: " + email + ", Department: " + department;
            AdminFrame.addAuditLog("Add", logEntry);


            // Refresh the admin table
            adminFrame.refreshEmployeeList();

            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        getContentPane().setLayout(null);

        getContentPane().add(nameLabel);
        getContentPane().add(nameField);
        getContentPane().add(emailLabel);
        getContentPane().add(emailField);
        getContentPane().add(departmentLabel);
        getContentPane().add(departmentDropdown);
        getContentPane().add(addButton);
        
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setForeground(new Color(160, 160, 160));
        separator.setBackground(new Color(43, 46, 68));
        separator.setBounds(70, 31, 7, 290);
        getContentPane().add(separator);
    }
}
