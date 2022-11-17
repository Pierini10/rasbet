package com.rasbet.backend.Security;

import com.rasbet.backend.Security.Service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthFacade
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthFacade.class);

    private final TokenService tokenService;

    public AuthFacade(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping
    public String home(Principal principal)
    {
        return "Hello, " + (principal != null ? principal.getName() : "user") + "!";
    }

    // DB Credentials:
    //  email: test@auth.com
    //  passw: Test3aut!
    //
    @PostMapping("/token")
    public String token(Authentication authentication)
    {
        LOG.debug("Token requested for user '{}'.", authentication.getName());
        String token = this.tokenService.generateToken(authentication);
        LOG.debug("Token granted '{}'.", token);
        return token;
    }
}
