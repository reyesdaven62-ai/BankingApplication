package org.example;

import java.sql.*;
import java.util.Scanner;

public class TransactionHistory {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNumber = input.nextLine();

        String url = "jdbc:mysql://localhost:3306/bankingapps";
        String username = "root";
        String password = "";

        // Updated to query the 'transactions' table with correct column names
        String sql = "SELECT id, transaction_type, "
                + "amount, description, transaction_date "
                + "FROM transactions "
                + "WHERE account_no = ? "
                + "ORDER BY transaction_date DESC";

        try (
                Connection conn = DriverManager.getConnection(
                        url, username, password
                );
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, accountNumber);

            try (ResultSet rs = pstmt.executeQuery()) {

                System.out.println();
                System.out.println("==================================");
                System.out.println("       TRANSACTION HISTORY");
                System.out.println("==================================");
                System.out.println("Account Number: " + accountNumber);
                System.out.println("----------------------------------");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println(
                            "Transaction ID: "
                                    + rs.getInt("id")
                    );

                    System.out.println(
                            "Type: "
                                    + rs.getString("transaction_type")
                    );

                    System.out.printf(
                            "Amount: %.2f%n",
                            rs.getDouble("amount")
                    );

                    System.out.println(
                            "Description: "
                                    + rs.getString("description")
                    );

                    System.out.println(
                            "Date: "
                                    + rs.getTimestamp("transaction_date")
                    );

                    System.out.println("----------------------------------");
                }

                if (!found) {
                    System.out.println(
                            "No transaction history found."
                    );
                }

            }

        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving transaction history: "
                            + e.getMessage()
            );

        } finally {
            input.close();
        }
    }
}