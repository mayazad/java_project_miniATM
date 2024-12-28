package project.atm;

import java.sql.*;

public class AccountManager {

    public boolean validateCredentials(String accountNumber, String pin) {
        String query = "SELECT * FROM accounts WHERE account_number = ? AND pin = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public double getBalance(String accountNumber) {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return -1;
    }

    public void updateBalance(String accountNumber, double newBalance) {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Balance updated successfully.");
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public boolean isAccountNumberValid(String accountNumber) {
        String query = "SELECT 1 FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

}
