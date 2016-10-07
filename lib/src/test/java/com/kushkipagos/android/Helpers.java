package com.kushkipagos.android;

import org.json.JSONException;
import org.json.JSONObject;

class Helpers {

    static String buildResponse(String code, String text) {
        return buildResponse(code, text, "", "");
    }

    static String buildResponse(String code, String text, String tokenValidity, String token) {
        try {
            return new JSONObject()
                    .put("response_code", code)
                    .put("response_text", text)
                    .put("transaction_token_validity", tokenValidity)
                    .put("transaction_token", token)
                    .toString();
        } catch (JSONException jsonException) {
            throw new IllegalArgumentException("Don't build invalid JSON!", jsonException);
        }
    }
}
