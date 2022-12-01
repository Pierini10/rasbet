package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static String getSport(int id) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT Name FROM Sport WHERE Sport_ID = " + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String res;

        if (rs.next())
            res = rs.getString("Name");
        else
            throw new SQLException("Sport not found");

        sqLiteJDBC2.closeRS(rs);

        return res;
    }

    /**
     * Gets the ID of a bet state
     * 
     * @param state
     * @return Id of the state
     * @throws SQLException
     */
    public static Map<String, List<String>> getCompetitions() throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        Map<String, List<String>> res = new HashMap<>();

        String query = "SELECT Name, Sport_ID FROM Competition;";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {

            String sport = getSport(rs.getInt("Sport_ID"));
            if (res.containsKey(sport)) {
                res.get(sport).add(rs.getString("Name"));
            } else {
                List<String> list = new ArrayList<>();
                list.add(rs.getString("Name"));
                res.put(sport, list);
            }
        }

        sqLiteJDBC2.closeRS(rs);

        return res;
    }

    public static String getCompetition(int competition_id) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT Name FROM Competition WHERE Competition_ID=" + competition_id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String res = null;

        if (rs.next())
            res = rs.getString("Name");

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();

        return res;
    }

    public static int getCompetition_ID(String competition) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT Competition_ID FROM Competition WHERE Name=" + SQLiteJDBC.prepare_string(competition)
                + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        int res = -1;

        if (rs.next())
            res = rs.getInt("Competition_ID");

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();

        return res;
    }

    public static void addSports(Map<String, List<String>> sports) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        for (String sport : sports.keySet()) {
            int sport_id;
            String get_Sport = "SELECT Sport_ID FROM Sport WHERE Name =" + SQLiteJDBC.prepare_string(sport) + ";";
            ResultSet rs_select = sqLiteJDBC2.executeQuery(get_Sport);
            if (rs_select.next()) {
                sport_id = rs_select.getInt("Sport_ID");
            } else {
                String insert_Sport = "INSERT INTO Sport (Name) VALUES (" + SQLiteJDBC.prepare_string(sport)
                        + ") RETURNING Sport_ID;";
                sqLiteJDBC2.executeQuery(insert_Sport);
                ResultSet rs_insert = sqLiteJDBC2.executeQuery(get_Sport);
                sport_id = rs_insert.getInt("Sport_ID");
            }
            List<String> competitions = new ArrayList<>();
            for (String competition : sports.get(sport)) {
                competitions.add("(" + SQLiteJDBC.prepare_string(competition) + "," + sport_id + ")");
            }
            String get_Competition = "INSERT OR IGNORE INTO Competition (Name, Sport_ID) VALUES "
                    + String.join(",", competitions) + ";";
            sqLiteJDBC2.executeUpdate(get_Competition);
        }

        sqLiteJDBC2.close();
    }

    public static int addCompetition(int sport, String competition) throws SQLException {
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String insert_Competition = "INSERT INTO Competition (Name, Sport_ID) VALUES ("
                + SQLiteJDBC.prepare_string(competition) + ", " + sport + ") RETURNING Competition_ID;";
        ResultSet rs_insert = sqLiteJDBC2.executeQuery(insert_Competition);
        int comp_id = rs_insert.getInt("Competition_ID");

        sqLiteJDBC2.close();
        return comp_id;
    }

}
