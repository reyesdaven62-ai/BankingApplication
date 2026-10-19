
package org.example;

import java.sql.*;
import java.util.Scanner;

public class Withdraw {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNumber = input.nextLine();

        System.out.print("Enter withdrawal amount: ");
        double withdrawAmount = input.nextDouble();

        if (withdrawAmount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            input.close();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/bankingapps";
        String username = "root";
        String password = "";

        String sql = "UPDATE accounts "
                + "SET balance = balance - ? "
                + "WHERE account_no = ? AND balance >= ?";

        try (
                Connection conn = DriverManager.getConnection(
                        url, username, password
                );
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setDouble(1, withdrawAmount);
            pstmt.setString(2, accountNumber);
            pstmt.setDouble(3, withdrawAmount);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println();
                System.out.println("Withdrawal successful!");
                System.out.println("Account Number: " + accountNumber);
                System.out.printf(
                        "Withdrawn Amount: %.2f%n", withdrawAmount
                );
            } else {
                System.out.println(
                        "Withdrawal failed. Account not found "
                                + "or insufficient balance."
                );
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error processing withdrawal: " + e.getMessage()
            );

        } finally {
            input.close();
        }
    }
}
