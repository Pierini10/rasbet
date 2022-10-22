package com.rasbet.backend.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.TransactionDB;
import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Entities.Transaction;
import com.rasbet.backend.Entities.User;
import com.rasbet.backend.GamesAPI.GamesApi;

@RestController
public class RasBetFacade {

    /**
     * Check backend connection.
     */
    @GetMapping("/checkConnectivity")
    public String checkConnection() {
        try {
            GamesApi.getEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @return True if user was registered successfully, false otherwise.
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
            @RequestParam(value = "bday") String birthday) {
        User new_user = new User(email, password, firstName, lastName, NIF, CC, address, phoneNumber, birthday);
        try {
            UserDB.create_User(new_user);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "SQLException", e);
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
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Password is Wrong!");
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "SQLException", e);
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
     * @return True if change was successful, false otherwise.
     */
    @PostMapping("/changeInfo")
    public boolean changeInfo(
            @RequestParam(value = "userID") int userID,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "pw", required = false) String password,
            @RequestParam(value = "fn", required = false) String firstName,
            @RequestParam(value = "ln", required = false) String lastName,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "pn", required = false) String phoneNumber) {
        boolean r = true;
        try {
            User user = UserDB.get_User(userID);
            user.update_info(email, password, firstName, lastName, address, phoneNumber);
            UserDB.update_User(user);
        } catch (SQLException e) {
            r = false;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "SQLException", e);
        }
        return r;
    }

    /**
     * Get current events information.
     * 
     * TODO: RETURN list of class event
     * 
     * @return List containing:
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
    public List<List<String>> getEvents() {
        return new ArrayList<List<String>>();
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
        return false;
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
    public List<List<String>> getBetsHistory(
            @RequestParam(value = "userID") int userID) {
        return new ArrayList<List<String>>();
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
    @GetMapping("/getTransactionsHistory")
    public ArrayList<Transaction> getTransactionsHistory(
            @RequestParam(value = "userID") int userID) {
        try {

            return TransactionDB.getTransactions(userID);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "SQLException", e);
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
    @PostMapping("/insertOdd")
    public boolean insertOdd(
            @RequestParam(value = "userID") int userID,
            @RequestParam(value = "eventID") int eventID,
            @RequestParam(value = "possibleBets") List<List<String>> possibleBets) {
        return false;
    }

    /**
     * Withdraw and deposit money.
     * 
     * TODO: ter dois links diferentes a vir para este metodo...
     * tipo o link deposit e o link withdraw ambos vinham para aqui
     * e o metodo fazia a operacao correspondente...
     * 
     * @param userID
     * @param amount (negative for withdraw, positive for deposit)
     * 
     * @return Balance after transaction or -1 if transaction failed.
     */
    @PostMapping("/withdrawDeposit")
    public double withdrawDeposit(
            @RequestParam(value = "userID") int userID,
            @RequestParam(value = "amount") double amount) {
        String transactionType = "levantamento";
        if (amount > 0) {
            transactionType = "deposito";
        }
        return TransactionDB.addTransaction(userID, transactionType, amount);

    }

    /**
     * TODO: Change bet status. Não percebi este requisito...
     * 
     */

    // TODO: REMOVE LATER, FOR TESTS ONLY

}
