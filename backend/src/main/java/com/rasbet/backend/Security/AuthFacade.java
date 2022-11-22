package com.rasbet.backend.Security;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasbet.backend.Security.Service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class AuthFacade {
    private static final Logger LOG = LoggerFactory.getLogger(AuthFacade.class);

    private final TokenService tokenService;

    public AuthFacade(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping
    public String home(Principal principal) {
        return "Hello, " + (principal != null ? principal.getName() : "user") + "!";
    }

    // DB Credentials:
    // email: test@auth.com
    // passw: Test3aut!
    //
    @Operation(summary = "Login user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Could not login"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials") })
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public String token(Authentication authentication) {
        LOG.debug("Token requested for user '{}'.", authentication.getName());
        String token = this.tokenService.generateToken(authentication);
        LOG.debug("Token granted '{}'.", token);
        return token;
    }
}
