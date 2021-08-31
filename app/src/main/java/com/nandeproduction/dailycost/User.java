package com.nandeproduction.dailycost;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String emailOrPhonenumber;
    private String street;
    private String country;
    private String currency;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailOrPhonenumber() {
        return emailOrPhonenumber;
    }

    public void setEmailOrPhonenumber(String emailOrPhonenumber) {
        this.emailOrPhonenumber = emailOrPhonenumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
