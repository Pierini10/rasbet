package com.rasbet.backend.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.rasbet.backend.BackendApplication;
import com.rasbet.backend.Entities.SharedEventSubject;
import com.rasbet.backend.Security.Service.RasbetTokenDecoder;
import com.rasbet.backend.Security.Service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class AuthFacade {

    @Autowired
    JwtDecoder jwtDecoder;

    @Autowired
    SharedEventSubject sharedEventSubject;

    private static final Logger LOG = LoggerFactory.getLogger(AuthFacade.class);

    private final TokenService tokenService;

    public AuthFacade(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/validToken")
    public boolean home(@RequestHeader("Authorization") String token) {
        BackendApplication.t.updateSharedEventSubject(sharedEventSubject);
        token = RasbetTokenDecoder.parseToken(token);
        return true;

    }

    // DB Credentials:
    // email: os que estão na base de dados
    // passw: Test3aut!
    //
    @Operation(summary = "Login user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Could not login"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials") })
    @PostMapping("/login")
    public String token(Authentication authentication) {
        BackendApplication.t.updateSharedEventSubject(sharedEventSubject);
        LOG.debug("Token requested for user '{}'.", authentication.getName());
        String token = this.tokenService.generateToken(authentication);
        LOG.debug("Token granted '{}'.", token);
        return token;
    }
}
