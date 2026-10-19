
package org.example;

import java.sql.*;
import java.util.Scanner;

public class Deposit {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNumber = input.nextLine();

        System.out.print("Enter deposit amount: ");
        double depositAmount = input.nextDouble();

        if (depositAmount <= 0) {
            System.out.println(
                    "Invalid deposit amount. Please enter a positive amount."
            );
            input.close();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/bankingapps";
        String username = "root";
        String password = "";

        String sql = "UPDATE accounts "
                + "SET balance = balance + ? "
                + "WHERE account_no = ?";

        try (
                Connection conn = DriverManager.getConnection(
                        url, username, password
                );
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setDouble(1, depositAmount);
            pstmt.setString(2, accountNumber);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println();
                System.out.println("Deposit successful!");
                System.out.println("Account Number: " + accountNumber);
                System.out.printf(
                        "Deposited Amount: %.2f%n", depositAmount
                );
            } else {
                System.out.println(
                        "No account found with the given account number."
                );
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error processing deposit: " + e.getMessage()
            );

        } finally {
            input.close();
        }
    }
}
