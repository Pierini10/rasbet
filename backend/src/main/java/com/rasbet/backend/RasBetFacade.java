package com.rasbet.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RasBetFacade {
    

    /**
     * Test method to Remove TODO
     */
    @GetMapping("/backendAlive")
    public String checkNet() {
        return "Backend is Working!!!";
    }
}
