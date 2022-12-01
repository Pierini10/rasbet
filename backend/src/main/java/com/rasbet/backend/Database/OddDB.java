package com.rasbet.backend.Database;

import java.sql.SQLException;
import java.util.List;

import com.rasbet.backend.Entities.EventOdds;
import com.rasbet.backend.Entities.OddSimple;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

/**
 * OddDB
 */
public class OddDB {
    /**
     * Update odds.
     * 
     * @param eventID Event ID.
     * @param odd     new Odd.
     * @param name    Odd name.
     * @return True if odds were updated successfully, false otherwise, i think.
     * @throws SQLException
     */
    private static void updateOdd(String eventID, Double odd, String name) throws SQLException {
        try {
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            String query = "Update Odd SET Odd =" + odd + ", OddSup = 1  WHERE Event_ID = + '" + eventID
                    + "' AND Entity = '" + name + "';";
            sqLiteJDBC2.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /**
     * Update various odds.
     * 
     * @param possibleBets List of odds to update.
     * @return True if odds were updated successfully, false otherwise, i think.
     */

    public static void updateOdds(int id, List<EventOdds> possibleBets) throws NoAuthorizationException, SQLException {

        UserDB.assert_is_Specialist(id);

        for (EventOdds eventOdd : possibleBets) {
            for (OddSimple odd : eventOdd.getOdds()) {

                updateOdd(eventOdd.getEventID(), odd.getOdd(), odd.getEntity());
            }
        }

    }

}