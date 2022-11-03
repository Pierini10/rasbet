package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletDB {

    // Creates a Wallet in the DB and returns the wallet id
    public static int create_Wallet() throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        // Create a wallet
        String query = "INSERT INTO Wallet (Balance) VALUES (0) RETURNING Wallet_ID;";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        int wallet_id = rs.getInt("Wallet_ID");

        sqLiteJDBC2.closeRS(rs);

        return wallet_id;
    }

    // Get the balance of a Wallet in the DB, by it's wallet id
    public static double get_Balance(int wallet_id) throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        // Create a wallet
        String query = "SELECT * FROM Wallet WHERE Wallet_ID=" + wallet_id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        double balance = rs.getDouble("Balance");

        sqLiteJDBC2.closeRS(rs);

        return balance;
    }

    // Delete a Wallet in the DB, by it's wallet id
    public static void delete_Wallet(int wallet_id) throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        // Create a wallet
        String query = "DELETE FROM Wallet WHERE Wallet_ID=" + wallet_id + ";";
        sqLiteJDBC2.executeUpdate(query);

        sqLiteJDBC2.close();
    }

    public static void setBalence(int walletID, double balance) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String update = "UPDATE Wallet SET Balance = " + (balance) +
                " WHERE Wallet_ID = "
                + walletID;
        sqLiteJDBC2.executeUpdate(update);
        sqLiteJDBC2.close();
    }
}
