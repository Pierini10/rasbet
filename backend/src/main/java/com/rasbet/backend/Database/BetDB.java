package com.rasbet.backend.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import com.rasbet.backend.Entities.Bet;

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

        String idUser = bet.getIdUser() == null ? "" : ("User_ID = " + bet.getIdUser() + ", ");
        String idBetState = bet.getIdBetState() == null ? "" : ("BetState_ID = " + bet.getIdBetState() + ", ");
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
        return id;
    }
}
