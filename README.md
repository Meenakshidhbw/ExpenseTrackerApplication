# Project : Expense Tracker Application

## Vision: 
The goal of this project is to create an Expense Tracker Application that enables users to manage their personal finances efficiently. The program allows users to add, update, delete, and view expenses, helping them gain better control of their spending habits. 

## FEATURES:
- Add Expenses: Users can add new expenses by entering a description, amount, and date.
- View All Expenses: Displays a complete list of all recorded expenses.
- View Expenses by Date: Allows users to filter and view expenses for a specific date, along with the total amount spent.
- Update Expenses: Users can edit and update details of existing expenses.
- Delete Expenses: Enables users to delete selected expenses from the list and database.
- Auto-filled Date Field: Automatically populates the date field with the current date for quicker entry.
- Data Persistence: All expense records are securely stored in a PostgreSQL database.
- Simple and Intuitive UI: A clean, user-friendly interface built using Java Swing for easy navigation and management.

## TECH STACK:
- Programming Language: Java.
- GUI Framework: Java Swing.
- Database: PostgreSQL.
- JDBC: Used for connecting and interacting with the PostgreSQL database (Java Database Connectivity).
- Build Tool: Apache Maven (for dependency management and project build).

## CONCEPTS USED:
- Object-Oriented Programming (OOP):
•	Encapsulation, inheritance, and polymorphism principles used in Java.
•	Example: The Expense class encapsulates expense data.
- Event Handling:
•	Handling user actions like button clicks through Java’s ActionEvent and ActionListener.
- Java Swing:
•	Building the user interface using components like JFrame, JPanel, JButton, JLabel, JTextField, JList, and JScrollPane.
- Database Integration:
•	Using JDBC to connect to a PostgreSQL database for storing and managing expense records.
- SQL Queries:
•	SQL commands for CRUD operations (Create, Read, Update, Delete) on the expenses table.
- Data Persistence:
•	Storing expenses in a relational database for long-term storage and retrieval.
- MVC Pattern (Implicit):
•	Separation of concerns between UI (Swing components), data (Expense model), and database operations (JDBC).
- List Management:
•	DefaultListModel and JList used for dynamically displaying and managing expenses.
- Date Formatting:
•	Using SimpleDateFormat to format dates for the UI and database operations.
- Exception Handling:
•	Try-catch blocks to manage errors and display user-friendly messages in case of issues (e.g., database connection errors).
- Looping and Conditional Logic:
•	Iterating through database records and conditionally performing operations.

## Conclusion
The Expense Tracker is a simple and easy tool to manage daily expenses. It uses Java and Swing for the user interface and PostgreSQL to store expenses securely. Users can add, view, update, or delete expenses and see their spending for specific dates. This project shows how to combine a user-friendly interface with a database for better financial management.
