package com.rasbet.backend.Security.Service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import java.time.Instant;


public class RasbetTokenDecoder
{
    private final String token;
    private final String issuer;
    private final Instant creationTime;
    private final Instant expiracyDate;
    private final String user;
    private final String role;

    public RasbetTokenDecoder(String token, JwtDecoder jwtDecoder)
    {
        this.token = token;

        Jwt jwt = jwtDecoder.decode(this.token);

        role = jwt.getClaimAsString("role");
        issuer = jwt.getIssuer().toString();
        creationTime = jwt.getIssuedAt();
        expiracyDate = jwt.getExpiresAt();
        user = jwt.getSubject();
    }

    public String getRole() 
    {
        return this.role;
    }

    public String getToken() {
        return token;
    }

    public String getIssuer() {
        return issuer;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public Instant getExpiracyDate() {
        return expiracyDate;
    }

    public String getUser() {
        return user;
    }
}
