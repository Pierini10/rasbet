package com.rasbet.backend.Controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.PromotionDB;
import com.rasbet.backend.Exceptions.NoAuthorizationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class PromotionFacade {

    @Operation(summary = "Create a promotion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promotion created"),
            @ApiResponse(responseCode = "401", description = "No authorization"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping("/createPromotion")
    public void createPromotion(
            @RequestParam() int requesterId,
            @RequestParam() String code,
            @RequestParam() String description,
            @RequestParam() double discount,
            @RequestParam() double minValue,
            @RequestParam() int type) {

        try {
            PromotionDB.createPromotion(requesterId, code, description, discount, minValue, type);
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
            @RequestParam() int requesterId,
            @RequestParam() String code) {
        try {
            PromotionDB.deletePromotion(requesterId, code);
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
            @RequestParam() int requesterId,
            @RequestParam() String code,
            @RequestParam() String description,
            @RequestParam() double discount,
            @RequestParam() double minValue,
            @RequestParam() int type) {
        try {
            PromotionDB.updatePromotion(requesterId, code, description, discount, minValue, type);
        } catch (NoAuthorizationException e) {

            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error modifying promotion", e);
        }
    }
}
