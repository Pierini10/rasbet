package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.BetDB;
import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.Database.OddDB;
import com.rasbet.backend.Database.TransactionDB;
import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Entities.Bet;
import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Entities.Transaction;
import com.rasbet.backend.Entities.UpdateOddRequest;
import com.rasbet.backend.Entities.User;
import com.rasbet.backend.Exceptions.BadPasswordException;
import com.rasbet.backend.Exceptions.NoAmountException;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class RasBetFacade {

    private LocalDateTime lastEventsUpdate;

    private boolean can_update(){
        LocalDateTime now = LocalDateTime.now();
        if (lastEventsUpdate == null || lastEventsUpdate.isBefore(now.minusMinutes(5))){
            lastEventsUpdate = now;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check backend connection.
     */
    @GetMapping("/checkConnectivity")
    public String checkConnection() {
        return "Backend is live!";
    }

    // TODO:
    // Logout ? probably token related...
    // Notifications?
    //
    // Create class for user, event, bet, transaction

    /**
     * Register user.
     * 
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param NIF
     * @param CC          (Citizen Card)
     * @param address
     * @param phoneNumber
     * @param Birthday    (yyyy-MM-dd)
     * 
     */
    @PostMapping("/register")
    public void register(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "pw") String password,
            @RequestParam(value = "fn") String firstName,
            @RequestParam(value = "ln") String lastName,
            @RequestParam(value = "NIF") String NIF,
            @RequestParam(value = "CC") String CC,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "pn") String phoneNumber,
            @RequestParam(value = "bday") String birthday,
            @RequestParam(value = "role") String role,
            @RequestParam(value = "userRequestID") int userRequestID) {
            try {
                User new_user = new User(email, password, firstName, lastName, NIF, CC, address, phoneNumber, birthday, role);
                UserDB.create_User(new_user, userRequestID);
            } catch (SQLException | BadPasswordException | NoAuthorizationException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        
    }

    /**
     * Login user.
     * 
     * @param email
     * @param password
     * 
     * @return User containing:
     *         0: User ID
     *         1: Balance
     *         3: Name
     *         4: Surname
     *         5: role
     */
    @PostMapping("/login")
    public User login(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "pw") String password) {
        User user = new User(email, password);
        try {
            int status = UserDB.get_User(user);
            if (status < 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Password is Wrong!");
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return user;
    }

    /**
     * Change user information.
     * Receiving only the parameters that are to be changed.
     * 
     * @param userID
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param address
     * @param phoneNumber
     * 
     */
    @PostMapping("/changeInfo")
    public void changeInfo(
            @RequestParam(value = "userID") int userID,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "pw", required = false) String password,
            @RequestParam(value = "fn", required = false) String firstName,
            @RequestParam(value = "ln", required = false) String lastName,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "pn", required = false) String phoneNumber) {
        try {
            User user = UserDB.get_User(userID);
            user.update_info(email, password, firstName, lastName, address, phoneNumber);
            UserDB.update_User(user);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Get current events information.
     *
     * @return List of Events containing:
     *         0: Event ID
     *         1: Sport Name
     *         2: Event Description (Porto x Benfica)
     *         3: Event Date (yyyy-MM-dd)
     *         4: Event Time (HH:mm)
     *         5: Possible Bets
     *         [
     *         (Name, Odd),
     *         ]
     *         6: Event Status (0: Not started, 1: In progress, 2: Final Result)
     */
    @GetMapping("/getEvents")
    public List<Event> getEvents() {
        try {
            if (can_update()) updateEvents();
            return EventsDB.get_Events();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Update events and bet information.
     */
    @GetMapping("/updateEvents")
    public void updateEvents() {
        try {
            EventsDB.update_Database();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

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
    @PostMapping("/makeBet")
    public boolean makeBet(
            @RequestParam(value = "userID") int userID,
            @RequestParam(value = "amount") double amount,
            @RequestParam(value = "paymentMethod") String paymentMethod,
            @RequestParam(value = "simpleBets") List<List<String>> simpleBets) {
        // TODO:
        // boolean r = true;
        //
        // try {
        // Integer idState = BetDB.get_Bet_State(state);
        // Bet bet = new Bet(idBet, idUser, idState, idState, null);
        // BetDB.update_Bet(bet);
        // } catch (SQLException e) {
        // r = false;
        // System.err.println(e.getClass().getName() + ": " + e.getMessage());
        // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        // }
        //
        // return r;
        return false;
    }

    /**
     * Change bet state.
     * 
     * vale a pena o userid??
     * 
     * @param idBet
     * @param userID
     * @param state
     * @return True if the change was successful, false otherwise.
     */
    @PostMapping("/changeBetState")
    public boolean changeBetState(
            @RequestParam(value = "idBet") int idBet,
            @RequestParam(value = "idUser") int idUser,
            @RequestParam(value = "state") String state) {
        boolean r = true;

        try {
            String userPermissions = UserDB.get_Role(idUser);

            if (userPermissions.equals("Administrator") || userPermissions.equals("Specialist")) {
                Bet bet = new Bet(idBet, null, state, null, null, null, null);
                BetDB.update_Bet(bet);
            } else {
                r = false;
            }
        } catch (SQLException e) {
            r = false;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        }

        return r;
    }

    /**
     * Get user's bets history.
     * 
     * @param userID
     * 
     *               TODO: list of class bet
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
    @GetMapping("/getBetsHistory")
    public List<Bet> getBetsHistory(
            @RequestParam(value = "userID") int userID) {
        List<Bet> r;

        try {
            r = BetDB.get_Bets(userID);
        } catch (SQLException e) {
            r = null;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        }

        return r;
    }

    /**
     * Get user's transactions history.
     * 
     * @param userID
     * 
     *               TODO: return list of class transaction
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
            @Parameter(name = "userID", description = "User ID that wants to see his transactions history") int userID) {
        try {

            return TransactionDB.getTransactions(userID);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "SQLException", e);
        }
    }

    /**
     * Insert new ODD.
     * 
     * @param UserID
     * @param List   containing:
     *               0: Event ID
     *               PossibleBets
     *               [
     *               (Name, Odd),
     *               ]
     * 
     * @return True if insertion was successful, false otherwise.
     */
    @Operation(summary = "Insert new ODD.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insertion successful"),
            @ApiResponse(responseCode = "400", description = "This User does not have authorization to change ODD'S"),
            @ApiResponse(responseCode = "500", description = "SqlException") })
    @PostMapping("/insertOdd")
    public boolean insertOdd(@RequestBody UpdateOddRequest possibleBets) {

        try {
            return OddDB.updateOdds(possibleBets);

        } catch (NoAuthorizationException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No authorization", e);
        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException");
        }
    }

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
            @ApiResponse(responseCode = "400", description = "Transaction failed.") })
    @PostMapping("/withdrawDeposit")
    public double withdrawDeposit(
            @Parameter(name = "userID", description = "User ID that wants to make the transaction") int userID,
            @Parameter(name = "amount", description = "Amount that is being transacted") double amount) {
        String transactionType = "levantamento";
        if (amount > 0) {
            transactionType = "deposito";
        }
        try {
            return TransactionDB.addTransaction(userID, transactionType, amount);
        } catch (NoAmountException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException");
        }
    }
    /**
     * TODO: Change bet status. Não percebi este requisito...
     * 
     */

    // TODO: REMOVE LATER, FOR TESTS ONLY
}
