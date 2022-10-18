package com.rasbet.backend.Database;

import java.sql.*;

public class SQLiteJDBC {
    public static void main(String args[]) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database/rasbet_db.db");
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }        
        System.out.println("Opened database successfully");
    }
}