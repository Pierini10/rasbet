package com.rasbet.backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.NotificationDB;
import com.rasbet.backend.Entities.RequestNotifications;

@RestController
public class NotificationFacade {

    @PostMapping("/createNotification")
    public void createNotification(
            @RequestParam() int requestUser,
            @RequestParam() int idUser,
            @RequestParam() String description) {
        try {
            NotificationDB.createNotification(idUser, description, requestUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/deleteNotification")
    public void deleteNotification(
            @RequestBody RequestNotifications request) {
        try {
            NotificationDB.deleteMultipleNotifications(request.getIdUser(), request.getDescription(),
                    request.getIdRequestUser());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
