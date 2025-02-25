package com.expensetracker; 

import java.awt.BorderLayout; 
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat; 
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;  
import javax.swing.SwingUtilities; 

class Expense {
    int id;
    double amount;
    String description;
    String date;

    public Expense(int id, double amount, String description, String date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date; 
    }

    @Override
    public String toString() {
        return id + ". " + date + " - " + description + ": €" + amount;
    } 
}  

public class ExpenseTracker {
    private JFrame frame;
    private DefaultListModel<Expense> expenseListModel;
    private JList<Expense> expenseList;
    private JTextField amountField, descriptionField, dateField;
    private Connection connection;  

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/expenses";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public ExpenseTracker() {
        connectToDatabase();
        initializeUI();
    }  

    private void initializeUI() {
        frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Amount (€):"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        inputPanel.add(dateField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("View");
        JButton viewByDateButton = new JButton("View by Date");

        addButton.addActionListener(this::addExpense); 
        updateButton.addActionListener(this::updateExpense);
        deleteButton.addActionListener(this::deleteExpense);
        viewButton.addActionListener(this::viewExpenses);
        viewByDateButton.addActionListener(this::viewExpensesByDate); 

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton); 
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(viewByDateButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        expenseListModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseListModel); 
        frame.add(new JScrollPane(expenseList), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void connectToDatabase() {  
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS expenses (id SERIAL PRIMARY KEY, amount DOUBLE PRECISION, description TEXT, date DATE)");
            } 
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
    }  

    private void addExpense(ActionEvent e) { 
        try { 
            double amount = Double.parseDouble(amountField.getText().trim());
            String description = descriptionField.getText().trim();
            String date = dateField.getText().trim();

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO expenses (amount, description, date) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setDouble(1, amount);
            stmt.setString(2, description);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            stmt.executeUpdate(); 
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                expenseListModel.addElement(new Expense(id, amount, description, date));
            } 
            clearFields(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        } 
    } 

    private void updateExpense(ActionEvent e) {
        Expense selected = expenseList.getSelectedValue();
        if (selected != null) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();
                String date = dateField.getText().trim();

                PreparedStatement stmt = connection.prepareStatement("UPDATE expenses SET amount = ?, description = ?, date = ? WHERE id = ?");
                stmt.setDouble(1, amount);
                stmt.setString(2, description);
                stmt.setDate(3, java.sql.Date.valueOf(date)); // FIXED
                stmt.setInt(4, selected.id);
                stmt.executeUpdate(); 

                selected.amount = amount;
                selected.description = description;
                selected.date = date;
                expenseList.repaint();

                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an expense to update.");
        }
    }  

    private void deleteExpense(ActionEvent e) {
        Expense selected = expenseList.getSelectedValue();
        if (selected != null) {
            try {
                PreparedStatement stmt = connection.prepareStatement("DELETE FROM expenses WHERE id = ?");
                stmt.setInt(1, selected.id);
                stmt.executeUpdate();
                expenseListModel.removeElement(selected);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an expense to delete.");
        }
    }

    private void viewExpenses(ActionEvent e) {
        expenseListModel.clear();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM expenses");
            while (rs.next()) {
                expenseListModel.addElement(new Expense(rs.getInt("id"), rs.getDouble("amount"), rs.getString("description"), rs.getString("date")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }                     

    private void viewExpensesByDate(ActionEvent e) {
        expenseListModel.clear();
        String date = dateField.getText().trim();
        double totalAmount = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM expenses WHERE date = ?");
            stmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                double amount = rs.getDouble("amount");
                totalAmount += amount;
                expenseListModel.addElement(new Expense(rs.getInt("id"), amount, rs.getString("description"), rs.getString("date")));
            }
            JOptionPane.showMessageDialog(frame, "Total expenses on " + date + ": €" + totalAmount);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    } 

    private void clearFields() {
        amountField.setText("");
        descriptionField.setText("");
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTracker::new);
    }
} 





