package com.rasbet.backend;

import com.rasbet.backend.Security.Config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rasbet.backend.GamesAPI.Updater;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class BackendApplication {

	public static final Updater t = new Updater();


	public static void main(String[] args) {
		new Thread(t).start();
		// Run backend application.
		SpringApplication.run(BackendApplication.class, args);
	}
}
