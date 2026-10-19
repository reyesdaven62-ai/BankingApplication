package org.example;

import java.sql.*;
import java.util.Scanner;

public class TransactionAccount {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNo = input.nextLine();

        System.out.print("Enter deposit amount: ");
        double amount = input.nextDouble();

        if (amount <= 0) {
            System.out.println("Invalid deposit amount.");
            input.close();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/bankingapps";
        String user = "root";
        String password = ""; // Enter your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // Disable auto-commit to start transaction
            conn.setAutoCommit(false);

            try {
                // 1. Update the account balance in 'accounts' table
                String updateSQL = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";

                int updated;
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                    updateStmt.setDouble(1, amount);
                    updateStmt.setString(2, accountNo);
                    updated = updateStmt.executeUpdate();
                }

                if (updated == 0) {
                    conn.rollback();
                    System.out.println("Account not found.");
                    input.close();
                    return;
                }

                // 2. Insert record into 'transactions' table
                String insertSQL = "INSERT INTO transactions (account_no, transaction_type, amount, description) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                    insertStmt.setString(1, accountNo);
                    insertStmt.setString(2, "DEPOSIT");
                    insertStmt.setDouble(3, amount);
                    insertStmt.setString(4, "Cash deposit");
                    insertStmt.executeUpdate();
                }

                // Commit the changes to both tables
                conn.commit();

                System.out.println();
                System.out.println("==================================");
                System.out.println("       DEPOSIT SUCCESSFUL!");
                System.out.println("==================================");
                System.out.println("Account Number : " + accountNo);
                System.out.printf("Deposited Amount: %.2f%n", amount);
                System.out.println("----------------------------------");

            } catch (SQLException e) {
                // Roll back transaction on error
                conn.rollback();
                throw e;
            } finally {
                // Reset auto-commit mode
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } finally {
            input.close();
        }
    }
}