package com.kushkipagos.android;

import org.json.JSONException;
import org.json.JSONObject;

public class Card {

    private final String name;
    private final String number;
    private final String cvv;
    private final String expiryMonth;
    private final String expiryYear;

    public Card(String name, String number, String cvv, String expiryMonth, String expiryYear) {
        this.name = name;
        this.number = number;
        this.cvv = cvv;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    JSONObject toJson() throws JSONException {
        return new JSONObject()
                .put("name", name)
                .put("number", number)
                .put("expiry_month", expiryMonth)
                .put("expiry_year", expiryYear)
                .put("cvv", cvv)
                .put("card_present", "1");
    }
}
