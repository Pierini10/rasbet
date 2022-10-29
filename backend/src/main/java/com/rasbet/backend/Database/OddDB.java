package com.rasbet.backend.Database;

import java.sql.SQLException;

import com.rasbet.backend.Entities.EventOdds;
import com.rasbet.backend.Entities.OddSimple;
import com.rasbet.backend.Entities.UpdateOddRequest;
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
    private static boolean updateOdd(String eventID, Double odd, String name) throws SQLException {
        try {
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
            String query = "Update Odd SET Odd =" + odd + ", OddSup = 1  WHERE Event_ID = + '" + eventID
                    + "' AND Entity = '" + name + "';";
            sqLiteJDBC2.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update various odds.
     * 
     * @param possibleBets List of odds to update.
     * @return True if odds were updated successfully, false otherwise, i think.
     */

    public static boolean updateOdds(UpdateOddRequest possibleBets) throws NoAuthorizationException, SQLException {

        if (UserDB.get_Role(possibleBets.getUserID()) != UserDB.SPECIALIST_ROLE)
            throw new NoAuthorizationException("User is not a specialist");

        for (EventOdds eventOdd : possibleBets.getPossibleBets()) {
            for (OddSimple odd : eventOdd.getOdds()) {

                updateOdd(eventOdd.getEventID(), odd.getOdd(), odd.getEntity());
            }
        }
        return true;

    }

}