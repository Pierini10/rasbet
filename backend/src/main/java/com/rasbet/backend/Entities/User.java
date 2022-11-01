package com.rasbet.backend.Entities;

import java.util.ArrayList;
import java.util.List;

import com.rasbet.backend.Exceptions.BadPasswordException;

import edu.vt.middleware.password.*;

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
            String phoneNumber, String birthday, String role) throws BadPasswordException {
        this.email = email;
        setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.NIF = NIF;
        this.CC = CC;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.role = role;
    }

    // Basic email, password Constructer
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
    
    public void setPassword(String password) throws BadPasswordException {
        // password must be between 8 and 16 chars long
        LengthRule lengthRule = new LengthRule(8, 16);
        // don't allow whitespace
        WhitespaceRule whitespaceRule = new WhitespaceRule();
        // control allowed characters
        CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
        // require at least 1 digit in passwords
        charRule.getRules().add(new DigitCharacterRule(1));
        // require at least 1 non-alphanumeric char
        charRule.getRules().add(new NonAlphanumericCharacterRule(1));
        // require at least 1 upper case char
        charRule.getRules().add(new UppercaseCharacterRule(1));
        // require at least 1 lower case char
        charRule.getRules().add(new LowercaseCharacterRule(1));
        // require at least 3 of the previous rules be met
        charRule.setNumberOfCharacteristics(3);
        // don't allow qwerty sequences
        QwertySequenceRule qwertySeqRule = new QwertySequenceRule();
        // don't allow 4 repeat characters
        RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);

        // group all rules together in a List
        List<Rule> ruleList = new ArrayList<Rule>();
        ruleList.add(lengthRule);
        ruleList.add(whitespaceRule);
        ruleList.add(charRule);
        ruleList.add(qwertySeqRule);
        ruleList.add(repeatRule);
        PasswordValidator validator = new PasswordValidator(ruleList);
        PasswordData passwordData = new PasswordData(new Password(password));

        RuleResult result = validator.validate(passwordData);

        if (result.isValid()) {
            this.password = password;
        } else {
            throw new BadPasswordException(validator.getMessages(result).get(0));
        }
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

    public void update_info(String email, String password, String firstName, String lastName, String address,String phoneNumber) throws BadPasswordException {
        if (email != null && !email.equals("")) this.email = email;
        if (password != null && !password.equals("")) setPassword(password); 
        if (firstName != null && !firstName.equals("")) this.firstName = firstName; 
        if (lastName != null && !lastName.equals("")) this.lastName = lastName; 
        if (address != null && !address.equals("")) this.address = address; 
        if (phoneNumber != null && !phoneNumber.equals("")) this.phoneNumber = phoneNumber;
    }
}
