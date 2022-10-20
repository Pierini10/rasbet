package com.rasbet.backend.Entities;

public class User {

    // Class variables
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String NIF;
    private final String CC;
    private final String address;
    private final String phoneNumber;
    private final String birthday;

    // Basic all variables Constructer
    public User(String email, String password, String firstName, String lastName, String NIF, String CC, String address,
            String phoneNumber, String birthday) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.NIF = NIF;
        this.CC = CC;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    // Basic getters
    public String getEmail() {
        return this.email;
    }
    
    
    public String getPassword() {
        return this.password;
    }
    
    
    public String getFirstName() {
        return this.firstName;
    }
    
    
    public String getLastName() {
        return this.lastName;
    }
    
    
    public String getNIF() {
        return this.NIF;
    }
    
    
    public String getCC() {
        return this.CC;
    }
    
    
    public String getAddress() {
        return this.address;
    }
    
    
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    
    public String getBirthday() {
        return this.birthday;
    }

    
    
}
    