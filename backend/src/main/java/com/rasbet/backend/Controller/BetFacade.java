package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.rasbet.backend.Security.Service.RasbetTokenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
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
@CrossOrigin(origins = "*")
public class BetFacade {

    @Autowired
    JwtDecoder jwtDecoder;

    /**
     * Make a bet.
     * 
     * @param amount
     * @param paymentMethod
     * @param simpleBets
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
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "amount") Float amount,
            @RequestParam(value = "paymentMethod") String paymentMethod,
            @RequestBody List<Prediction> simpleBets)
    {
        token = RasbetTokenDecoder.parseToken(token);

        try {
            int idUser = new RasbetTokenDecoder(token, jwtDecoder).getId();
            UserDB.assert_is_Normal(idUser);
            List<String> idEvents = simpleBets.stream().map(Prediction::getIdEvent).collect(Collectors.toList());
            boolean r = EventsDB.checkEventsAreOpen(idEvents);

            if (r) {
                Bet bet = new Bet(null, idUser, null, amount, null, null, simpleBets.size(), simpleBets);
                bet.calculateTotalOdds();
                BetDB.add_Bet(bet);
                if (TransactionDB.needsDeposit(paymentMethod))
                    TransactionDB.addTransaction(idUser, "Deposit", amount.doubleValue(), null);
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
    public HistoryBets getBetsHistory(@RequestHeader("Authorization") String token)
    {
        token = RasbetTokenDecoder.parseToken(token);

        HistoryBets r;

        try {
            r = BetDB.get_Bets(new RasbetTokenDecoder(token, jwtDecoder).getId());
        } catch (SQLException e) {
            r = null;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException", e);
        }

        return r;
    }

}
