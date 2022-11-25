package com.rasbet.backend.Controller;

import java.sql.SQLException;

import com.rasbet.backend.Security.Service.RasbetTokenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.PromotionDB;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class PromotionFacade {

    @Autowired
    JwtDecoder jwtDecoder;

    @Operation(summary = "Create a promotion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promotion created"),
            @ApiResponse(responseCode = "401", description = "No authorization"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping("/createPromotion")
    public void createPromotion(
            @RequestHeader("Authorization") String token,
            @RequestParam() String code,
            @RequestParam() String description,
            @RequestParam() double discount,
            @RequestParam() double minValue,
            @RequestParam() int type)
    {
        token = RasbetTokenDecoder.parseToken(token);


        try {
            PromotionDB.createPromotion(new RasbetTokenDecoder(token, jwtDecoder).getId(), code, description, discount, minValue, type);
        } catch (NoAuthorizationException e) {

            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Operation(summary = "Delete a promotion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promotion created"),
            @ApiResponse(responseCode = "401", description = "No authorization"),
            @ApiResponse(responseCode = "500", description = "SQL error")
    })
    @PostMapping("/deletePromotion")
    public void deletePromotion(
            @RequestHeader("Authorization") String token,
            @RequestParam() String code)
    {
        token = RasbetTokenDecoder.parseToken(token);

        try {
            PromotionDB.deletePromotion(new RasbetTokenDecoder(token, jwtDecoder).getId(), code);
        } catch (NoAuthorizationException e) {

            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SQLException e) {

            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Modify a promotion")
    @PostMapping("/modifyPromotion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promotion created"),
            @ApiResponse(responseCode = "401", description = "No authorization"),
            @ApiResponse(responseCode = "500", description = "SQL error")
    })
    public void modifyPromotion(
            @RequestHeader("Authorization") String token,
            @RequestParam() String code,
            @RequestParam() String description,
            @RequestParam() double discount,
            @RequestParam() double minValue,
            @RequestParam() int type)
    {
        token = RasbetTokenDecoder.parseToken(token);

        try {
            PromotionDB.updatePromotion(new RasbetTokenDecoder(token, jwtDecoder).getId(), code, description, discount, minValue, type);
        } catch (NoAuthorizationException e) {

            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error modifying promotion", e);
        }
    }
}
