package com.rasbet.backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.Database.PromotionDB;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
public class PromotionFacade {

    @Operation(summary = "Create a promotion")
    @PostMapping("/createPromotion")
    public void createPromotion(
            @Parameter(name = "id", description = "Id of the user that will add the promotion") int id,
            @Parameter(name = "code", description = "Code to get the promotion") String code,
            @Parameter(name = "descripton", description = "Description of the promotion") String description,
            @Parameter(name = "discount", description = "Discount of the promotion") double value,
            @Parameter(name = "minValue", description = "Minimum value to get the promotion") double minValue,
            @Parameter(name = "type", description = "Type of the promotion (percentage/absolute") String type) {

        try {
            PromotionDB.createPromotion(id, code, description, value, minValue, type);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating promotion", e);
        }

    }

    @Operation(summary = "Delete a promotion")
    public void deletePromotion(
            @Parameter(name = "id", description = "Id of the user that will delete the promotion") int id,
            @Parameter(name = "code", description = "Code of the promotion to delete") String code) {
        try {
            PromotionDB.deletePromotion(id, code);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting promotion", e);
        }
    }

    // TODO: MISSING MODIFY PROMOTION
}
