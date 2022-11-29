package com.rasbet.backend.Controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.rasbet.backend.Security.Service.RasbetTokenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.rasbet.backend.BackendApplication;
import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.Database.OddDB;
import com.rasbet.backend.Database.SportsDB;
import com.rasbet.backend.Database.UserDB;
import com.rasbet.backend.Entities.Event;
import com.rasbet.backend.Requests.UpdateOddRequest;
import com.rasbet.backend.Exceptions.NoAuthorizationException;
import com.rasbet.backend.Exceptions.SportDoesNotExistExeption;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class EventsFacade {

    @Autowired
    JwtDecoder jwtDecoder;

    /**
     * Get current events information.
     *
     * @return List of Events containing:
     *         0: Event IDEvents
     *         5: Possible Bets
     *         [
     *         (Name, Odd),
     *         ]
     *         6: Event Status (0: Not started, 1: In progress, 2: Final Result)
     */
    @Operation(summary = "Gets current events information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting events successful"),
            @ApiResponse(responseCode = "400", description = "Something went wrong fetching data") })
    @GetMapping("/getEvents")
    public List<Event> getEvents(
            @RequestParam(name = "sport") String sport) {
        try {
            BackendApplication.t.signal(false);
            return EventsDB.get_Events(sport);
        } catch (SportDoesNotExistExeption | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Update events and bet information.
     */
    @Operation(summary = "Updates events and bets information. Every 5 minutes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successful"),
            @ApiResponse(responseCode = "400", description = "Something went wrong fetching data") })
    @GetMapping("/updateEvents")
    public void updateEvents() {
        BackendApplication.t.signal(true);
    }

    /**
     * Adds a new Event.
     * 
     * @param sport
     * @param datetime
     * @param description (yyyy-MM-ddThh:mm:ss)
     * 
     */
    @Operation(summary = "Adds a new Event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event added was successful."),
            @ApiResponse(responseCode = "400", description = "Addidicion failed.") })
    @PostMapping("/addEvent")
    public void addEvent(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "sport") String sport,
            @RequestParam(name = "competition") String competition,
            @RequestParam(name = "datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime datetime,
            @RequestParam(name = "description")  String description)
    {
        token = RasbetTokenDecoder.parseToken(token);

        Event event = new Event(null, sport, competition, datetime, description, null, null, null);
        try {
            UserDB.assert_is_Specialist(new RasbetTokenDecoder(token, jwtDecoder).getId());
            EventsDB.add_Event(event);
        } catch (SportDoesNotExistExeption | NoAuthorizationException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Change event state.
     * 
     * @param idEvent
     * @param state
     * @return True if the change was successful, false otherwise.
     */
    @Operation(summary = "Change state of event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change successful"),
            @ApiResponse(responseCode = "400", description = "This User does not have authorization to change bet state"),
            @ApiResponse(responseCode = "500", description = "SqlException") })
    @PostMapping("/changeEventState")
    public void changeEventState(
            @RequestParam(value = "idEvent") String idEvent,
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "state") String state)
    {
        token = RasbetTokenDecoder.parseToken(token);

        try {
            EventsDB.update_Event_State(idEvent, new RasbetTokenDecoder(token, jwtDecoder).getId(), state);

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException", e);
        } catch (NoAuthorizationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Get all sports
     * 
     * @return List of sports
     */
    @Operation(summary = "Get all sports.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All sports."),
            @ApiResponse(responseCode = "500", description = "SQLException.") })
    @GetMapping("/getAllSports")
    public List<String> getAllSports()
    {

        try {
            return SportsDB.getSports();

        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQLException");
        }
    }

    /**
     * Insert new ODD.
     * list   containing:
     *               0: Event ID
     *               PossibleBets
     *               [
     *               (Name, Odd),
     *               ]
     * 
     * @return True if insertion was successful, false otherwise.
     */
    @Operation(summary = "Insert new ODD.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insertion successful"),
            @ApiResponse(responseCode = "401", description = "This User does not have authorization to change ODD'S"),
            @ApiResponse(responseCode = "500", description = "SqlException") })
    @PostMapping("/insertOdd")
    public void insertOdd(@RequestBody UpdateOddRequest possibleBets) {

        try {
            OddDB.updateOdds(possibleBets);

        } catch (NoAuthorizationException e) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
