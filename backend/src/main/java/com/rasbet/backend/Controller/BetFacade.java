package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.BetDB;
import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.Database.TransactionDB;
import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Entities.Bet;
import com.rasbet.backend.Entities.HistoryBets;
import com.rasbet.backend.Entities.Prediction;
import com.rasbet.backend.Exceptions.NoAmountException;
import com.rasbet.backend.Exceptions.NoAuthorizationException;
import com.rasbet.backend.Exceptions.NoMinimumValueException;
import com.rasbet.backend.Exceptions.NoPromotionCodeException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class BetFacade {

    /**
     * Make a bet.
     * 
     * @param userID
     * @param amount
     * @param paymentMethod
     * @param SimpleBets
     *                      [
     *                      (eventID, prediction, odd),
     *                      ]
     * 
     * @return True if bet was successful, false otherwise.
     */
    @Operation(summary = "Makes a bet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet was made"),
            @ApiResponse(responseCode = "400", description = "Bet was not possible to make"),
            @ApiResponse(responseCode = "500", description = "SqlException") })
    @PostMapping("/makeBet")
    public void makeBet(
            @RequestParam(value = "userID") int idUser,
            @RequestParam(value = "amount") Float amount,
            @RequestParam(value = "paymentMethod") String paymentMethod,
            @RequestBody List<Prediction> simpleBets) {

        try {
            List<String> idEvents = simpleBets.stream().map(sb -> sb.getIdEvent()).collect(Collectors.toList());
            Boolean r = EventsDB.checkEventsAreOpen(idEvents);

            if (r) {
                Bet bet = new Bet(null, idUser, null, amount, null, null, simpleBets.size(), simpleBets);
                bet.calculateTotalOdds();
                BetDB.add_Bet(bet);
                if (TransactionDB.needsDeposit(paymentMethod))
                    TransactionDB.addTransaction(idUser, "Deposit", amount.doubleValue(), null);
                UserDB.assert_is_Normal(idUser);
                TransactionDB.addTransaction(idUser, "Bet", -amount.doubleValue(), null);
            } else {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "The events are not open");
            }
        } catch (SQLException | NoAmountException | NoPromotionCodeException |
         NoMinimumValueException | IllegalArgumentException | NoAuthorizationException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Get user's bets history.
     * 
     * @param userID
     * 
     * @return List containing:
     *         0: Bet ID
     *         SimpleBet
     *         [
     *         0: Description (Porto x Benfica)
     *         1: Prediction (Porto)
     *         2: Result (Porto)
     *         3: Amount
     *         4: Profit
     *         ]
     */
    @Operation(summary = "Get user's bet history.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User's bet history."),
            @ApiResponse(responseCode = "500", description = "SQLException.")
    })
    @GetMapping("/getBetsHistory")
    public HistoryBets getBetsHistory(
            @RequestParam(value = "userID") int userID) {
        HistoryBets r;

        try {
            r = BetDB.get_Bets(userID);
        } catch (SQLException e) {
            r = null;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException", e);
        }

        return r;
    }

}