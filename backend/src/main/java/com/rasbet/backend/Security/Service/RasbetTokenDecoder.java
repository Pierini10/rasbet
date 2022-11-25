package com.rasbet.backend.Security.Service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import java.time.Instant;


public class RasbetTokenDecoder
{
    private final String token;
    private final Instant creationTime;
    private final Instant expiracyDate;
    private final String user;
    private final String role;
    private final int id;

    public RasbetTokenDecoder(String token, JwtDecoder jwtDecoder)
    {
        this.token = token;

        Jwt jwt = jwtDecoder.decode(this.token);

        id = Integer.parseInt(jwt.getClaimAsString("iss"));
        role = jwt.getClaimAsString("role");
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

    public Instant getCreationTime() {
        return creationTime;
    }

    public Instant getExpiracyDate() {
        return expiracyDate;
    }

    public String getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public static String parseToken(String receivedToken)
    {
        return receivedToken.split("Bearer ")[1];
    }
}
