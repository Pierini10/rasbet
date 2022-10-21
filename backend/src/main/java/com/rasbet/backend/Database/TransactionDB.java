package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.rasbet.backend.Entities.Transaction;

public class TransactionDB {

    public static ArrayList<Transaction> getTransactions(int userID) throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String wallet_id = "SELECT * FROM Transaction INNER JOIN User WHERE User_ID=$" + userID
                + " ON Transaction.Wallet_ID = User.Wallet_ID;";
        ResultSet rs = sqLiteJDBC2.executeQuery(wallet_id);
        while (rs.next()) {
            transactions.add(new Transaction(rs.getString("Date"), "deposito",
                    rs.getDouble("Value"), rs.getDouble("PostTransactionBalance")));
        }
        return transactions;
    }

}
