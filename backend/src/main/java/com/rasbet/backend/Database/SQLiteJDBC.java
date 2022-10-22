package com.rasbet.backend.Database;

import java.sql.*;

public class SQLiteJDBC {
    public static void main(String args[]) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database/rasbet_db.db");

            // get table users
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String NIF = rs.getString("NIF");
                String CC = rs.getString("CC");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phoneNumber");
                String birthday = rs.getString("birthday");

                System.out.println("ID = " + id);
                System.out.println("EMAIL = " + email);
                System.out.println("PASSWORD = " + password);
                System.out.println("FIRSTNAME = " + firstName);
                System.out.println("LASTNAME = " + lastName);
                System.out.println("NIF = " + NIF);
                System.out.println("CC = " + CC);
                System.out.println("ADDRESS = " + address);
                System.out.println("PHONENUMBER = " + phoneNumber);
                System.out.println("BIRTHDAY = " + birthday);
                System.out.println();
            }
            rs.close();
            stmt.close();
            

            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }        
        System.out.println("Opened database successfully");
    }
}