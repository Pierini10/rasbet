package com.rasbet.backend.Entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.rasbet.backend.Exceptions.BadPasswordException;

import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.QwertySequenceRule;
import edu.vt.middleware.password.RepeatCharacterRegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.WhitespaceRule;

public class User {

    // Class variables
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int NIF;
    private int CC;
    private String address;
    private String phoneNumber;
    private LocalDate birthday;
    private double balance;
    private String role;

    // Basic all variables Constructer
    public User(String email, String password, String firstName, String lastName, int NIF, int CC, String address,
            String phoneNumber, LocalDate birthday, String role) throws BadPasswordException {
        setEmail(email);
        setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        setNIF(NIF);
        setCC(CC);
        this.address = address;
        setPhoneNumber(phoneNumber);
        setBirthday(birthday);
        this.role = role;
    }

    // Basic email, password Constructer
    public User(String email, String password) {
        setEmail(email);
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

    public int getNIF() {
        return this.NIF;
    }

    public int getCC() {
        return this.CC;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setEmail(String email) {
        if (Pattern.compile("^(.+)@(\\S+)$").matcher(email).matches())
            this.email = email;
        else
            throw new IllegalArgumentException("Email is not valid");
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

    public static boolean isValidNIF(String number) {
        final int max = 9;
        // check if is numeric and has 9 numbers
        if (!number.matches("[0-9]+") || number.length() != max)
            return false;
        int checkSum = 0;
        // calculate checkSum
        for (int i = 0; i < max - 1; i++) {
            checkSum += (number.charAt(i) - '0') * (max - i);
        }
        int checkDigit = 11 - (checkSum % 11);
        // if checkDigit is higher than 9 set it to zero
        if (checkDigit > 9)
            checkDigit = 0;
        // compare checkDigit with the last number of NIF
        return checkDigit == number.charAt(max - 1) - '0';
    }

    public void setNIF(int NIF) {
        if (isValidNIF(Integer.toString(NIF)))
            this.NIF = NIF;
        else
            throw new IllegalArgumentException("NIF is not valid");
    }

    public static boolean isValidCC(String number) {
        final int max = 8;
        // check if is numeric and has 9 numbers
        if (!number.matches("[0-9]+") || number.length() != max)
            return false;
        return true;
    }

    public void setCC(int CC) {
        if (isValidCC(Integer.toString(CC)))
            this.CC = CC;
        else
            throw new IllegalArgumentException("CC is not valid");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (Pattern.compile("^(\\+\\d{1,3})?\\d{1,14}$").matcher(phoneNumber).matches())
            this.phoneNumber = phoneNumber;
        else
            throw new IllegalArgumentException("Phone number is not valid");
    }

    public void setBirthday(LocalDate birthday) {
        if (birthday.isBefore(LocalDate.now().minus(18, ChronoUnit.YEARS)))
            this.birthday = birthday;
        else
            throw new IllegalArgumentException("You must be at least 18 years old");
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void update_info(String email, String password, String firstName, String lastName, String address,
            String phoneNumber) throws BadPasswordException {
        if (email != null && !email.equals(""))
            setEmail(email);
        if (password != null && !password.equals(""))
            setPassword(password);
        if (firstName != null && !firstName.equals(""))
            this.firstName = firstName;
        if (lastName != null && !lastName.equals(""))
            this.lastName = lastName;
        if (address != null && !address.equals(""))
            this.address = address;
        if (phoneNumber != null && !phoneNumber.equals(""))
            setPhoneNumber(phoneNumber);
    }
}
