package practice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AuditLogFrame extends JFrame {
    private JTable logTable;
    private DefaultTableModel tableModel;

    public AuditLogFrame() {
        setTitle("Audit Logs");
        setSize(700, 300); // Increased frame width
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🏷 Column names
        String[] columnNames = {"Timestamp", "Action Type", "Description"};

        // 📊 Table Model (No editable cells)
        tableModel = new DefaultTableModel(columnNames, 0);
        logTable = new JTable(tableModel);
        logTable.setDefaultEditor(Object.class, null); // Read-only

        // ✅ Adjust column widths
        logTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Timestamp
        logTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Action Type
        logTable.getColumnModel().getColumn(2).setPreferredWidth(400); // Description (Increased size)

        // 🆕 Load Logs into Table
        updateLogs();

        // 📜 Add Table to Scroll Pane
        add(new JScrollPane(logTable), BorderLayout.CENTER);
    }


    private void updateLogs() {
        tableModel.setRowCount(0);
        List<String> logs = AdminFrame.getAuditLogs();
        for (String log : logs) {
            String[] parts = log.split(" \\| ");
            if (parts.length == 3) {
                tableModel.addRow(parts);
            }
        }
    }
}
