package com.rasbet.backend.Database;

import java.sql.SQLException;
import java.util.List;

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
        sqLiteJDBC2.close();
    }

    public static void deleteNotification(int idUser, String description, int requestUser)
            throws SQLException {

        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        if (idUser == NotificationDB.globalId) {
            String query = "DELETE FROM Notification WHERE description = '" + description + "';";
            sqLiteJDBC2.executeUpdate(query);
        } else {
            String query = "DELETE FROM Notification WHERE idUser = " + idUser + " AND description = '" + description
                    + "';";
            sqLiteJDBC2.executeUpdate(query);
        }

        sqLiteJDBC2.close();

    }

    public static void deleteMultipleNOtificatios(int idUser, List<String> descriptions, int requestUser)
            throws SQLException, NoAuthorizationException {
        if (UserDB.get_Role(requestUser) == UserDB.ADMIN_ROLE || requestUser == idUser) {
            for (String description : descriptions) {
                deleteNotification(idUser, description, requestUser);
            }
        } else {
            throw new NoAuthorizationException("This user is not an admin");
        }

    }
}