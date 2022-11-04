package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rasbet.backend.Entities.Notification;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

/**
 * NotificationDB
 */
public class NotificationDB {

    public final static int globalId = -1;

    public static void createNotification(int idUser, String description, int requestUser)
            throws SQLException, NoAuthorizationException {
        if (!UserDB.assert_is_Administrator(requestUser)) {
            throw new NoAuthorizationException("This user is not an admin");

        }
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        String query = "INSERT INTO Notification (IdUser, Description) VALUES ('" + idUser + "', '" + description
                + "');";

        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();
    }

    public static void createAutomaticNotification(List<Notification> request) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        for (Notification notification : request) {
            String query = "INSERT INTO Notification (IdUser, Description) VALUES ('" + notification.getIdUser()
                    + "', '" + notification.getDescription()
                    + "');";
            sqLiteJDBC2.executeUpdate(query);
        }
        sqLiteJDBC2.close();
    }

    public static void deleteNotification(int idUser, String description, int requestUser)
            throws SQLException {

        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        if (idUser == NotificationDB.globalId) {
            String query = "DELETE FROM Notification WHERE Description = '" + description + "';";
            sqLiteJDBC2.executeUpdate(query);
        } else {
            String query = "DELETE FROM Notification WHERE IdUser = " + idUser + " AND Description = '" + description
                    + "';";
            sqLiteJDBC2.executeUpdate(query);
        }

        sqLiteJDBC2.close();

    }

    public static void deleteAllUserNotifications(int idUser, int requestUser)
            throws SQLException {

        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "DELETE FROM Notification WHERE idUser = " + idUser + ";";
        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();
    }

    public static void deleteMultipleNotifications(int idUser, List<String> descriptions, int requestUser)
            throws SQLException, NoAuthorizationException {
        if (UserDB.assert_is_Administrator(requestUser) || requestUser == idUser) {
            if (descriptions != null) {
                for (String description : descriptions) {
                    deleteNotification(idUser, description, requestUser);
                }
            } else {
                deleteAllUserNotifications(idUser, requestUser);
            }
        } else {
            throw new NoAuthorizationException("This user is not an admin");
        }

    }

    public static List<String> getNotifications(int idUser, int requestUser)
            throws SQLException, NoAuthorizationException {
        List<String> notifications = new ArrayList<>();
        if (UserDB.assert_is_Administrator(requestUser) || requestUser == idUser) {
            SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
            String query = "SELECT Description FROM Notification WHERE IdUser = " + idUser + ";";
            ResultSet rs = sqLiteJDBC.executeQuery(query);
            while (rs.next()) {
                notifications.add(rs.getString("Description"));
            }
            sqLiteJDBC.close();
            return notifications;
        } else {
            throw new NoAuthorizationException("This user is not an admin");
        }
    }
}