package com.rasbet.backend.Database;

import java.sql.*;

import com.rasbet.backend.Entities.User;

public class UserDB {

    public static void create_User(User user) {
        try {
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
            Statement stmt = sqLiteJDBC2.getC().createStatement();

            // Create a Wallet
            String query_create_wallet = "INSERT INTO Wallet (Balance)\nVALUES (0)\nRETURNING Wallet_ID;";
            ResultSet rs = stmt.executeQuery(query_create_wallet);
            int wallet_id = rs.getInt("Wallet_ID");

            // Get id of the normal user role TODO
            int role_id = 0;

            // Create a User
            String query_create_user = "INSERT INTO User (Wallet_ID, Email, Password, Role, Name, Surname, Birthday, NIF, CC, Address, Phone)\nVALUES ("
                    + wallet_id + ", "
                    + user.getEmail() + ", "
                    + user.getPassword() + ", "
                    + role_id + ", "
                    + user.getFirstName() + ", "
                    + user.getLastName() + ", "
                    + user.getBirthday() + ", "
                    + user.getNIF() + ", "
                    + user.getCC() + ", "
                    + user.getAddress() + ", "
                    + user.getPhoneNumber()
                    + ");";
            stmt.executeQuery(query_create_user);
            stmt.close();
            rs.close();
            sqLiteJDBC2.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

}
