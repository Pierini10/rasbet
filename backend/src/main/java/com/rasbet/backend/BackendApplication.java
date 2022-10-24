package com.rasbet.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.GamesAPI.GamesApi;

import java.sql.*;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		// Run backend application.
		try {
            EventsDB.update_Events(GamesApi.getEvents());
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
	}
}
