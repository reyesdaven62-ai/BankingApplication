
package org.example;

import java.sql.*;
import java.util.Scanner;

public class Transfer {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter your account number: ");
        String fromAccount = input.nextLine();

        System.out.print("Enter recipient account number: ");
        String toAccount = input.nextLine();

        if (fromAccount.equals(toAccount)) {
            System.out.println(
                    "Cannot transfer money to the same account."
            );
            input.close();
            return;
        }

        System.out.print("Enter transfer amount: ");
        double transferAmount = input.nextDouble();

        if (transferAmount <= 0) {
            System.out.println("Invalid transfer amount.");
            input.close();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/bankingapps";
        String username = "root";
        String password = "";

        String checkSql =
                "SELECT balance FROM accounts WHERE account_no = ?";

        String debitSql =
                "UPDATE accounts SET balance = balance - ? "
                        + "WHERE account_no = ? AND balance >= ?";

        String creditSql =
                "UPDATE accounts SET balance = balance + ? "
                        + "WHERE account_no = ?";

        try (Connection conn =
                     DriverManager.getConnection(url, username, password)) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement checkStmt =
                            conn.prepareStatement(checkSql);
                    PreparedStatement debitStmt =
                            conn.prepareStatement(debitSql);
                    PreparedStatement creditStmt =
                            conn.prepareStatement(creditSql)
            ) {

                // Check sender account and balance
                checkStmt.setString(1, fromAccount);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Sender account not found.");
                        conn.rollback();
                        return;
                    }

                    double balance = rs.getDouble("balance");

                    if (balance < transferAmount) {
                        System.out.println(
                                "Insufficient balance in sender account."
                        );
                        conn.rollback();
                        return;
                    }
                }

                // Check recipient account
                checkStmt.setString(1, toAccount);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(
                                "Recipient account not found."
                        );
                        conn.rollback();
                        return;
                    }
                }

                // Deduct from sender
                debitStmt.setDouble(1, transferAmount);
                debitStmt.setString(2, fromAccount);
                debitStmt.setDouble(3, transferAmount);

                int debitRows = debitStmt.executeUpdate();

                if (debitRows == 0) {
                    conn.rollback();
                    System.out.println("Transfer failed.");
                    return;
                }

                // Add to recipient
                creditStmt.setDouble(1, transferAmount);
                creditStmt.setString(2, toAccount);

                int creditRows = creditStmt.executeUpdate();

                if (creditRows == 0) {
                    conn.rollback();
                    System.out.println("Transfer failed.");
                    return;
                }

                // Save both balance changes
                conn.commit();

                System.out.println();
                System.out.println("Transfer successful!");
                System.out.println("Sender Account: " + fromAccount);
                System.out.println("Recipient Account: " + toAccount);
                System.out.printf(
                        "Transferred Amount: %.2f%n",
                        transferAmount
                );

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error processing transfer: " + e.getMessage()
            );

        } finally {
            input.close();
        }
    }
}
