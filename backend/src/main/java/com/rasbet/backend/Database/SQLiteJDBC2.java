package com.rasbet.backend.Database;

import java.sql.*;

public class SQLiteJDBC2 {

    private Connection c;


    public SQLiteJDBC2() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:database/rasbet_db.db");
        } catch (Exception e) {
            this.c = null;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }        
    }


    public void close(){
        try {
            this.c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }    
    }


    public Connection getC() {
        return this.c;
    }

}
