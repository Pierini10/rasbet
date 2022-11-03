package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.rasbet.backend.Entities.Bet;
import com.rasbet.backend.Entities.HistoryBets;
import com.rasbet.backend.Entities.Prediction;

public class BetDB {

    public final static String PENDING_STATUS = "Pending";
    public final static String WIN_STATUS = "Win";
    public final static String LOSS_STATUS = "Loss";

    /**
     * Updates the data of a bet in the database
     * 
     * @param bet New bet data
     * @throws SQLException
     */
    public static void update_Bet(Bet bet) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        Integer idState = BetDB.get_Bet_State(bet.getBetState());

        String idUser = bet.getIdUser() == null ? "" : ("User_ID = " + bet.getIdUser() + ", ");
        String idBetState = idState == null ? "" : ("BetState_ID = " + idState + ", ");
        String amount = bet.getAmount() == null ? "" : ("Amount = " + bet.getAmount() + ", ");
        String dateTime = bet.getDateTime() == null ? ""
                : ("DateTime = " + SQLiteJDBC2.prepare_string(bet.getDateTime().format(formatter)) + ", ");

        StringBuilder sb = new StringBuilder();
        sb.append(idUser).append(idBetState).append(amount).append(dateTime).setLength(sb.length() - 2);

        String query = "UPDATE Bet SET " + sb.toString() + " WHERE Bet_ID =" + bet.getId() + ";";

        sqLiteJDBC2.executeUpdate(query);
        sqLiteJDBC2.close();
    }

    /**
     * Gets the ID of a bet state
     * 
     * @param state
     * @return Id of the state
     * @throws SQLException
     */
    public static Integer get_Bet_State(String state) throws SQLException {
        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();

        String query = "SELECT BetState_ID FROM BetState WHERE Name =" + SQLiteJDBC2.prepare_string(state) + ";";
        ResultSet rs = sqLiteJDBC2.executeQuery(query);
        int id = rs.getInt("BetState_ID");

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();
        return id;
    }

    /**
     * Get all bets made by an user
     * 
     * @param idUser
     * @return
     * @throws SQLException
     */
    public static HistoryBets get_Bets(Integer idUser) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        List<Bet> bets = new ArrayList<>();

        String query = "SELECT b.Bet_ID, b.Amount, b.GamesLeft, b.DateTime, bs.Name FROM Bet as b INNER JOIN BetState as bs ON b.BetState_ID = bs.BetState_ID WHERE b.User_ID = "
                + idUser + ";";

        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {
            Integer id = rs.getInt("Bet_ID");

            // acertar query
            query = "SELECT sb.Prediction, sb.Odd, sb.Event_ID, sb.Totalodds, bs.Name FROM ((SimpleBet as sb INNER JOIN BetState as bs ON sb.BetState_ID = bs.BetState_ID WHERE sb.Bet_ID = "
                    + id + ") INNER JOIN );";

            ResultSet rs2 = sqLiteJDBC2.executeQuery(query);

            List<Prediction> predictions = new ArrayList<>();
            while (rs2.next()) {
                Prediction p = new Prediction(rs2.getString("Prediction"), rs2.getFloat("Odd"),
                        rs2.getString("Event_ID"), null, rs2.getString("Name"));
                predictions.add(p);
            }
            sqLiteJDBC2.closeRS(rs2);

            Bet b = new Bet(rs.getInt("Bet_ID"), idUser, rs.getString("Name"), rs.getInt("GamesLeft"),
                    rs.getFloat("Amount"), LocalDateTime.parse(rs.getString("DateTime"), formatter),
                    rs.getFloat("Totalodds"), predictions);
            bets.add(b);
        }

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();

        HistoryBets h = new HistoryBets(null, bets);
        h.calculateWPercentage();

        return h;
    }

    /**
     * Add bet
     * 
     * @param bet
     * @throws SQLException
     */
    public static void add_Bet(Bet bet) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        SQLiteJDBC2 sqLiteJDBC2 = new SQLiteJDBC2();
        StringBuilder sb = new StringBuilder();

        Integer idBetState = get_Bet_State(PENDING_STATUS);
        LocalDateTime dateTime = LocalDateTime.now();

        sb.append("INSERT INTO Bet (User_ID, BetState_ID, Amount, GamesLeft, DateTime, Totalodds) VALUES (");
        sb.append(bet.getIdUser()).append(", ");
        sb.append(idBetState).append(", ");
        sb.append(bet.getAmount()).append(", ");
        sb.append(bet.getGamesLeft()).append(", ");
        sb.append(SQLiteJDBC2.prepare_string(dateTime.format(formatter))).append(", ");
        sb.append(bet.getTotalOdds()).append(") RETURNING Bet_ID;");

        String query = sb.toString();
        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        for (Prediction prediction : bet.getPredictions()) {
            StringBuilder sb2 = new StringBuilder();
            sb.append("INSERT INTO SimpleBet (Bet_ID, Prediction, Odd, Event_ID) VALUES (");
            sb.append(rs.getInt("Bet_ID")).append(", ");
            sb.append(prediction.getPrediction()).append(", ");
            sb.append(prediction.getOdd()).append(", ");
            sb.append(prediction.getIdEvent()).append(");");

            query = sb2.toString();
            sqLiteJDBC2.executeQuery(query);
        }

        sqLiteJDBC2.close();
    }
}
