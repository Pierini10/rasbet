package com.rasbet.backend.Controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Entities.User;
import com.rasbet.backend.Exceptions.BadPasswordException;
import com.rasbet.backend.Exceptions.NoAuthorizationException;
import com.rasbet.backend.Requests.SignUpRequest;
import com.rasbet.backend.Security.Service.RasbetTokenDecoder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class UserFacade {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtDecoder jwtDecoder;

    /**
     * Register user.
     *
     */
    @Operation(summary = "Register user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register successful"),
            @ApiResponse(responseCode = "400", description = "Could not register") })
    @PostMapping("/register")
    public void register(
            @RequestBody SignUpRequest signUpRequest,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            User new_user = new User(
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword(),
                    this.encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),
                    signUpRequest.getNIF(),
                    signUpRequest.getCC(),
                    signUpRequest.getAddress(),
                    signUpRequest.getPhoneNumber(),
                    signUpRequest.getBirthday(),
                    signUpRequest.getRole());

            // Get user role from bearer token (if there is a token available).
            String userRequestRole = UserDB.NORMAL_ROLE;
            if (token != null) {
                token = RasbetTokenDecoder.parseToken(token);
                RasbetTokenDecoder rasbetTokenDecoder = new RasbetTokenDecoder(token, jwtDecoder);
                userRequestRole = rasbetTokenDecoder.getRole();
            }

            UserDB.create_User(new_user, userRequestRole);
        } catch (SQLException | BadPasswordException | NoAuthorizationException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Change user information.
     * Receiving only the parameters that are to be changed.
     * 
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
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestHeader(value = "Authorization") String token) {
        try {
            token = RasbetTokenDecoder.parseToken(token);
            RasbetTokenDecoder rasbetTokenDecoder = new RasbetTokenDecoder(token, jwtDecoder);
            User user = UserDB.get_User(rasbetTokenDecoder.getId());
            assert user != null;
            String encodedPassword = password;
            if (password != null) {
                encodedPassword = this.encoder.encode(password);
            }
            user.update_info(email, password, encodedPassword, firstName, lastName, address, phoneNumber);
            UserDB.update_User(user);
        } catch (BadPasswordException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Get user.
     *
     */
    @Operation(summary = "Get user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user successful"),
            @ApiResponse(responseCode = "400", description = "Could not get info") })
    @GetMapping("/getUser")
    public User getUser(
            @RequestHeader(value = "Authorization") String token) {
        try {
            if (token != null) {
                token = RasbetTokenDecoder.parseToken(token);
                RasbetTokenDecoder rasbetTokenDecoder = new RasbetTokenDecoder(token, jwtDecoder);
                return UserDB.get_User(rasbetTokenDecoder.getId());
            } else
                return null;
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
