package org.Atharv1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ExpenseDB {
    static final Statement stat;

    static {
        try {
            String username = LoginRegisterUI.getLoggedInUsername();
            stat = ConnectDB.connect(username).createStatement();
            stat.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Expenses (
                       ID INT PRIMARY KEY,
                       UserID INT,
                       Name VARCHAR(100),
                       Description VARCHAR(255),
                       Amount DECIMAL(10, 2),
                       Category VARCHAR(50),
                       Date DATE
                    );
                    CREATE TABLE IF NOT EXISTS categories (
                       category_name VARCHAR(255) NOT NULL UNIQUE
                    );
                    INSERT OR IGNORE INTO categories (category_name) VALUES
                        ('All'),
                        ('Housing'),
                        ('Transportation'),
                        ('Utilities'),
                        ('Food'),
                        ('Child Expenses'),
                        ('Health Care'),
                        ('Insurance'),
                        ('Taxes'),
                        ('Entertainment'),
                        ('Miscellaneous');
                    """
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExpenseDB() {

    }

    public static ResultSet 
    getALL() throws SQLException {
        String selectSQL;
        if (HomeWindow.getSort().equals("All")) {
            selectSQL = "SELECT Name, Description, Amount, Category, Date FROM \"expenses\"";
        } else {
            selectSQL = "SELECT Name, Description, Amount, Category, Date FROM \"expenses\" WHERE Category LIKE '" + HomeWindow.getSort() + "'";
        }
        return stat.executeQuery(selectSQL);

    }

    public static String[] getColumns() throws SQLException {
        int count = getALL().getMetaData().getColumnCount();
        String[] columns = new String[count];
        for (int i = 0; i < count; i++) {
            columns[i] = getALL().getMetaData().getColumnName(i + 1);
        }
        return columns;
    }

    public static String[] getCategories() {
        String selectSQL = "SELECT * FROM \"categories\"";
        ArrayList<String> categoryList = new ArrayList<>();
        try {
            ResultSet rs = stat.executeQuery(selectSQL);
            while (rs.next()) {
                categoryList.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryList.toArray(new String[0]);

    }

    public static void insertExpenses(int userId, String name, String description, int amount, String category, String date) {
        String selectSQL = String.format("INSERT INTO Expenses (UserID, Name, Description, Amount, Category, Date) VALUES (%d, \"%s\" ,\"%s\", %d, \"%s\", \"%s\")", userId, name, description, amount, category, date);
        try {
            stat.executeUpdate(selectSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Expense inserted!");
    }
    public static ResultSet getExpensesByUserId(int userId) throws SQLException {
        String selectSQL = "SELECT Name, Description, Amount, Category, Date FROM Expenses WHERE UserID = " + userId;
        return stat.executeQuery(selectSQL);
    }     

    public static String totalExpenses() {
        String selectSQL;
        try {
            if (HomeWindow.getSort().equals("All")) {
                selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\"";
                return "Total Expenses : " + stat.executeQuery(selectSQL).getString(1);
            } else {
                selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\" WHERE Category LIKE '" + HomeWindow.getSort() + "'";
                return HomeWindow.getSort() + " Expenses : " + stat.executeQuery(selectSQL).getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String totalMonthlyExpenses() {
        String selectSQL;
        ResultSet rs;
        if (HomeWindow.getSort().equals("All")) {
            System.out.println("test");
            selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\" GROUP BY strftime('%%Y', date), strftime('%%d', date)";
        } else {
            selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\" WHERE Category LIKE '" + HomeWindow.getSort() + "' GROUP BY strftime('%%Y',date) ,  strftime('%%m', date)";
        }
        try {
            rs = stat.executeQuery(selectSQL);
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "test";
    }

    public static String totalExpenses(int userId, String category) {
        String selectSQL;
        if ("All".equals(category)) {
            selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\" WHERE UserId = " + userId;
        } else {
            selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\" WHERE UserId = " + userId + " AND Category LIKE '" + category + "'";
        }
        try {
            return stat.executeQuery(selectSQL).getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    

    public static String totalExpenses(String sort) {
        String selectSQL;
        try {
            if (sort.equals("All")) {
                selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\"";
                return "Total Expenses : " + stat.executeQuery(selectSQL).getString(1);
            } else {
                selectSQL = "SELECT SUM(\"Amount\") FROM \"expenses\" WHERE Category LIKE '" + sort + "'";
                return stat.executeQuery(selectSQL).getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //     public static ResultSet getExpensesByUserId(int userId) {
    //     String selectSQL = "SELECT * FROM expenses WHERE UserID = ?";
    //     try (PreparedStatement pstmt = ConnectDB.connect().prepareStatement(selectSQL)) {
    //         pstmt.setInt(1, userId);
    //         return pstmt.executeQuery();
    //     } catch (SQLException e) {
    //         throw new RuntimeException(e);
    //     }
    // }
}
