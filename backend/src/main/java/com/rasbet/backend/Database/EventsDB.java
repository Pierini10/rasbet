package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Entities.Notification;
import com.rasbet.backend.Entities.Odd;
import com.rasbet.backend.Entities.SharedEventSubject;
import com.rasbet.backend.Exceptions.NoAmountException;
import com.rasbet.backend.Exceptions.NoAuthorizationException;
import com.rasbet.backend.Exceptions.SportDoesNotExistExeption;
import com.rasbet.backend.GamesAPI.GamesApi;

public class EventsDB {

    public final static String PENDING_STATUS = "Pending";
    public final static String FINISHED_STATUS = "Finished";
    public final static String CLOSED_STATUS = "Closed";

    public static String get_EventStatus(int id) throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT * FROM EventState WHERE EventState_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String status = rs.getString("Name");

        sqLiteJDBC2.closeRS(rs);
        return status;
    }

    public static int get_EventStatusID(String status) throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT * FROM EventState WHERE Name=" + SQLiteJDBC.prepare_string(status) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        int id = rs.getInt("EventState_ID");

        sqLiteJDBC2.closeRS(rs);
        return id;
    }

    public static boolean checkEventsAreOpen(List<String> events) throws SQLException {
        boolean r = true;
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        for (String idEvent : events) {
            String query = "SELECT EventState_ID FROM Event WHERE Event_ID=" + SQLiteJDBC.prepare_string(idEvent) + ";";
            ResultSet rs = sqLiteJDBC2.executeQuery(query);
            
            if (!get_EventStatus(rs.getInt("EventState_ID")).equals(PENDING_STATUS)) {
                r = false;
                break;
            }
        }

        sqLiteJDBC2.close();

        return r;
    }

    public static String get_Sport(int id) throws SQLException {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT * FROM Sport WHERE Sport_ID=" + id + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        String sport = rs.getString("Name");

        sqLiteJDBC2.closeRS(rs);
        return sport;
    }

    public static int get_SportID(String sport) throws SQLException, SportDoesNotExistExeption {
        // Create a connection
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();

        String query = "SELECT * FROM Sport WHERE Name=" + SQLiteJDBC.prepare_string(sport) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        if (!rs.next())
            throw new SportDoesNotExistExeption(sport + " is not supported in this app!");
        int id = rs.getInt("Sport_ID");

        sqLiteJDBC2.closeRS(rs);
        return id;
    }

    public static void add_Event(Event e, SharedEventSubject sharedEventSubject) throws SQLException, SportDoesNotExistExeption {

        sharedEventSubject.addEvent(e.getId());

        if (e != null) {
            // Event
            List<String> event_string = new ArrayList<>();
            event_string.add(SQLiteJDBC.prepare_string(e.getId()));
            int sport_id = get_SportID(e.getSport());
            event_string.add(Integer.toString(sport_id));
            int comp_id = SportsDB.getCompetition_ID(e.getCompetition());
            if (comp_id == -1) comp_id = SportsDB.addCompetition(sport_id, e.getCompetition());
            event_string.add(Integer.toString(comp_id));
            event_string.add(Integer.toString(get_EventStatusID(PENDING_STATUS)));
            event_string.add(SQLiteJDBC.prepare_string(e.getDatetime().toString()));
            event_string.add(SQLiteJDBC.prepare_string(e.getDescription()));
            event_string.add(SQLiteJDBC.prepare_string(e.getResult()));
            String event_str = SQLiteJDBC.prepareList(event_string);

            List<String> odds_string = new ArrayList<>();
            // Odds
            for (Odd o : e.getOdds().values()) {
                int odd_sup = o.getOdd_Sup() ? 1 : 0;
                List<String> odd_string = new ArrayList<>();
                odd_string.add(SQLiteJDBC.prepare_string(e.getId()));
                odd_string.add(SQLiteJDBC.prepare_string(o.getEntity()));
                odd_string.add(Double.toString(o.getOdd()));
                odd_string.add(Integer.toString(odd_sup));
                odds_string.add(SQLiteJDBC.prepareList(odd_string));
            }

            // Insert into Database
            String insert_events = "INSERT INTO Event " + " VALUES " + event_str + ";";
            String insert_odds = "INSERT INTO Odd VALUES " + String.join(",", odds_string) + ";";
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            sqLiteJDBC2.executeUpdate(insert_events);
            sqLiteJDBC2.executeUpdate(insert_odds);
            sqLiteJDBC2.close();
        }
    }

    public static void add_Events(List<Event> newEvents, SharedEventSubject sharedEventSubject) throws SQLException, SportDoesNotExistExeption {

        if (!newEvents.isEmpty()) {

            List<String> events_string = new ArrayList<>();
            List<String> odds_string = new ArrayList<>();

            for (Event e : newEvents) {

                sharedEventSubject.addEvent(e.getId());

                // Event
                List<String> event_string = new ArrayList<>();
                event_string.add(SQLiteJDBC.prepare_string(e.getId()));
                event_string.add(Integer.toString(get_SportID(e.getSport())));
                event_string.add(Integer.toString(SportsDB.getCompetition_ID(e.getCompetition())));
                event_string.add(Integer.toString(get_EventStatusID(PENDING_STATUS)));
                event_string.add(SQLiteJDBC.prepare_string(e.getDatetime().toString()));
                event_string.add(SQLiteJDBC.prepare_string(e.getDescription()));
                event_string.add(SQLiteJDBC.prepare_string(e.getResult()));
                events_string.add(SQLiteJDBC.prepareList(event_string));

                // Odds
                for (Odd o : e.getOdds().values()) {
                    int odd_sup = o.getOdd_Sup() ? 1 : 0;
                    List<String> odd_string = new ArrayList<>();
                    odd_string.add(SQLiteJDBC.prepare_string(e.getId()));
                    odd_string.add(SQLiteJDBC.prepare_string(o.getEntity()));
                    odd_string.add(Double.toString(o.getOdd()));
                    odd_string.add(Integer.toString(odd_sup));
                    odds_string.add(SQLiteJDBC.prepareList(odd_string));
                }

            }

            // Insert into Database
            String insert_events = "INSERT INTO Event VALUES " + String.join(",", events_string) + ";";
            String insert_odds = "INSERT INTO Odd VALUES " + String.join(",", odds_string) + ";";
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            sqLiteJDBC2.executeUpdate(insert_events);
            sqLiteJDBC2.executeUpdate(insert_odds);
            sqLiteJDBC2.close();
        }
    }

    public static String calculateWinner(String description, String result) {
        if (Pattern.compile("^(\\d+)x(\\d+)$").matcher(result).matches()){
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
        else {
            return result;
        }
    }

    public static void pay_bets(Map<Integer, Double> trans) throws NoAmountException, SQLException {
        for (Map.Entry<Integer, Double> entry : trans.entrySet()) {
            try {
                TransactionDB.addTransaction(entry.getKey(), "Win", entry.getValue(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void update_Events(List<Event> events, SharedEventSubject sharedEventSubject) throws SQLException {

        int bet_state_pending_id = BetDB.get_Bet_State(BetDB.PENDING_STATUS);
        int bet_state_win_id = BetDB.get_Bet_State(BetDB.WIN_STATUS);
        int bet_state_loss_id = BetDB.get_Bet_State(BetDB.LOSS_STATUS);
        int finished_state_id = get_EventStatusID(FINISHED_STATUS);
        Map<Integer, Double> trans = new HashMap<>();
        List<Notification> notifications = new ArrayList<>();
        
        for (Event e : events){
            if (e.getResult() != "null")
                sharedEventSubject.notifyFollowers(e.getId(), "Followed game: " + e.getDescription() + " ended " + e.getResult());
        }

        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC(); // Não puxar para cima porque não é SQLite safe
        
        for (Event e : events) {

            // Update Event
            String update_event = "UPDATE Event SET "
                    + "Result = " + SQLiteJDBC.prepare_string(e.getResult())
                    + ",EventState_ID = " + finished_state_id
                    + " WHERE Event_ID=" + SQLiteJDBC.prepare_string(e.getId()) + ";";
            sqLiteJDBC2.executeUpdate(update_event);

            // Update Odds
            if (e.getOdds() != null) {
                
                String get_odds = "SELECT * FROM Odd WHERE Event_ID=" + SQLiteJDBC.prepare_string(e.getId()) + ";";
                ResultSet rs_odds = sqLiteJDBC2.executeQuery(get_odds);
                while (rs_odds.next()) {
                    String entity = rs_odds.getString("Entity");
                    Odd odd = e.getOdd(entity);
                    if (rs_odds.getInt("OddSup") == 0 && rs_odds.getDouble("odd") != odd.getOdd()) {
                        String update_odd = "UPDATE Odd SET "
                        + "Odd = " + odd.getOdd()
                        + " WHERE Event_ID=" + SQLiteJDBC.prepare_string(e.getId()) + ";";
                        sqLiteJDBC2.executeUpdate(update_odd);
                        //sharedEventSubject.notifyFollowers(e.getId(), "Followed game: Odd " + odd.getEntity() + "updated to" + odd.getOdd());
                    }
                }
                rs_odds.close();
            }

            List<String> execute_querys = new ArrayList<>();
            List<String> update_querys = new ArrayList<>();
            // Update all simple bets
            String get_bets = "SELECT * FROM SimpleBet WHERE Event_ID=" + SQLiteJDBC.prepare_string(e.getId())
                    + " AND BetState_ID=" + bet_state_pending_id + ";";
            Map<Integer, Integer> simplebet_counter = new HashMap<>();
            String winner = calculateWinner(e.getDescription(), e.getResult());
            ResultSet bets = sqLiteJDBC2.executeQuery(get_bets);
            while (bets.next()) {
                int state_id = winner.equals(bets.getString("Prediction")) ? bet_state_win_id
                        : bet_state_loss_id;
                int bet_id = bets.getInt("Bet_ID");

                if (state_id == bet_state_loss_id) {
                    String update_bet = "UPDATE Bet SET "
                            + "BetState_ID = " + state_id
                            + " WHERE  Bet_ID=" + bet_id + " RETURNING *;";
                    execute_querys.add(update_bet);        
                }

                String update_bet = "UPDATE SimpleBet SET "
                        + "BetState_ID = " + state_id
                        + " WHERE Event_ID=" + SQLiteJDBC.prepare_string(e.getId()) + " AND Bet_ID="
                        + bet_id + ";";
                update_querys.add(update_bet);
                if (simplebet_counter.containsKey(bet_id))
                    simplebet_counter.put(bet_id, simplebet_counter.get(bet_id) + 1);
                else
                    simplebet_counter.put(bet_id, 1);
            }
            bets.close();

            for(String s : execute_querys){
                SQLiteJDBC sqLiteJDBC_sup = new SQLiteJDBC();
                ResultSet rs = sqLiteJDBC_sup.executeQuery(s);
                notifications.add(new Notification(rs.getInt("User_ID"), "You lost bet number " + rs.getInt("Bet_ID")));
                sqLiteJDBC_sup.closeRS(rs);
            }

            for(String s : update_querys){
                SQLiteJDBC sqLiteJDBC_sup = new SQLiteJDBC();
                sqLiteJDBC_sup.executeUpdate(s);
                sqLiteJDBC_sup.close();
            }

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

                        notifications
                                .add(new Notification(bet.getInt("User_ID"), "You Won bet number " + entry.getKey()));
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
        NotificationDB.createAutomaticNotification(notifications);
        try {
            pay_bets(trans);
        } catch (NoAmountException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Updates the state of a event
     * 
     * @param idEvent
     * @param state
     * @throws SQLException
     * @throws NoAuthorizationException
     */
    public static void update_Event_State(String idEvent, Integer idUser, String state, SharedEventSubject sharedEventSubject) throws SQLException, NoAuthorizationException {
        UserDB.assert_is_Administrator(idUser);

        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();

        Integer idState = EventsDB.get_EventStatusID(state);

        String query = "UPDATE Event SET EventState_ID = " + idState + " WHERE Event_ID = "
                + SQLiteJDBC.prepare_string(idEvent) + " RETURNING Description;";

                
        ResultSet rs = sqLiteJDBC.executeQuery(query);     
        String description = rs.getString("Description");
        sqLiteJDBC.close();

        sharedEventSubject.notifyFollowers(idEvent, " Event " + description + " has changed to " + state);
    }

    // Updates all DB events
    public static void update_Database(SharedEventSubject sharedEventSubject) throws Exception {

        // Get events from API
        List<Event> events = GamesApi.getallEvents();

        // Create structs for new events
        List<Event> newEvents = new ArrayList<>();
        List<Event> oldEvents = new ArrayList<>();

        int state_pending_id = get_EventStatusID(PENDING_STATUS);

        if (events != null) {
            // Create a connection
            SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
            for (Event e : events) {
                String query = "SELECT * FROM Event WHERE Event_ID=" + SQLiteJDBC.prepare_string(e.getId()) + ";";
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
            update_Events(oldEvents, sharedEventSubject);
            add_Events(newEvents, sharedEventSubject);
        }

    }

    // Gets all DB events
    public static ArrayList<Event> get_Events(String sport, Boolean closed_state) throws SQLException, SportDoesNotExistExeption {
        // Create a connection
        int sport_id = get_SportID(sport);
        ArrayList<Event> events = new ArrayList<>();
        
        int state = get_EventStatusID(PENDING_STATUS);
        String query = "SELECT * FROM Event WHERE Sport_ID=" + sport_id + " AND (EventState_ID=" + state;
        if (closed_state){
            state = get_EventStatusID(CLOSED_STATUS);
            query += " OR EventState_ID=" + state;
        }
        query += ");";

        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {
            // Get Event
            Map<String, Odd> odds = new HashMap<>();
            String id = rs.getString("Event_ID"), status = get_EventStatus(rs.getInt("EventState_ID"));
            String competition = SportsDB.getCompetition(rs.getInt("Competition_ID"));
            events.add(new Event(id, sport, competition, LocalDateTime.parse(rs.getString("DateTime")), rs.getString("Description"),
                    rs.getString("Result"), status, odds));
        }

        for (Event e : events) {
            // Get all odds
            String get_odds = "SELECT * FROM Odd WHERE Event_ID=" + SQLiteJDBC.prepare_string(e.getId()) + ";";
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

    // Gets all DB events
    public static ArrayList<String> getAllEventIds() throws SQLException {
        // Create a connection
        ArrayList<String> events = new ArrayList<>();
        
        int state = get_EventStatusID(FINISHED_STATUS);
        String query = "SELECT Event_ID FROM Event WHERE  (EventState_ID!=" + state + ");";

        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {
            events.add(rs.getString("Event_ID"));
        }

        sqLiteJDBC2.close();

        return events;
    }

}
