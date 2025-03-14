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
        String[] departments = {"All", "IT", "HR", "Finance", "Operations"};
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

        // 🔧 Panel for Search & Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        FlowLayout flowLayout = (FlowLayout) filterPanel.getLayout();
        flowLayout.setAlignOnBaseline(true);
        filterPanel.setForeground(new Color(255, 255, 255));
        filterPanel.setBackground(new Color(31, 34, 40));
        filterPanel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
        
        JLabel lblNewLabel = new JLabel("      securiti");
        lblNewLabel.setForeground(new Color(27, 166, 221));
        lblNewLabel.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        filterPanel.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("         ");
        filterPanel.add(lblNewLabel_1);
        JLabel label_1 = new JLabel("Search:");
        label_1.setForeground(new Color(255, 255, 255));
        filterPanel.add(label_1);
        filterPanel.add(searchField);
        JLabel label = new JLabel("Filter by Department:");
        label.setForeground(new Color(255, 255, 255));
        filterPanel.add(label);
        filterPanel.add(departmentFilter);
        JLabel label_2 = new JLabel("Sort:");
        label_2.setForeground(new Color(255, 255, 255));
        filterPanel.add(label_2);
        filterPanel.add(sortOptions);

        // 🔘 Panel for Buttons (Audit Logs + Logout)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(auditLogsButton);
        buttonPanel.add(logoutButton);

        // 🏗 Add Components
        getContentPane().add(filterPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(employeeTable), BorderLayout.CENTER);
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

    // 🚪 Logout Method
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close the ManagerFrame
            new LoginFrame().setVisible(true); // Open Login Frame (Replace with your login frame class)
        }
    }
}
