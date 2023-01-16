package com.rasbet.backend.Controller;

import com.rasbet.backend.Database.FollowDB;
import com.rasbet.backend.Security.Service.RasbetTokenDecoder;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class FollowFacade
{

    @Autowired
    JwtDecoder jwtDecoder;

    @Operation(summary = "Follow an event.")
    @PostMapping("/followEvent")
    public void followEvent(
            @RequestHeader("Authorization") String token,
            @RequestParam() int event_id)
    {
        token = RasbetTokenDecoder.parseToken(token);

        // Get user_id from token
        int user_id = new RasbetTokenDecoder(token, jwtDecoder).getId();

        // Follow event: insert (user_id, event_id) into follow table
        try
        {
            FollowDB.followEvent(true, user_id, event_id);
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
            @RequestParam() int event_id) {
        token = RasbetTokenDecoder.parseToken(token);

        // Get user_id from token
        int user_id = new RasbetTokenDecoder(token, jwtDecoder).getId();

        // Unfollow event: delete (user_id, event_id) from follow table
        try {
            FollowDB.followEvent(false, user_id, event_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
