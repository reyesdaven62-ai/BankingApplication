package org.example;
import java.sql.*;
import java.util.Scanner;

public class CreateAccount {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNumber = input.nextLine();

        System.out.print("Enter account holder name: ");
        String accountHolder = input.nextLine();

        System.out.print("Enter initial deposit: ");
        double initialDeposit = input.nextDouble();

        if (initialDeposit < 0) {
            System.out.println("Invalid initial deposit.");
            input.close();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/bankingapps";
        String username = "root";
        String password = ""; // Enter your MySQL password

        String sql = "INSERT INTO accounts "
                + "(holder_name, account_no, balance) "
                + "VALUES (?, ?, ?)";

        try (
                Connection conn = DriverManager.getConnection(
                        url, username, password
                );
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, accountHolder);
            pstmt.setString(2, accountNumber);
            pstmt.setDouble(3, initialDeposit);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println();
                System.out.println("Account created successfully!");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Account number already exists.");

        } catch (SQLException e) {
            System.out.println("Error creating account: "
                    + e.getMessage());

        } finally {
            input.close();
        }
    }
}