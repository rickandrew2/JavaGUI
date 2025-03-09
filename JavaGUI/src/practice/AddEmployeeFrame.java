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
        this.adminFrame = adminFrame;

        setTitle("Add Employee");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        
        JLabel departmentLabel = new JLabel("Department:");
        String[] departments = {"IT", "HR", "Finance", "Operations"};
        departmentDropdown = new JComboBox<>(departments);

        JButton addButton = new JButton("Add Employee");

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
            AdminFrame.addAuditLog(logEntry);

            // Refresh the admin table
            adminFrame.refreshEmployeeList();

            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(departmentLabel);
        add(departmentDropdown);
        add(new JLabel()); // Spacer
        add(addButton);
    }
}
