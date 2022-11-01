package com.rasbet.backend.Database;

import java.sql.SQLException;

import com.rasbet.backend.Exceptions.NoAuthorizationException;

public class PromotionDB {

    public static void createPromotion(int id, String code, String description, double value, double minValue,
            String type)
            throws SQLException, NoAuthorizationException {

        if (UserDB.get_Role(id) != UserDB.ADMIN_ROLE) {
            throw new NoAuthorizationException("This user is not an admin");
        }
        NotificationDB.deleteNotification(-1, description, id);
        
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        String query = "INSERT INTO Promotion (Code, Value, MinValue, Type) VALUES ('" + code + "', '"
                + value + ", " + minValue + ", '" + type + "');";
        sqLiteJDBC2.executeUpdate(query);

    }

    public static void deletePromotion(int id, String code) throws SQLException, NoAuthorizationException {
        if (UserDB.get_Role(id) != UserDB.ADMIN_ROLE) {
            throw new NoAuthorizationException("This user is not an admin");
        }
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        String query = "DELETE FROM Promotion WHERE code = " + code + ";";
        sqLiteJDBC2.executeUpdate(query);
    }
}
