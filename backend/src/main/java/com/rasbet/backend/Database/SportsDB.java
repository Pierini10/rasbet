package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SportsDB {

    /**
     * Gets the ID of a bet state
     * 
     * @param state
     * @return Id of the state
     * @throws SQLException
     */
    public static List<String> getSports() throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        List<String> res = new ArrayList<>();

        String query = "SELECT Name FROM Sport;";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {
            res.add(rs.getString("Name"));
        }

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();

        return res;
    }

}
