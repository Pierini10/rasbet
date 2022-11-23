package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.TransactionDB;
import com.rasbet.backend.Entities.Transaction;
import com.rasbet.backend.Exceptions.NoAmountException;
import com.rasbet.backend.Exceptions.NoMinimumValueException;
import com.rasbet.backend.Exceptions.NoPromotionCodeException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class WalletFacade {

     /**
     * Withdraw and deposit money.
     * 
     * @param userID
     * @param amount (negative for withdraw, positive for deposit)
     * 
     * @return Balance after transaction or -1 if transaction failed.
     */
    @Operation(summary = "Withdraw and deposit money.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction was successful."),
            @ApiResponse(responseCode = "400", description = "Transaction failed."),
            @ApiResponse(responseCode = "500", description = "SQLException.") })
    @PostMapping("/withdrawDeposit")
    public double withdrawDeposit(
            @RequestParam() int userID,
            @RequestParam() double amount,
            @RequestParam(required = false) String promotionCode,
            @RequestParam(required = false) String method) {
        String transactionType = "levantamento";
        if (amount > 0) {
            transactionType = "deposito";
        }
        try {
            return TransactionDB.addTransaction(userID, transactionType, amount, promotionCode);
        } catch (NoAmountException | NoPromotionCodeException | NoMinimumValueException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException");
        }
    }

    /**
     * Get user's transactions history.
     * 
     * @param userID
     * 
     * 
     * @return List containing:
     *         0: Date (yyyy-MM-dd)
     *         1: Time (HH:mm)
     *         2: Description
     *         3: Amount
     *         4: PostTransactionBalance
     */
    @Operation(summary = "Get user's transactions history.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User's transactions history."),
            @ApiResponse(responseCode = "500", description = "SQLException.")
    })
    @GetMapping("/getTransactionsHistory")
    public ArrayList<Transaction> getTransactionsHistory(
            @RequestParam() int userID) {
        try {

            return TransactionDB.getTransactions(userID);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "SQLException", e);
        }
    }
    
}
