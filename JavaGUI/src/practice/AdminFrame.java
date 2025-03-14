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
    	setResizable(false);
        setTitle("Admin Panel");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Table Columns: ID, Name, Email, Department
        String[] columnNames = {"ID", "Name", "Email", "Department"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setDefaultEditor(Object.class, null); // Read-only

        //----search field
        searchField = new JTextField(15);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterEmployees();
            }
        });

        //----dept filter
        String[] departments = {"All", "IT", "HR", "Finance", "Operations"};
        departmentFilter = new JComboBox<>(departments);
        departmentFilter.addActionListener(e -> filterEmployees());

        //----sorting options
        String[] sortingOptions = {"Sort by Name", "Sort by Department"};
        sortOptions = new JComboBox<>(sortingOptions);
        sortOptions.addActionListener(e -> filterEmployees());

        //---buttons
        JButton addEmployeeButton = new JButton("Add Employee");
        JButton updateEmployeeButton = new JButton("Update Employee");
        JButton deleteEmployeeButton = new JButton("Delete Employee");
        JButton auditLogsButton = new JButton("View Audit Logs");
        JButton logoutButton = new JButton("Logout"); 
        logoutButton.setForeground(new Color(255, 255, 255));
        logoutButton.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 10));
        logoutButton.setBorderPainted(false);
        logoutButton.setBackground(new Color(165, 42, 42));
        
        //----add button styling
        addEmployeeButton.setForeground(new Color(255, 255, 255));
        addEmployeeButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        addEmployeeButton.setBackground(new Color(27, 166, 221));
        addEmployeeButton.setFocusPainted(false);
        addEmployeeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //----update button styling
        updateEmployeeButton.setForeground(new Color(255, 255, 255));
        updateEmployeeButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        updateEmployeeButton.setBackground(new Color(27, 166, 221));
        updateEmployeeButton.setFocusPainted(false);
        updateEmployeeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //----delete button styling
        deleteEmployeeButton.setForeground(new Color(255, 255, 255));
        deleteEmployeeButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        deleteEmployeeButton.setBackground(new Color(27, 166, 221));
        deleteEmployeeButton.setFocusPainted(false);
        deleteEmployeeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //----audit logs button styling
        auditLogsButton.setForeground(new Color(255, 255, 255));
        auditLogsButton.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        auditLogsButton.setBackground(new Color(27, 166, 221));
        auditLogsButton.setFocusPainted(false);
        auditLogsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //logout button
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //---button actions
        addEmployeeButton.addActionListener(e -> new AddEmployeeFrame(this).setVisible(true));
        updateEmployeeButton.addActionListener(e -> updateEmployee());
        deleteEmployeeButton.addActionListener(e -> deleteEmployee());
        auditLogsButton.addActionListener(e -> new AuditLogFrame().setVisible(true));

        //---logout button action
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose(); // Close AdminFrame
                new LoginFrame().setVisible(true); // Reopen LoginFrame
            }
        });

        //-----search & filter
        JPanel filterPanel = new JPanel();
        filterPanel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
        filterPanel.setBackground(new Color(31, 34, 40));
        FlowLayout fl_filterPanel = new FlowLayout(FlowLayout.LEFT, 5, 5);
        filterPanel.setLayout(fl_filterPanel);
        
        JLabel lblNewLabel = new JLabel("      securiti");
        lblNewLabel.setForeground(new Color(27, 166, 221));
        lblNewLabel.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        filterPanel.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("         ");
        filterPanel.add(lblNewLabel_1);
        JLabel label = new JLabel("Search:");
        label.setForeground(new Color(255, 255, 255));
        filterPanel.add(label);
        filterPanel.add(searchField);
        JLabel label_1 = new JLabel("Filter by Department:");
        label_1.setForeground(new Color(255, 255, 255));
        filterPanel.add(label_1);
        filterPanel.add(departmentFilter);
        JLabel label_2 = new JLabel("Sort:");
        label_2.setForeground(new Color(255, 255, 255));
        filterPanel.add(label_2);
        filterPanel.add(sortOptions);

        //----button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        buttonPanel.add(auditLogsButton);
        buttonPanel.add(logoutButton);

        getContentPane().add(filterPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setForeground(new Color(255, 255, 255));
        scrollPane.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 10));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        refreshEmployeeList();
    }

    public static boolean addEmployee(Employee emp) {
        //----check for duplicates
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
        addAuditLog("Add", "Added employee: " + emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")");
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

    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            return;
        }

        Employee emp = employeeList.get(selectedRow);
        String oldInfo = emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")";

        String newName = JOptionPane.showInputDialog("Enter New Name:", emp.getName());
        String newEmail = JOptionPane.showInputDialog("Enter New Email:", emp.getEmail());
        String newDepartment = JOptionPane.showInputDialog("Enter New Department:", emp.getDepartment());

        if (newName != null && newEmail != null && newDepartment != null) {
            emp.setName(newName);
            emp.setEmail(newEmail);
            emp.setDepartment(newDepartment);

            String newInfo = emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")";
            addAuditLog("Update", "Updated employee: " + oldInfo + " → " + newInfo);
            refreshEmployeeList();
        }
    }


    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }

        Employee emp = employeeList.get(selectedRow);
        
        // Confirmation dialog
        int choice = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete " + emp.getName() + "?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            employeeList.remove(selectedRow);
            addAuditLog("Delete", "Deleted employee: " + emp.getName() + " (" + emp.getEmail() + ", " + emp.getDepartment() + ")");
            refreshEmployeeList();
        }
    }



    // 📜 **Add an audit log**
    public static void addAuditLog(String actionType, String description) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        auditLogs.add(timestamp + " | " + actionType + " | " + description);
    }


    public static ArrayList<String> getAuditLogs() {
        return auditLogs;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminFrame().setVisible(true));
    }
}
