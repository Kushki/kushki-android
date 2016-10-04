package com.kushkipagos.android;

public class Card {
    private final String name;
    private final String number;
    private final String cvv;
    private final String expiryMonth;
    private final String expiryYear;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public Card(String name, String number, String cvv, String expiryMonth, String expiryYear) {
        this.name = name;
        this.number = number;
        this.cvv = cvv;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }
}
