package com.kushkipagos.android;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

class KushkiJsonBuilder {

    String buildJson(String publicMerchantId, Card card) {
        return buildJsonObject(publicMerchantId, card).toString();
    }

    String buildJson(String publicMerchantId, Card card, double totalAmount) {
        return buildJsonObject(publicMerchantId, card, totalAmount).toString();
    }

    private JSONObject buildJsonObject(String publicMerchantId, Card card, double totalAmount) {
        try {
            return buildJsonObject(publicMerchantId, card)
                    .put("token_type", "transaction-token")
                    .put("amount", String.format(Locale.ENGLISH, "%.2f", totalAmount));
        } catch (JSONException jsonException) {
            throw new IllegalArgumentException(jsonException);
        }
    }

    private JSONObject buildJsonObject(String publicMerchantId, Card card) {
        try {
            return new JSONObject()
                    .put("remember_me", "0")
                    .put("deferred_payment", "0")
                    .put("language_indicator", "es")
                    .put("merchant_identifier", publicMerchantId)
                    .put("card", card.toJsonObject())
                    .put("token_type", "subscription-token");
        } catch (JSONException jsonException) {
            throw new IllegalArgumentException(jsonException);
        }
    }
}