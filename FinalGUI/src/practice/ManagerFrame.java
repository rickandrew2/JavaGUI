package practice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ManagerFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JTextField searchField;
    private JComboBox<String> departmentFilter;
    private JComboBox<String> sortOptions;
    private JButton logoutButton;

    public ManagerFrame() {
        setTitle("Manager Panel");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Table Columns: ID, Name, Email, Department
        String[] columnNames = {"ID", "Name", "Email", "Department"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setDefaultEditor(Object.class, null); // Read-only table

        // 🔎 Search Field
        searchField = new JTextField(15);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterEmployees();
            }
        });

        // 🔽 Department Filter
        String[] departments = {"All", "Administration", "Creative Design", "Customer Success", "Data Science", "DevSecOps", "Development", "Marketing", "Product Management", "Quality Assurance", "Sales", "Security & Compliance"};
        departmentFilter = new JComboBox<>(departments);
        departmentFilter.addActionListener(e -> filterEmployees());

        // 🔄 Sorting Options
        String[] sortingOptions = {"Sort by Name", "Sort by Department"};
        sortOptions = new JComboBox<>(sortingOptions);
        sortOptions.addActionListener(e -> filterEmployees());

        // 📜 "View Audit Logs" Button
        JButton auditLogsButton = new JButton("View Audit Logs");
        auditLogsButton.setForeground(new Color(255, 255, 255));
        auditLogsButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        auditLogsButton.setBackground(new Color(27, 166, 221));
        auditLogsButton.setFocusPainted(false);
        auditLogsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        auditLogsButton.addActionListener(e -> new AuditLogFrame().setVisible(true));

        // 🚪 Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setForeground(new Color(255, 255, 255));
        logoutButton.setBorderPainted(false);
        logoutButton.setBackground(new Color(165, 42, 42));
        logoutButton.addActionListener(e -> logout());

        // 🆕 Update Employee Button
        JButton updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.setForeground(Color.WHITE);
        updateEmployeeButton.setBackground(new Color(27, 166, 221));
        updateEmployeeButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        updateEmployeeButton.setFocusPainted(false);
        updateEmployeeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateEmployeeButton.addActionListener(e -> updateEmployee());

        // 🔧 Panel for Search & Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterPanel.setBackground(new Color(31, 34, 40));
        
        JLabel lblTitle = new JLabel("      Manager Panel");
        lblTitle.setForeground(new Color(27, 166, 221));
        lblTitle.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        filterPanel.add(lblTitle);
        
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Filter by Department:"));
        filterPanel.add(departmentFilter);
        filterPanel.add(new JLabel("Sort:"));
        filterPanel.add(sortOptions);

        // 🔘 Panel for Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(auditLogsButton);
        buttonPanel.add(logoutButton);

        // 🏗 Add Components
        getContentPane().add(filterPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 📌 Load Employee List
        refreshEmployeeList();
    }

    // 🆕 Load Employees
    public void refreshEmployeeList() {
        filterEmployees();
    }

    // 🔎 Search, Filter & Sort Employees
    public void filterEmployees() {
        tableModel.setRowCount(0);
        String searchQuery = searchField.getText().toLowerCase();
        String selectedDepartment = (String) departmentFilter.getSelectedItem();
        String selectedSort = (String) sortOptions.getSelectedItem();

        List<Employee> filteredList = new ArrayList<>(AdminFrame.employeeList);

        // Apply search filter
        filteredList.removeIf(emp -> !emp.getName().toLowerCase().contains(searchQuery));

        // Apply department filter
        if (!selectedDepartment.equals("All")) {
            filteredList.removeIf(emp -> !emp.getDepartment().equals(selectedDepartment));
        }

        // Apply sorting
        if (selectedSort.equals("Sort by Name")) {
            filteredList.sort(Comparator.comparing(Employee::getName));
        } else if (selectedSort.equals("Sort by Department")) {
            filteredList.sort(Comparator.comparing(Employee::getDepartment));
        }

        // Populate the table
        int id = 1;
        for (Employee emp : filteredList) {
            tableModel.addRow(new Object[]{id++, emp.getName(), emp.getEmail(), emp.getDepartment()});
        }
    }

    // 🆕 Update Employee Method
    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get selected employee data
        int employeeID = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String currentDepartment = (String) tableModel.getValueAt(selectedRow, 3);

        // Create input fields
        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JComboBox<String> departmentBox = new JComboBox<>(new String[]{"Administration", "Creative Design", "Customer Success", "Data Science", "DevSecOps", "Development", "Marketing", "Product Management", "Quality Assurance", "Sales", "Security & Compliance"});
        departmentBox.setSelectedItem(currentDepartment);

        // Create panel for input
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Department:"));
        panel.add(departmentBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newDepartment = (String) departmentBox.getSelectedItem();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email cannot be empty.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the employee list
            for (Employee emp : AdminFrame.employeeList) {
                if (emp.getName().equals(currentName) && emp.getEmail().equals(currentEmail)) {
                    emp.setName(newName);
                    emp.setEmail(newEmail);
                    emp.setDepartment(newDepartment);
                    break;
                }
            }

            // Refresh the table
            refreshEmployeeList();
            JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            addAuditLog("Logout", "[Manager] logged out"); // Add audit log entry
            dispose(); // Close the ManagerFrame
            new LoginFrame().setVisible(true); // Open Login Frame
        }
    }

    // 📝 Method to add an entry to the audit log
    private void addAuditLog(String actionType, String description) {
        AdminFrame.addAuditLog(actionType, description);
    }

}
