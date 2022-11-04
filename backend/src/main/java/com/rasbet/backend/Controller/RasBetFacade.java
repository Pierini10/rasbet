package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.rasbet.backend.Database.OddDB;
import com.rasbet.backend.Database.SportsDB;
import com.rasbet.backend.Database.TransactionDB;
import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Entities.Bet;
import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Entities.HistoryBets;
import com.rasbet.backend.Entities.Prediction;
import com.rasbet.backend.Entities.Transaction;
import com.rasbet.backend.Entities.UpdateOddRequest;
import com.rasbet.backend.Entities.User;
import com.rasbet.backend.Exceptions.BadPasswordException;
import com.rasbet.backend.Exceptions.NoAmountException;
import com.rasbet.backend.Exceptions.NoAuthorizationException;
import com.rasbet.backend.Exceptions.NoMinimumValueException;
import com.rasbet.backend.Exceptions.NoPromotionCodeException;
import com.rasbet.backend.Exceptions.SportDoesNotExistExeption;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class RasBetFacade {

    private LocalDateTime lastEventsUpdate;

    private boolean can_update() {
        LocalDateTime now = LocalDateTime.now();
        if (lastEventsUpdate == null || lastEventsUpdate.isBefore(now.minusMinutes(5))) {
            lastEventsUpdate = now;
            return true;
        } else {
            return false;
        }
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
    @Operation(summary = "Register user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register successful"),
            @ApiResponse(responseCode = "400", description = "Could not register") })
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
            User new_user = new User(email, password, firstName, lastName, NIF, CC, address, phoneNumber, birthday,
                    role);
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
    @Operation(summary = "Login user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Could not login") })
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
    @Operation(summary = "Changes user information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changing user successful"),
            @ApiResponse(responseCode = "400", description = "Could not change user info") })
    @PostMapping("/changeInfo")
    public void changeInfo(
            @RequestParam(name = "userID") int userID,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber) {
        try {
            User user = UserDB.get_User(userID);
            user.update_info(email, password, firstName, lastName, address, phoneNumber);
            UserDB.update_User(user);
        } catch (BadPasswordException | SQLException e) {
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
    @Operation(summary = "Gets current events information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting events successful"),
            @ApiResponse(responseCode = "400", description = "Something went wrong fetching data") })
    @GetMapping("/getEvents")
    public List<Event> getEvents(
            @RequestParam(name = "sport") String sport) {
        try {
            if (can_update())
                updateEvents();
            return EventsDB.get_Events(sport);
        } catch (SportDoesNotExistExeption | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Update events and bet information.
     */
    @Operation(summary = "Updates events and bets information. Every 5 minutes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successful"),
            @ApiResponse(responseCode = "400", description = "Something went wrong fetching data") })
    @GetMapping("/updateEvents")
    public void updateEvents() {
        try {
            EventsDB.update_Database();
        } catch (SportDoesNotExistExeption | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Adds a new Event.
     * 
     * @param userID
     * @param sport
     * @param datetime
     * @param description
     * 
     */
    @Operation(summary = "Adds a new Event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event added was successful."),
            @ApiResponse(responseCode = "400", description = "Addidicion failed.") })
    @PostMapping("/addEvent")
    public void addEvent(
            @RequestParam(name = "userID") int userID,
            @RequestParam(name = "sport") String sport,
            @RequestParam(name = "datetime") String datetime,
            @RequestParam(name = "description") String description) {
        Event event = new Event(null, sport, datetime, description, null, null, null);
        try {
            UserDB.assert_is_Specialist(userID);
            EventsDB.add_Event(event);
        } catch (SportDoesNotExistExeption | NoAuthorizationException | SQLException e) {
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

                TransactionDB.addTransaction(idUser, "bet", amount.doubleValue(), null);
            } else {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "The events are not open");
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException", e);
        } catch (NoAmountException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "No amount", e);
        } catch (NoPromotionCodeException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "No promotion code", e);
        } catch (NoMinimumValueException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Below minimum amount", e);
        }
    }

    /**
     * Change event state.
     * 
     * @param idEvent
     * @param userID  User that is trying to change the state
     * @param state
     * @return True if the change was successful, false otherwise.
     */
    @Operation(summary = "Change state of event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change successful"),
            @ApiResponse(responseCode = "400", description = "This User does not have authorization to change bet state"),
            @ApiResponse(responseCode = "500", description = "SqlException") })
    @PostMapping("/changeEventState")
    public void changeEventState(
            @RequestParam(value = "idEvent") String idEvent,
            @RequestParam(value = "idUser") int idUser,
            @RequestParam(value = "state") String state) {

        try {
            EventsDB.update_Event_State(idEvent, idUser, state);

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException", e);
        } catch (NoAuthorizationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, e.getMessage());
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
            @ApiResponse(responseCode = "401", description = "This User does not have authorization to change ODD'S"),
            @ApiResponse(responseCode = "500", description = "SqlException") })
    @PostMapping("/insertOdd")
    public void insertOdd(@RequestBody UpdateOddRequest possibleBets) {

        try {
            OddDB.updateOdds(possibleBets);

        } catch (NoAuthorizationException e) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
     * Get all sports
     * 
     * @return List of sports
     */
    @Operation(summary = "Get all sports.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All sports."),
            @ApiResponse(responseCode = "500", description = "SQLException.") })
    @GetMapping("/getAllSports")
    public List<String> getAllSports() {

        try {
            return SportsDB.getSports();

        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException");
        }
    }
}
