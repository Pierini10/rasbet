package com.rasbet.backend.Database;

import java.sql.*;
import java.util.List;

public class SQLiteJDBC2 {

    private Connection c;
    private Statement s;

    public Statement getS() {
        return this.s;
    }

    public SQLiteJDBC2() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        this.c = DriverManager.getConnection("jdbc:sqlite:database/rasbet_db.db");
        this.s = this.c.createStatement();

    }

    public void close() throws SQLException {
        this.s.close();
        this.c.close();
    }

    public void closeRS(ResultSet rs) throws SQLException {
        rs.close();
        this.s.close();
        this.c.close();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return s.executeQuery(query);
    }

    public void executeUpdate(String query) throws SQLException {
        s.executeUpdate(query);
    }

    public static String prepare_string(String string) {
        return "\'" + string + "\'";
    }

    public static String prepareList(List<String> list){
        String delimiter = ",";
        return "(" + String.join(delimiter, list) + ")";
    }

}
