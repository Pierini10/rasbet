package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDB
{

    public static void followEvent(boolean follow, int user_id, String event_id) throws SQLException
    {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();

        String query;
        if(follow)
            query = "INSERT INTO Follow (User_ID, Event_ID) VALUES (" + user_id + ", " + SQLiteJDBC.prepare_string(event_id) + ");";
        else
            query = "DELETE FROM Follow WHERE User_ID = " + user_id + " AND Event_ID = " + SQLiteJDBC.prepare_string(event_id) + ";";

        sqLiteJDBC.executeUpdate(query);
        sqLiteJDBC.close();
    }

    public static List<String> getFollowedEvents(int user_id) throws SQLException
    {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();

        String query = "SELECT Event_ID FROM Follow WHERE User_ID = " + user_id + ";";
        ResultSet rs = sqLiteJDBC.executeQuery(query);

        List<String> res = new ArrayList<>();
        while(rs.next())
            res.add(rs.getString("Event_ID"));

        sqLiteJDBC.closeRS(rs);
        return res;
    }

    public static List<Integer> getFollowers(String event_id) throws SQLException
    {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        String query = "SELECT User_ID FROM Follow WHERE Event_ID = " + SQLiteJDBC.prepare_string(event_id) + ";";
        ResultSet rs = sqLiteJDBC.executeQuery(query);

        List<Integer> res = new ArrayList<>();
        while(rs.next())
            res.add(rs.getInt("User_ID"));

        sqLiteJDBC.closeRS(rs);
        return res;
    }

}
