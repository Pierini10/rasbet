package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Entities.User;
import com.rasbet.backend.Exceptions.BadPasswordException;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class UserFacade {

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
     * @param birthday    (dd-MM-yyyy)
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
            @RequestParam(value = "NIF") int NIF,
            @RequestParam(value = "CC") int CC,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "pn") String phoneNumber,
            @RequestParam(value = "bday") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate birthday,
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

}
