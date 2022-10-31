package com.rasbet.backend.Database;

import java.sql.SQLException;

import com.rasbet.backend.Exceptions.NoAuthorizationException;

/**
 * NotificationDB
 */
public class NotificationDB {

    public final static int globalId = -1;

    public static void createNotification(int idUser, String description, int requestUser)
            throws SQLException, NoAuthorizationException {
        if (UserDB.get_Role(requestUser) != UserDB.ADMIN_ROLE) {
            throw new NoAuthorizationException("This user is not an admin");
        }
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        String query = "INSERT INTO Notification (idUser, Description) VALUES ('" + idUser + "', '" + description
                + "');";

        sqLiteJDBC2.executeUpdate(query);
    }

    public static void deleteNotification(int idUser, String description, int requestUser) throws SQLException {

        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        String query = "DELETE FROM Notification WHERE idUser = " + idUser + " AND Description = " + description + ";";
        sqLiteJDBC2.executeUpdate(query);
    }
}