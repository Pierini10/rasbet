package com.rasbet.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rasbet.backend.Database.FollowDB;
import com.rasbet.backend.Entities.SharedEventSubject;
import com.rasbet.backend.Security.Service.RasbetTokenDecoder;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
public class FollowFacade
{

    @Autowired
    JwtDecoder jwtDecoder;
    
    @Autowired
    SharedEventSubject sharedEventSubject;

    @Operation(summary = "Follow an event.")
    @PostMapping("/followEvent")
    public void followEvent(
            @RequestHeader("Authorization") String token,
            @RequestParam() String event_id)
    {
        token = RasbetTokenDecoder.parseToken(token);

        // Get user_id from token
        int user_id = new RasbetTokenDecoder(token, jwtDecoder).getId();

        // Follow event: insert (user_id, event_id) into follow table
        try
        {
            sharedEventSubject.addFollow(user_id, event_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Unfollow an event.")
    @PostMapping("/unfollowEvent")
    public void unfollowEvent(
            @RequestHeader("Authorization") String token,
            @RequestParam() String event_id) {
        token = RasbetTokenDecoder.parseToken(token);

        // Get user_id from token
        int user_id = new RasbetTokenDecoder(token, jwtDecoder).getId();

        // Unfollow event: delete (user_id, event_id) from follow table
        try {
            sharedEventSubject.removeFollow(user_id, event_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Get all followed games by a user.")
    @GetMapping("/getFollowedGames")
    public List<String> getFollowedGames(@RequestHeader("Authorization") String token) {
        token = RasbetTokenDecoder.parseToken(token);

        // Get user_id from token
        int user_id = new RasbetTokenDecoder(token, jwtDecoder).getId();

        // Get all followed games by user
        try {
            return FollowDB.getFollowedEvents(user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
