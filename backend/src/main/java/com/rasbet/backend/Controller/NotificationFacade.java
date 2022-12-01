package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.NotificationDB;
import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Exceptions.NoAuthorizationException;
import com.rasbet.backend.Security.Service.RasbetTokenDecoder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class NotificationFacade {

    @Autowired
    JwtDecoder jwtDecoder;

    @Operation(summary = "Create a notification for a given user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification created"),
            @ApiResponse(responseCode = "401", description = "User does not have Authorization"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/createNotification")
    public void createNotification(
            @RequestHeader("Authorization") String token,
            @RequestParam() String userEmail,
            @RequestParam() String description) {
        token = RasbetTokenDecoder.parseToken(token);

        try {
            int user_id = UserDB.get_User(userEmail).getId();
            NotificationDB.createNotification(user_id, description, new RasbetTokenDecoder(token, jwtDecoder).getId());
        } catch (NoAuthorizationException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Email does not exist notification has not been created");
        }
    }

    @Operation(summary = "Get all notifications for a user or for the admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications found"),
            @ApiResponse(responseCode = "500", description = "SQL Exception")
    })
    @GetMapping("/getNotifications")
    public List<String> getNotifications(
            @RequestHeader("Authorization") String token,
            @RequestParam() int lastNNotifications) {
        token = RasbetTokenDecoder.parseToken(token);
        try {
            return NotificationDB.getNotifications(new RasbetTokenDecoder(token, jwtDecoder).getId(),
                    lastNNotifications);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
