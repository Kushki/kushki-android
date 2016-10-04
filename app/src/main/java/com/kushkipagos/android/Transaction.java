package com.kushkipagos.android;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

    private JSONObject jsonResponse;

    public Transaction(String responseBody) throws JSONException {
        this.jsonResponse = new JSONObject(responseBody);
    }

    public String getCode() throws JSONException {
        return jsonResponse.getString("response_code");
    }

    public String getToken() throws JSONException {
        return jsonResponse.getString("transaction_token");
    }

    public String getText() throws JSONException {
        return jsonResponse.getString("response_text");
    }
}
