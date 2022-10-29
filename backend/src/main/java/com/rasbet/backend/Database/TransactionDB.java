package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.rasbet.backend.Entities.Transaction;
import com.rasbet.backend.Exceptions.NoAmountException;

public class TransactionDB {

    /**
     * Get all transactions from a user.
     * 
     * @param userID User ID.
     * @return List of transactions.
     * @throws SQLException
     */
    public static ArrayList<Transaction> getTransactions(int userID) throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        String walletIdQuery = "SELECT Wallet_ID FROM User WHERE User_ID = " + userID + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(walletIdQuery);
        int walletID = rs.getInt("Wallet_ID");
        String query = "SELECT * FROM 'Transaction'"
                + " INNER JOIN TransactionType ON 'Transaction'.TransactionType=TransactionType.TransactionType_ID Where 'Transaction'.Wallet_ID = "
                + walletID + ";";
        rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {
            String transactionType = rs.getString("Name");
            double value = rs.getDouble("Value");
            double postTransactionBalance = rs.getDouble("PostTransactionBalance");
            String date = rs.getString("Date");
            String time = rs.getString("Time");
            transactions.add(new Transaction(transactionType, value, postTransactionBalance, date, time));
        }

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();

        return transactions;
    }

    /**
     * Creates a Transaction in the DB and returns the current balance of the user
     * wallet
     * 
     * @param userId          the user id
     * @param TransactionType the transaction type
     * @param value           the value of the transaction
     * @return the current balance of the user wallet
     * @throws NoAmountException if the user has not enough money on his wallet
     * @throws SQLException
     */
    public static double addTransaction(int userId, String TransactionType, double value)
            throws NoAmountException, SQLException {

        int transactionType_ID = hasType(TransactionType);

        if (transactionType_ID == -1) {
            transactionType_ID = addType(TransactionType);
        }

        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String wallet_id = "SELECT * FROM User WHERE User_ID='" + userId + "'";
        ResultSet rs = sqLiteJDBC2.executeQuery(wallet_id);

        int walletID = rs.getInt("Wallet_ID");

        double balanceValue = WalletDB.get_Balance(walletID);

        if (balanceValue + value >= 0) {
            String fullDate = getDateAndTime();
            String date = fullDate.split(" ")[0];
            String time = fullDate.split(" ")[1];

            String insert = "INSERT into 'Transaction' (Wallet_ID,TransactionType,Value,PostTransactionBalance,'Date','Time') values ('"
                    + walletID + "','" + transactionType_ID + "','" + value + "','" + (balanceValue + value) + "','"
                    + date + "','" + time + "')";
            sqLiteJDBC2.executeUpdate(insert);

            WalletDB.setBalence(walletID, balanceValue + value);
        } else {
            sqLiteJDBC2.closeRS(rs);
            sqLiteJDBC2.close();
            throw new NoAmountException("Not enough money in wallet to finish the transaction");
        }

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();

        return balanceValue + value;

    }

    /** @return a string with the date and time in the correct format */
    private static String getDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return dtf.format(LocalDateTime.now());
    }

    /**
     * 
     * @param Name name of the transactionType to add
     * @return the id of the transactionType added
     */
    private static int addType(String name) {
        try {
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

            String insert = "Insert into TransactionType (Name) values ('" + name + "') returning TransactionType_ID";
            ResultSet rs = sqLiteJDBC2.executeQuery(insert);

            sqLiteJDBC2.closeRS(rs);
            sqLiteJDBC2.close();

            return rs.getInt("TransactionType_ID");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 
     * @param type name of the transactionType to check
     * @return the id of the transactionType if it exists, -1 if it doesn't
     */
    public static int hasType(String type) {
        try {
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

            String query = "SELECT * FROM TransactionType WHERE Name='" + type + "'";
            ResultSet rs = sqLiteJDBC2.executeQuery(query);
            int result = rs.getInt("TransactionType_ID");

            sqLiteJDBC2.closeRS(rs);
            sqLiteJDBC2.close();

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
