package com.kushkipagos.android;

import org.json.JSONException;
import org.json.JSONObject;

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

    JSONObject toJson() throws JSONException {
        return new JSONObject()
                .put("name", getName())
                .put("number", getNumber())
                .put("expiry_month", getExpiryMonth())
                .put("expiry_year", getExpiryYear())
                .put("cvv", getCvv())
                .put("card_present", "1");
    }
}
