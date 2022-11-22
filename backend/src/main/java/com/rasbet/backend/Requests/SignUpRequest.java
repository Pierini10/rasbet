package com.rasbet.backend.Requests;

import java.time.LocalDate;

public class SignUpRequest
{
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final int NIF;
    private final int CC;
    private final String address;
    private final String phoneNumber;
    private final LocalDate birthday;
    private final String role;

    public SignUpRequest()
    {
        email = null;
        password = null;
        firstName = null;
        lastName = null;
        NIF = -1;
        CC = -1;
        address = null;
        phoneNumber = null;
        birthday = null;
        role = null;
    }

    public SignUpRequest(
            String email,
            String password,
            String firstName,
            String lastName,
            int NIF,
            int CC,
            String address,
            String phoneNumber,
            LocalDate birthday,
            String role)
    {

        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.NIF = NIF;
        this.CC = CC;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNIF() {
        return NIF;
    }

    public int getCC() {
        return CC;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getRole() {
        return role;
    }
}
