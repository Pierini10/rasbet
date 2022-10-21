package com.rasbet.backend.Entities;

public class User {

    // Class variables
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String NIF;
    private String CC;
    private String address;
    private String phoneNumber;
    private String birthday;
    private double balance;
    private String role;

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

    // Basic all variables Constructer
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Basic getters
    public int getId() {
        return this.id;
    }

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

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getRole() {
        return this.role;
    }
    
    // Basic setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setNIF(String NIF) {
        this.NIF = NIF;
    }
    
    public void setCC(String CC) {
        this.CC = CC;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    
    public void setRole(String role) {
        this.role = role;
    }
}
