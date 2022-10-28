package com.rasbet.backend.Database;

import java.sql.*;

import com.rasbet.backend.Entities.User;

public class UserDB {

    public final static String NORMAL_ROLE = "Normal";
    public final static String SPECIALIST_ROLE = "Specialist";
    public final static String ADMIN_ROLE = "Administrator";

    public static String get_Role(int id) throws SQLException{
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String query = "SELECT * FROM Role WHERE Role_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String role = rs.getString("Name");

        sqLiteJDBC2.closeRS(rs);
        return role;
    }

    public static int get_RoleID(String role)throws SQLException{
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String query = "SELECT * FROM Role WHERE Name=" + SQLiteJDBC2.prepare_string(role) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        int id = rs.getInt("Role_ID");

        sqLiteJDBC2.closeRS(rs);
        return id;
    }

    public static int create_User(User user) throws SQLException {
        // Create a Wallet
        int wallet_id = WalletDB.create_Wallet();
        if (wallet_id < 0) return -1;

        // Get id of the normal user role
        int role_id = get_RoleID(NORMAL_ROLE);

        // Create a User
        String query = "INSERT INTO User (Wallet_ID, Email, Password, Role, Name, Surname, Birthday, NIF, CC, Address, Phone) VALUES ("
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

        try {
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
            sqLiteJDBC2.executeUpdate(query);
            sqLiteJDBC2.close();
        } catch (SQLException e) {
            WalletDB.delete_Wallet(wallet_id);
            throw e;
        }

        return 1;
    }

    public static int get_User(User user) throws SQLException {
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        // Get a user by email
        String query = "SELECT * FROM User WHERE Email=" + SQLiteJDBC2.prepare_string(user.getEmail()) + ";";
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
        if (balance < 0) return -1;
        user.setBalance(balance);

        // Get role
        String role = get_Role(role_id);
        if (role.equals("")) return -1;
        user.setRole(role);

        return 1;
    }

    public static User get_User(int id) throws SQLException {
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        // Get a user by email
        String query = "SELECT * FROM User WHERE User_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        User user = new User(rs.getString("Email"), rs.getString("Password"));
        user.setId(id);
        user.setFirstName(rs.getString("Name"));
        user.setLastName(rs.getString("Surname"));
        user.setAddress(rs.getString("Address"));
        user.setPhoneNumber(rs.getString("Phone"));

        sqLiteJDBC2.closeRS(rs);

        return user;
    }

    public static void update_User(User user) throws SQLException {
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        // Update a user
        String query = "UPDATE User SET "
        + "Email = " + SQLiteJDBC2.prepare_string(user.getEmail()) + ", "
        + "Password = " + SQLiteJDBC2.prepare_string(user.getPassword()) + ", "
        + "Name = " + SQLiteJDBC2.prepare_string(user.getFirstName()) + ", "
        + "Surname = " + SQLiteJDBC2.prepare_string(user.getLastName()) + ", "
        + "Address = " + SQLiteJDBC2.prepare_string(user.getAddress()) + ", "
        + "Phone = " + SQLiteJDBC2.prepare_string(user.getPhoneNumber())
        + " WHERE User_ID=" + user.getId()  + ";";

        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();
    }
}
