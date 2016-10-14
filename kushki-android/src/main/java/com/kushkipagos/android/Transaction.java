package com.kushkipagos.android;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

    private final String code;
    private final String message;
    private final String token;
    private final boolean successful;

    Transaction(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            code = jsonResponse.getString("response_code");
            message = jsonResponse.getString("response_text");
            token = jsonResponse.getString("transaction_token");
            successful = "000".equals(code);
        } catch (JSONException jsonException) {
            throw new IllegalArgumentException(jsonException);
        }
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
