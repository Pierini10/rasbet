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
     * Gets the ID of a bet state
     * 
     * @param state
     * @return Id of the state
     * @throws SQLException
     */
    public static Integer get_Bet_State(String state) throws SQLException {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();

        String query = "SELECT BetState_ID FROM BetState WHERE Name = " + SQLiteJDBC.prepare_string(state) + ";";
        ResultSet rs = sqLiteJDBC.executeQuery(query);
        int id = rs.getInt("BetState_ID");

        sqLiteJDBC.closeRS(rs);
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
        SQLiteJDBC sqLiteJDBC2 = new SQLiteJDBC();
        List<Bet> res = new ArrayList<>();

        String query = "SELECT b.Bet_ID, b.Amount, b.GamesLeft, b.DateTime, b.Totalodds, bs.Name FROM Bet as b INNER JOIN BetState as bs ON b.BetState_ID = bs.BetState_ID WHERE b.User_ID = "
                + idUser + ";";

        ResultSet rs = sqLiteJDBC2.executeQuery(query);

        while (rs.next()) {
            Bet b = new Bet(rs.getInt("Bet_ID"), idUser, rs.getString("Name"), rs.getFloat("Amount"),
                    rs.getString("DateTime"), rs.getFloat("Totalodds"),
                    rs.getInt("GamesLeft"), new ArrayList<>());
            res.add(b);
        }

        for (Bet bet : res) {
            query = "SELECT sb.Prediction, sb.Event_ID, sb.Odd, bs.Name FROM SimpleBet as sb INNER JOIN BetState as bs ON sb.BetState_ID = bs.BetState_ID WHERE sb.Bet_ID = "
                    + bet.getId() + ";";

            ResultSet rs2 = sqLiteJDBC2.executeQuery(query);

            List<Prediction> predictions = new ArrayList<>();
            while (rs2.next()) {
                Prediction p = new Prediction(rs2.getString("Prediction"), rs2.getFloat("Odd"),
                        null, rs2.getString("Event_ID"), rs2.getString("Name"));

                predictions.add(p);
            }

            for (Prediction prediction : predictions) {
                ResultSet rs3 = sqLiteJDBC2.executeQuery("SELECT Description FROM Event WHERE Event_ID = "
                        + SQLiteJDBC.prepare_string(prediction.getIdEvent()) + ";");

                prediction.setEvent(rs3.getString("Description"));
            }
            bet.setPredictions(predictions);
        }

        sqLiteJDBC2.closeRS(rs);
        sqLiteJDBC2.close();

        HistoryBets h = new HistoryBets(null, res);
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
        Integer idBetState = get_Bet_State(PENDING_STATUS);

        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        StringBuilder sb = new StringBuilder();
        LocalDateTime dateTime = LocalDateTime.now();

        sb.append("INSERT INTO Bet (User_ID, BetState_ID, Amount, GamesLeft, DateTime, Totalodds) VALUES (");
        sb.append(bet.getIdUser()).append(", ");
        sb.append(idBetState).append(", ");
        sb.append(bet.getAmount()).append(", ");
        sb.append(bet.getGamesLeft()).append(", ");
        sb.append(SQLiteJDBC.prepare_string(dateTime.format(formatter))).append(", ");
        sb.append(bet.getTotalOdds()).append(") RETURNING Bet_ID;");

        String query = sb.toString();
        ResultSet rs = sqLiteJDBC.executeQuery(query);
        Integer bet_ID = rs.getInt("Bet_ID");

        for (Prediction prediction : bet.getPredictions()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("INSERT INTO SimpleBet (Bet_ID, BetState_ID, Prediction, Odd, Event_ID) VALUES (");
            sb2.append(bet_ID).append(", ");
            sb2.append(idBetState).append(", ");
            sb2.append(SQLiteJDBC.prepare_string(prediction.getPrediction())).append(", ");
            sb2.append(prediction.getOdd()).append(", ");
            sb2.append(SQLiteJDBC.prepare_string(prediction.getIdEvent())).append(");");

            query = sb2.toString();
            sqLiteJDBC.executeUpdate(query);
        }

        sqLiteJDBC.closeRS(rs);
    }

    
}
