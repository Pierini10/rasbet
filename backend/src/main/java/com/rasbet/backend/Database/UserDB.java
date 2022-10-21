package com.rasbet.backend.Database;

import java.sql.*;

import com.rasbet.backend.Entities.User;

public class UserDB {

    public static int create_User(User user) throws SQLException {
        // Create a Wallet
        int wallet_id = WalletDB.create_Wallet();
        if (wallet_id < 0)
            return -1;

        // Get id of the normal user role TODO
        int role_id = 0;

        // Create a User
        String query = "INSERT INTO User (Wallet_ID, Email, Password, Role, Name, Surname, Birthday, NIF, CC, Address, Phone)\nVALUES ("
                + wallet_id + ", "
                + SQLiteJDBC2.prepare_string(user.getEmail()) + ", "
                + SQLiteJDBC2.prepare_string(user.getPassword()) + ", "
                + role_id + ", "
                + SQLiteJDBC2.prepare_string(user.getFirstName()) + ", "
                + SQLiteJDBC2.prepare_string(user.getLastName()) + ", "
                + SQLiteJDBC2.prepare_string(user.getBirthday()) + ", "
                + SQLiteJDBC2.prepare_string(user.getNIF()) + ", "
                + SQLiteJDBC2.prepare_string(user.getCC()) + ", "
                + SQLiteJDBC2.prepare_string(user.getAddress()) + ", "
                + SQLiteJDBC2.prepare_string(user.getPhoneNumber())
                + ");";

        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();

        return 1;
    }

    public static int check_User(User user) throws SQLException {
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        // Get a user by email
        String query = "SELECT * FROM User \nWHERE Email=" + SQLiteJDBC2.prepare_string(user.getEmail()) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        if (!user.getPassword().equals(rs.getString("Password")))
            return -1;

        user.setId(rs.getInt("User_ID"));
        user.setFirstName(rs.getString("Name"));
        user.setLastName(rs.getString("Surname"));
        user.setNIF(rs.getString("NIF"));
        user.setCC(rs.getString("CC"));
        user.setAddress(rs.getString("Address"));
        user.setPhoneNumber(rs.getString("Phone"));
        user.setBirthday(rs.getString("Birthday"));
        int wallet_id = rs.getInt("Wallet_ID");
        int role_id = rs.getInt("Role");

        // Close connections
        sqLiteJDBC2.closeRS(rs);

        // Get Balance
        double balance = WalletDB.get_Balance(wallet_id);
        if (balance < 0)
            return -1;
        user.setBalance(balance);

        // Get role
        String role = "Normal"; // TODO
        if (role.equals(""))
            return -1;
        user.setRole(role);

        return 1;
    }

}
