package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.rasbet.backend.Entities.Promotion;
import com.rasbet.backend.Exceptions.NoAuthorizationException;
import com.rasbet.backend.Exceptions.NoPromotionCodeException;

public class PromotionDB {
    public final static int PERCENTAGE_TYPE = 1;
    public final static int ABSOLUTE_TYPE = 2;

    public static void createPromotion(int id, String code, String description, double value, double minValue,
            int type)
            throws SQLException, NoAuthorizationException {

        UserDB.assert_is_Administrator(id);

        NotificationDB.createNotification(NotificationDB.globalId, description, id);

        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        String query = "INSERT INTO Promotion (Code, Value, MinValue, Type) VALUES ('" + code + "'"
                + ", '" + value + "', '" + minValue + "', '" + type + "');";
        sqLiteJDBC.executeUpdate(query);
        sqLiteJDBC.close();
    }

    public static void deletePromotion(int id, String code) throws SQLException, NoAuthorizationException {
        UserDB.assert_is_Administrator(id);

        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        String query = "DELETE FROM Promotion WHERE Code = '" + code + "';";

        sqLiteJDBC.executeUpdate(query);
        sqLiteJDBC.close();
    }

    public static void updatePromotion(int id, String code, String description, double value, double minValue,
            int type)
            throws SQLException, NoAuthorizationException {
        UserDB.assert_is_Administrator(id);
        
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        String query = "UPDATE Promotion SET Value = '" + value + "', MinValue = '" + minValue + "', Type = '" + type
                + "' WHERE Code = '" + code + "';";
        sqLiteJDBC.executeUpdate(query);
        sqLiteJDBC.close();
    }

    public static Promotion getPromotion(String code) throws SQLException, NoPromotionCodeException {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        String query = "SELECT * FROM Promotion WHERE Code = '" + code + "';";
        ResultSet rs = sqLiteJDBC.executeQuery(query);
        if (!rs.next()) {
            rs.close();
            sqLiteJDBC.close();
            throw new NoPromotionCodeException("This promotion code is not valid");
        }
        Promotion promotion = new Promotion(rs.getDouble("Value"), rs.getDouble("MinValue"), rs.getInt("Type"));

        rs.close();
        sqLiteJDBC.close();
        return promotion;
    }
}
