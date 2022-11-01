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

        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        String query = "INSERT INTO Promotion (Code, Value, MinValue, Type) VALUES ('" + code + "', '"
                + value + ", " + minValue + ", '" + type + "');";

        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();
    }

    public static void deletePromotion(int id, String code) throws SQLException, NoAuthorizationException {
        if (UserDB.get_Role(id) != UserDB.ADMIN_ROLE) {
            throw new NoAuthorizationException("This user is not an admin");
        }
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        String query = "DELETE FROM Promotion WHERE code = " + code + ";";

        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();
    }
}
