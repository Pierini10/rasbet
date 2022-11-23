package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.rasbet.backend.Entities.User;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

public class UserDB {

    public final static String NORMAL_ROLE = "Normal";
    public final static String SPECIALIST_ROLE = "Specialist";
    public final static String ADMIN_ROLE = "Administrator";

    public static String get_Role(int id) throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT * FROM Role WHERE Role_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String role = rs.getString("Name");

        sqLiteJDBC2.closeRS(rs);
        return role;
    }

    public static int get_RoleID(String role) throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT * FROM Role WHERE Name=" + SQLiteJDBC.prepare_string(role) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        int id = rs.getInt("Role_ID");

        sqLiteJDBC2.closeRS(rs);
        return id;
    }

    public static void assert_is_Specialist(int user_id) throws SQLException, NoAuthorizationException {
        // Create a connection
        User requestUser = get_User(user_id);
        if (requestUser == null || !requestUser.getRole().equals(SPECIALIST_ROLE))
            throw new NoAuthorizationException("Request is not made by specialist!!");
    }

    public static void assert_is_Normal(int user_id) throws SQLException, NoAuthorizationException {
        // Create a connection
        User requestUser = get_User(user_id);
        if (requestUser == null || !requestUser.getRole().equals(NORMAL_ROLE))
            throw new NoAuthorizationException("Request is not made by a normal user!!");
    }

    public static void assert_is_Administrator(int user_id) throws SQLException, NoAuthorizationException {
        // Create a connection
        User requestUser = get_User(user_id);
        if (requestUser == null || !requestUser.getRole().equals(ADMIN_ROLE))
            throw new NoAuthorizationException("Request is not made by Administrator!!");
    }

    public static int create_User(User user, String userRequestRole) throws SQLException, NoAuthorizationException {
        int role_id;

        if (!user.getRole().equals(NORMAL_ROLE))
        {
            if (!userRequestRole.equals(ADMIN_ROLE))
                throw new NoAuthorizationException("Request is not made by admin!!");
            role_id = get_RoleID(user.getRole());
        }
        else
        {
            // Get id of the normal user role
            role_id = get_RoleID(NORMAL_ROLE);
        }

        // Create a Wallet
        int wallet_id = WalletDB.create_Wallet();
        if (wallet_id < 0)
            return -1;

        // Create a User
        String query = "INSERT INTO User (Wallet_ID, Email, Password, Role, Name, Surname, Birthday, NIF, CC, Address, Phone) VALUES ("
                + wallet_id + ", "
                + SQLiteJDBC.prepare_string(user.getEmail()) + ", "
                + SQLiteJDBC.prepare_string(user.getPassword()) + ", "
                + role_id + ", "
                + SQLiteJDBC.prepare_string(user.getFirstName()) + ", "
                + SQLiteJDBC.prepare_string(user.getLastName()) + ", "
                + SQLiteJDBC.prepare_string(user.getBirthday().toString()) + ", "
                + user.getNIF() + ", "
                + user.getCC() + ", "
                + SQLiteJDBC.prepare_string(user.getAddress()) + ", "
                + SQLiteJDBC.prepare_string(user.getPhoneNumber())
                + ");";

        try {
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            sqLiteJDBC2.executeUpdate(query);
            sqLiteJDBC2.close();
        } catch (SQLException e) {
            WalletDB.delete_Wallet(wallet_id);
            throw e;
        }

        return 1;
    }

    public static int get_User(User user) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        // Get a user by email
        String query = "SELECT * FROM User WHERE Email=" + SQLiteJDBC.prepare_string(user.getEmail()) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        if (!user.getPassword().equals(rs.getString("Password")))
            return -1;

        user.setId(rs.getInt("User_ID"));
        user.setFirstName(rs.getString("Name"));
        user.setLastName(rs.getString("Surname"));
        user.setNIF(rs.getInt("NIF"));
        user.setCC(rs.getInt("CC"));
        user.setAddress(rs.getString("Address"));
        user.setPhoneNumber(rs.getString("Phone"));
        user.setBirthday(LocalDate.parse(rs.getString("Birthday")));
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
        String role = get_Role(role_id);
        if (role.equals(""))
            return -1;
        user.setRole(role);

        return 1;
    }

    public static User get_User(int id) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        // Get a user by email
        String query = "SELECT * FROM User WHERE User_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        User user;

        if (rs.next()) {
            user = new User(rs.getString("Email"), rs.getString("Password"));
            user.setId(id);
            user.setFirstName(rs.getString("Name"));
            user.setLastName(rs.getString("Surname"));
            user.setAddress(rs.getString("Address"));
            user.setPhoneNumber(rs.getString("Phone"));
            // Get role
            String role = get_Role(rs.getInt("Role"));
            if (role.equals(""))
                return null;
            user.setRole(role);
        } else {
            user = null;
        }

        sqLiteJDBC2.closeRS(rs);

        return user;
    }

    public static void update_User(User user) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        // Update a user
        String query = "UPDATE User SET "
                + "Email = " + SQLiteJDBC.prepare_string(user.getEmail()) + ", "
                + "Password = " + SQLiteJDBC.prepare_string(user.getPassword()) + ", "
                + "Name = " + SQLiteJDBC.prepare_string(user.getFirstName()) + ", "
                + "Surname = " + SQLiteJDBC.prepare_string(user.getLastName()) + ", "
                + "Address = " + SQLiteJDBC.prepare_string(user.getAddress()) + ", "
                + "Phone = " + SQLiteJDBC.prepare_string(user.getPhoneNumber())
                + " WHERE User_ID=" + user.getId() + ";";

        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();
    }

    public static String getPasswordByEmail(String email)
    {
        String password = "";
        try
        {
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            String query = "SELECT * FROM User WHERE Email=" + SQLiteJDBC.prepare_string(email) + ";";;
            ResultSet rs = sqLiteJDBC2.executeQuery(query);
            if (rs.next()) {
                password = rs.getString("Password");
            }
            sqLiteJDBC2.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return password;
    }

    public static String getRoleByEmail(String email)
    {
        String role = "";
        try
        {
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            String query = "SELECT * FROM User WHERE Email=" + SQLiteJDBC.prepare_string(email) + ";";;
            ResultSet rs = sqLiteJDBC2.executeQuery(query);
            if (rs.next()) {
                role = get_Role(rs.getInt("Role"));
            }
            sqLiteJDBC2.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return role;
    }

    public static int getIdByEmail(String email)
    {
        int id = -1;
        try
        {
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            String query = "SELECT * FROM User WHERE Email=" + SQLiteJDBC.prepare_string(email) + ";";;
            ResultSet rs = sqLiteJDBC2.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("User_ID");
            }
            sqLiteJDBC2.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }
}
