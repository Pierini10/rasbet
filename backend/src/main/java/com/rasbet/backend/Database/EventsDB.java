package com.rasbet.backend.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Entities.Odd;

public class EventsDB {

    // Updates all DB events
    public static void update_Events(ArrayList<Event> events) throws SQLException {
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        for (Event e : events) {
            String query = "SELECT * FROM Event WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
            ResultSet rs = sqLiteJDBC2.executeQuery(query);
            if (rs.next()) {
                // Update Event
                if ((e.getResult() != null) && rs.getString("Result") == null) {
                    String update_event = "UPDATE Event SET "
                            + "Result = " + SQLiteJDBC2.prepare_string(e.getResult())
                            + " WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
                    sqLiteJDBC2.executeUpdate(update_event);
                }

                // Update Odds
                String get_odds = "SELECT * FROM Odd WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
                ResultSet rs_odds = sqLiteJDBC2.executeQuery(get_odds);
                while (rs_odds.next()) {
                    String entity = rs.getString("Entity");
                    Odd odd = e.getOdd(entity);
                    if (rs_odds.getInt("OddSup") == 0 && rs_odds.getDouble("odd") != odd.getOdd()) {
                        String update_event = "UPDATE Odd SET "
                                + "Odd = " + odd.getOdd()
                                + " WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
                        sqLiteJDBC2.executeUpdate(update_event);
                    }
                }
            } else { // TODO DO NOT CREATE GAMES DONE
                // Create Odds
                for (Odd o : e.getOdds().values()) {
                    int odd_sup;
                    if (o.getOdd_Sup())
                        odd_sup = 1;
                    else
                        odd_sup = 0;
                    String create_odd = "INSERT INTO Odd VALUES ("
                            + SQLiteJDBC2.prepare_string(e.getId()) + ", "
                            + SQLiteJDBC2.prepare_string(o.getEntity()) + ", "
                            + o.getOdd() + ", "
                            + odd_sup
                            + ");";
                    sqLiteJDBC2.executeUpdate(create_odd);
                    System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" + e.getId() + o.getEntity());
                }
                String create_event = "INSERT INTO Event VALUES ("
                        + SQLiteJDBC2.prepare_string(e.getId()) + ", "
                        + 0 + ", " // TODO
                        + 0 + ", " // TODO
                        + SQLiteJDBC2.prepare_string(e.getDatetime()) + ", "
                        + SQLiteJDBC2.prepare_string(e.getDescription()) + ", "
                        + SQLiteJDBC2.prepare_string(e.getResult())
                        + ");";
                sqLiteJDBC2.executeUpdate(create_event);
            }
        }
        sqLiteJDBC2.close();
    }

    // Gets all DB events
    public static ArrayList<Event> get_Events() throws SQLException {
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        ArrayList<Event> events = new ArrayList<>();
        int state = 0; // TODO
        String query = "SELECT * FROM Event WHERE EventState_ID=" + state + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        while (rs.next()) {

            // Get Event
            Map<String, Odd> odds = new HashMap<>();
            String id = rs.getString("Event_ID");
            String sport = "Football"; // TODO
            events.add(new Event(id, sport, rs.getString("DateTime"), rs.getString("Description"), rs.getString("Result"), odds)); 
            
        
            // Get all odds
            String get_odds = "SELECT * FROM Odd WHERE Event_ID=" + SQLiteJDBC2.prepare_string(id) + ";";
            ResultSet rs_odds = sqLiteJDBC2.executeQuery(get_odds);
            while(rs_odds.next()){
                boolean odd_sup;
                if (rs_odds.getInt("OddSup") == 1) odd_sup = true;
                else odd_sup = false;
                String entity = rs_odds.getString("Entity");
                odds.put(entity, new Odd(entity, rs_odds.getDouble("Odd"), odd_sup));
            }
        }
        sqLiteJDBC2.close();
        return events;
    }

}
