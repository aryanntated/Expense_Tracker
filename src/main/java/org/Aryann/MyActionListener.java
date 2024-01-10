package org.Atharv1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MyActionListener extends Component implements ActionListener {

    private int userId;  // Add a field to store userId

    public MyActionListener(int userId) {
        this.userId = userId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            if (e.getActionCommand().equals("Add Expenses")) {
                new NewExpense(userId);  // Pass userId to the constructor of NewExpense
            }

            if (e.getActionCommand().equals("Summary")) {
                String[] rs = ExpenseDB.getCategories();
                StringBuilder summary = new StringBuilder();
                summary.append("Summary:\n\n");

                for (String s : rs) {
                    summary.append(s).append(" : ").append(ExpenseDB.totalExpenses(userId, s)).append("\n");
                }

                JOptionPane.showMessageDialog(this, summary.toString(), "Total Expense in each Category ", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (e.getSource() instanceof JComboBox) {
            try {
                HomeWindow.updateTable();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
            HomeWindow.updateTotal();
        }
    }
}
