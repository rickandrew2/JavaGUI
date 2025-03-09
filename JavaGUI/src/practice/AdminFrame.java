package practice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AdminFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JTextField searchField;
    private JComboBox<String> departmentFilter;
    private JComboBox<String> sortOptions;

    static ArrayList<Employee> employeeList = new ArrayList<>();
    private static ArrayList<String> auditLogs = new ArrayList<>();

    public AdminFrame() {
        setTitle("Admin Panel");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table Columns: ID, Name, Email, Department
        String[] columnNames = {"ID", "Name", "Email", "Department"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setDefaultEditor(Object.class, null); // Read-only

        // 🔍 Search Field
        searchField = new JTextField(15);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterEmployees();
            }
        });

        // 📂 Department Filter
        String[] departments = {"All", "IT", "HR", "Finance", "Operations"};
        departmentFilter = new JComboBox<>(departments);
        departmentFilter.addActionListener(e -> filterEmployees());

        // 🔽 Sorting Options
        String[] sortingOptions = {"Sort by Name", "Sort by Department"};
        sortOptions = new JComboBox<>(sortingOptions);
        sortOptions.addActionListener(e -> filterEmployees());

        // 🛠 Buttons
        JButton addEmployeeButton = new JButton("Add Employee");
        JButton updateEmployeeButton = new JButton("Update Employee");
        JButton deleteEmployeeButton = new JButton("Delete Employee");
        JButton auditLogsButton = new JButton("View Audit Logs");
        JButton logoutButton = new JButton("Logout"); // 🚀 Logout button added

        addEmployeeButton.addActionListener(e -> new AddEmployeeFrame(this).setVisible(true));
        updateEmployeeButton.addActionListener(e -> updateEmployee());
        deleteEmployeeButton.addActionListener(e -> deleteEmployee());
        auditLogsButton.addActionListener(e -> new AuditLogFrame().setVisible(true));

        // 🚀 Logout button action
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose(); // Close AdminFrame
                new LoginFrame().setVisible(true); // Reopen LoginFrame
            }
        });

        // 🔍 Search & Filter Panel
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Filter by Department:"));
        filterPanel.add(departmentFilter);
        filterPanel.add(new JLabel("Sort:"));
        filterPanel.add(sortOptions);

        // 🔘 Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        buttonPanel.add(auditLogsButton);
        buttonPanel.add(logoutButton); // 🚀 Add logout button here

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshEmployeeList();
    }

    public static boolean addEmployee(Employee emp) {
        // Check for duplicates
        for (Employee existingEmp : employeeList) {
            if (existingEmp.getName().equalsIgnoreCase(emp.getName())) {
                JOptionPane.showMessageDialog(null, "Employee name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (existingEmp.getEmail().equalsIgnoreCase(emp.getEmail())) {
                JOptionPane.showMessageDialog(null, "Email is already in use!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // If no duplicates found, add employee
        employeeList.add(emp);
        addAuditLog("Added employee: " + emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")");
        return true;
    }

    public void refreshEmployeeList() {
        filterEmployees();
    }

    public void filterEmployees() {
        tableModel.setRowCount(0);
        String searchQuery = searchField.getText().toLowerCase();
        String selectedDepartment = (String) departmentFilter.getSelectedItem();
        String selectedSort = (String) sortOptions.getSelectedItem();

        List<Employee> filteredList = new ArrayList<>(employeeList);

        // 🔍 Apply search filter
        filteredList.removeIf(emp -> !emp.getName().toLowerCase().contains(searchQuery));

        // 📂 Apply department filter
        if (!selectedDepartment.equals("All")) {
            filteredList.removeIf(emp -> !emp.getDepartment().equals(selectedDepartment));
        }

        // 🔽 Apply sorting
        if (selectedSort.equals("Sort by Name")) {
            filteredList.sort(Comparator.comparing(Employee::getName));
        } else if (selectedSort.equals("Sort by Department")) {
            filteredList.sort(Comparator.comparing(Employee::getDepartment));
        }

        // Populate table
        int id = 1;
        for (Employee emp : filteredList) {
            tableModel.addRow(new Object[]{id++, emp.getName(), emp.getEmail(), emp.getDepartment()});
        }
    }

    // ✏️ **Update Employee**
    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            return;
        }

        Employee emp = employeeList.get(selectedRow);

        String newName = JOptionPane.showInputDialog("Enter New Name:", emp.getName());
        String newEmail = JOptionPane.showInputDialog("Enter New Email:", emp.getEmail());
        String newDepartment = JOptionPane.showInputDialog("Enter New Department:", emp.getDepartment());

        if (newName != null && newEmail != null && newDepartment != null) {
            String oldInfo = emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")";
            emp.setName(newName);
            emp.setEmail(newEmail);
            emp.setDepartment(newDepartment);

            String newInfo = emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")";
            addAuditLog("Updated employee: " + oldInfo + " → " + newInfo);
            refreshEmployeeList();
        }
    }

    // ❌ **Delete Employee**
    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }

        Employee emp = employeeList.remove(selectedRow);
        addAuditLog("Deleted employee: " + emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")");
        refreshEmployeeList();
    }

    // 📜 **Add an audit log**
    public static void addAuditLog(String log) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        auditLogs.add("[" + timestamp + "] " + log);
    }

    public static ArrayList<String> getAuditLogs() {
        return auditLogs;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminFrame().setVisible(true));
    }
}
