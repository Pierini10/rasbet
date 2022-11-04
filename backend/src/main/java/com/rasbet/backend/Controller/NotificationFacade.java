package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.NotificationDB;
import com.rasbet.backend.Entities.RequestNotifications;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class NotificationFacade {

    @Operation(summary = "Create a notification for a given user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification created"),
            @ApiResponse(responseCode = "401", description = "User does not have Authorization"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/createNotification")
    public void createNotification(
            @RequestParam() int requestUser,
            @RequestParam() int idUser,
            @RequestParam() String description) {
        try {
            NotificationDB.createNotification(idUser, description, requestUser);
        } catch (NoAuthorizationException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "deletes all notifications of a specific user or a given list of notifications")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Notifications deleted"),
            @ApiResponse(responseCode = "401", description = "Request user has no authorization to delete notifications"),
            @ApiResponse(responseCode = "500", description = "Sql error")
    })
    @PostMapping("/deleteNotification")
    public void deleteNotification(
            @RequestBody RequestNotifications request) {
        try {
            NotificationDB.deleteMultipleNotifications(request.getIdUser(), request.getDescription(),
                    request.getIdRequestUser());
        } catch (NoAuthorizationException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get all notifications for a user or for the admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications found"),
            @ApiResponse(responseCode = "401", description = "User does not have Authorization"),
            @ApiResponse(responseCode = "500", description = "SQL Exception")
    })
    @GetMapping("/getNotifications")
    public List<String> getNotifications(@RequestParam() int idUser, @RequestParam() int requestUser) {
        try {
            return NotificationDB.getNotifications(idUser, requestUser);
        } catch (NoAuthorizationException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
