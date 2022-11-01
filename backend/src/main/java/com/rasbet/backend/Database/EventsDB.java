package com.rasbet.backend.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Entities.Odd;
import com.rasbet.backend.Exceptions.NoAmountException;
import com.rasbet.backend.Exceptions.SportDoesNotExistExeption;
import com.rasbet.backend.GamesAPI.GamesApi;

public class EventsDB {

    public final static String PENDING_STATUS = "Pending";
    public final static String FINISHED_STATUS = "Finished";
    public final static String CLOSED_STATUS = "Closed";

    public final static String FOOTBALL = "Football";

    public static String get_EventStatus(int id) throws SQLException {
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String query = "SELECT * FROM EventState WHERE EventState_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String status = rs.getString("Name");

        sqLiteJDBC2.closeRS(rs);
        return status;
    }

    public static int get_EventStatusID(String status) throws SQLException {
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String query = "SELECT * FROM EventState WHERE Name=" + SQLiteJDBC2.prepare_string(status) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        int id = rs.getInt("EventState_ID");

        sqLiteJDBC2.closeRS(rs);
        return id;
    }

    public static String get_Sport(int id) throws SQLException {
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String query = "SELECT * FROM Sport WHERE Sport_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String sport = rs.getString("Name");

        sqLiteJDBC2.closeRS(rs);
        return sport;
    }

    public static int get_SportID(String sport) throws SQLException, SportDoesNotExistExeption {
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String query = "SELECT * FROM Sport WHERE Name=" + SQLiteJDBC2.prepare_string(sport) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        if (!rs.next()) throw new SportDoesNotExistExeption(sport + " is not supported in this app!");
        int id = rs.getInt("Sport_ID");

        sqLiteJDBC2.closeRS(rs);
        return id;
    }

    public static void add_Event(Event e) throws SQLException, SportDoesNotExistExeption {

        if (e != null) {
            // Event
            List<String> event_string = new ArrayList<>();
            event_string.add(Integer.toString(get_SportID(e.getSport())));
            System.out.println("HEIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
            event_string.add(Integer.toString(get_EventStatusID(PENDING_STATUS)));
            event_string.add(SQLiteJDBC2.prepare_string(e.getDatetime()));
            event_string.add(SQLiteJDBC2.prepare_string(e.getDescription()));
            String event_str = SQLiteJDBC2.prepareList(event_string);

            List<String> event_atr_string = new ArrayList<>();
            event_atr_string.add("Sport_ID");
            event_atr_string.add("EventState_ID");
            event_atr_string.add("DateTime");
            event_atr_string.add("Description");
            String event_atr_str = SQLiteJDBC2.prepareList(event_atr_string);

            // Insert into Database
            String insert_events = "INSERT INTO " + event_atr_str + " VALUES " + event_str + ";";
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
            sqLiteJDBC2.executeUpdate(insert_events);
            sqLiteJDBC2.close();
        }
    }

    public static void add_Events(List<Event> newEvents) throws SQLException, SportDoesNotExistExeption {

        if (!newEvents.isEmpty()) {

            List<String> events_string = new ArrayList<>();
            List<String> odds_string = new ArrayList<>();

            for (Event e : newEvents) {

                // Event
                List<String> event_string = new ArrayList<>();
                event_string.add(SQLiteJDBC2.prepare_string(e.getId()));
                event_string.add(Integer.toString(get_SportID(e.getSport())));
                event_string.add(Integer.toString(get_EventStatusID(PENDING_STATUS)));
                event_string.add(SQLiteJDBC2.prepare_string(e.getDatetime()));
                event_string.add(SQLiteJDBC2.prepare_string(e.getDescription()));
                event_string.add(SQLiteJDBC2.prepare_string(e.getResult()));
                events_string.add(SQLiteJDBC2.prepareList(event_string));

                // Odds
                for (Odd o : e.getOdds().values()) {
                    int odd_sup = o.getOdd_Sup() ? 1 : 0;
                    List<String> odd_string = new ArrayList<>();
                    odd_string.add(SQLiteJDBC2.prepare_string(e.getId()));
                    odd_string.add(SQLiteJDBC2.prepare_string(o.getEntity()));
                    odd_string.add(Double.toString(o.getOdd()));
                    odd_string.add(Integer.toString(odd_sup));
                    odds_string.add(SQLiteJDBC2.prepareList(odd_string));
                }

            }

            // Insert into Database
            String insert_events = "INSERT INTO Event VALUES " + String.join(",", events_string) + ";";
            String insert_odds = "INSERT INTO Odd VALUES " + String.join(",", odds_string) + ";";
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
            sqLiteJDBC2.executeUpdate(insert_events);
            sqLiteJDBC2.executeUpdate(insert_odds);
            sqLiteJDBC2.close();
        }
    }

    public static String calculateWinner(String description, String result) {
        String[] r = result.split("x", 2);
        int winner = (Integer.parseInt(r[0])) - (Integer.parseInt(r[1]));
        String[] d = description.split(" v ", 2);
        if (winner == 0)
            return "Draw";
        else if (winner > 0)
            return d[0];
        else
            return d[1];
    }

    public static void pay_bets(Map<Integer, Double> trans) throws NoAmountException, SQLException {
        for (Map.Entry<Integer, Double> entry : trans.entrySet()) {
            TransactionDB.addTransaction(entry.getKey(), "Win", entry.getValue());
        }
    }

    public static void update_Events(List<Event> events) throws SQLException {

        int bet_state_pending_id = BetDB.get_Bet_State(BetDB.PENDING_STATUS);
        int bet_state_win_id = BetDB.get_Bet_State(BetDB.WIN_STATUS);
        int bet_state_loss_id = BetDB.get_Bet_State(BetDB.LOSS_STATUS);
        int finished_state_id = get_EventStatusID(FINISHED_STATUS);
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        Map<Integer, Double> trans = new HashMap<>();

        for (Event e : events) {

            // Update Event
            String update_event = "UPDATE Event SET "
                    + "Result = " + SQLiteJDBC2.prepare_string(e.getResult())
                    + ",EventState_ID = " + finished_state_id
                    + " WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
            sqLiteJDBC2.executeUpdate(update_event);

            // Update Odds
            String get_odds = "SELECT * FROM Odd WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
            ResultSet rs_odds = sqLiteJDBC2.executeQuery(get_odds);
            while (rs_odds.next()) {
                String entity = rs_odds.getString("Entity");
                Odd odd = e.getOdd(entity);
                if (rs_odds.getInt("OddSup") == 0 && rs_odds.getDouble("odd") != odd.getOdd()) {
                    String update_odd = "UPDATE Odd SET "
                            + "Odd = " + odd.getOdd()
                            + " WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
                    sqLiteJDBC2.executeUpdate(update_odd);
                }
            }
            rs_odds.close();

            // Update all simple bets
            String get_bets = "SELECT * FROM SimpleBet WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId())
                    + " AND BetState_ID=" + bet_state_pending_id + ";";
            ResultSet bets = sqLiteJDBC2.executeQuery(get_bets);
            Map<Integer, Integer> simplebet_counter = new HashMap<>();
            while (bets.next()) {
                String winner = calculateWinner(e.getDescription(), e.getResult());
                int state_id = winner.equals(bets.getString("Prediction")) ? bet_state_win_id
                        : bet_state_loss_id;
                int bet_id = bets.getInt("Bet_ID");

                if (state_id == bet_state_loss_id) {
                    String update_bet = "UPDATE Bet SET "
                            + "BetState_ID = " + state_id
                            + " WHERE  Bet_ID=" + bet_id + ";";
                    sqLiteJDBC2.executeUpdate(update_bet);
                }

                String update_bet = "UPDATE SimpleBet SET "
                        + "BetState_ID = " + state_id
                        + " WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + " AND Bet_ID="
                        + bet_id + ";";
                sqLiteJDBC2.executeUpdate(update_bet);
                if (simplebet_counter.containsKey(bet_id))
                    simplebet_counter.put(bet_id, simplebet_counter.get(bet_id) + 1);
                else
                    simplebet_counter.put(bet_id, 1);
            }
            bets.close();

            // Update Bets and Wallets
            for (Map.Entry<Integer, Integer> entry : simplebet_counter.entrySet()) {
                String bet_query = "SELECT * FROM Bet WHERE Bet_ID=" + entry.getKey() + " AND BetState_ID="
                        + bet_state_pending_id + ";";
                ResultSet bet = sqLiteJDBC2.executeQuery(bet_query);
                if (bet.next()) {
                    int gamesleft = bet.getInt("GamesLeft") - entry.getValue();
                    if (gamesleft == 0) {
                        String update_bet = "UPDATE Bet SET "
                                + "BetState_ID = " + bet_state_win_id
                                + " WHERE  Bet_ID=" + entry.getKey() + ";";

                        // Won Bet
                        double money_won = bet.getDouble("Amount") * bet.getDouble("Totalodds");
                        trans.put(bet.getInt("User_ID"), money_won);
                        sqLiteJDBC2.executeUpdate(update_bet);
                    } else {
                        String update_bet = "UPDATE Bet SET "
                                + "GamesLeft = " + gamesleft
                                + " WHERE  Bet_ID=" + entry.getKey() + ";";
                        sqLiteJDBC2.executeUpdate(update_bet);
                    }
                }
                bet.close();
            }
        }
        sqLiteJDBC2.close();
        try {
            pay_bets(trans);
        } catch (NoAmountException e1) {
            e1.printStackTrace();
        }
    }

    // Updates all DB events
    public static void update_Database() throws SQLException, SportDoesNotExistExeption {

        // Get events from API
        List<Event> events = GamesApi.getEvents();

        // Create structs for new events
        List<Event> newEvents = new ArrayList<>();
        List<Event> oldEvents = new ArrayList<>();

        int state_pending_id = get_EventStatusID(PENDING_STATUS);

        if (events != null) {
            // Create a connection
            SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
            for (Event e : events) {
                String query = "SELECT * FROM Event WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
                ResultSet rs = sqLiteJDBC2.executeQuery(query);

                if (rs.next()) {
                    if (rs.getInt("EventState_ID") == state_pending_id && e.getResult() != null) {
                        oldEvents.add(e);
                    }
                } else if (e.getResult() == null)
                    newEvents.add(e);
            }
            sqLiteJDBC2.close();

            // Add new Oods and Events
            update_Events(oldEvents);
            add_Events(newEvents);
        }
    }

    // Gets all DB events
    public static ArrayList<Event> get_Events() throws SQLException {
        // Create a connection
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        ArrayList<Event> events = new ArrayList<>();
        int state = get_EventStatusID(PENDING_STATUS);
        String query = "SELECT * FROM Event WHERE EventState_ID=" + state + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {
            // Get Event
            Map<String, Odd> odds = new HashMap<>();
            String id = rs.getString("Event_ID"), sport = get_Sport(rs.getInt("Sport_ID")),
                    status = get_EventStatus(rs.getInt("EventState_ID"));
            events.add(new Event(id, sport, rs.getString("DateTime"), rs.getString("Description"),
                    rs.getString("Result"), status, odds));
        }

        for (Event e : events) {
            // Get all odds
            String get_odds = "SELECT * FROM Odd WHERE Event_ID=" + SQLiteJDBC2.prepare_string(e.getId()) + ";";
            ResultSet rs_odds = sqLiteJDBC2.executeQuery(get_odds);
            while (rs_odds.next()) {
                boolean odd_sup;
                if (rs_odds.getInt("OddSup") == 1)
                    odd_sup = true;
                else
                    odd_sup = false;
                String entity = rs_odds.getString("Entity");
                e.getOdds().put(entity, new Odd(entity, rs_odds.getDouble("Odd"), odd_sup));
            }
        }

        sqLiteJDBC2.close();
        return events;
    }

}
