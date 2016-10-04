package com.kushkipagos.android;

import android.support.annotation.VisibleForTesting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Kushki {

    private final String publicMerchantId;
    private final String currency;
    private final KushkiEnvironment environment;
    private final AurusEncryption aurusEncryption;

    // TODO: Check what to do about throwing all those exceptions
    public Kushki(String publicMerchantId, String currency, KushkiEnvironment environment) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this(publicMerchantId, currency, environment, new AurusEncryption());
    }

    @VisibleForTesting
    Kushki(String publicMerchantId, String currency, KushkiEnvironment environment, AurusEncryption aurusEncryption) {
        this.publicMerchantId = publicMerchantId;
        this.currency = currency;
        this.environment = environment;
        this.aurusEncryption = aurusEncryption;
    }

    public Transaction requestToken(Card card, double totalAmount) throws IOException, JSONException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String encryptedRequest = aurusEncryption.encryptMessageChunk(buildParameters(card, totalAmount));
        HttpURLConnection connection = AurusClient.prepareConnection(environment.getUrl() + "/tokens", encryptedRequest);
        InputStream responseInputStream = AurusClient.getResponseInputStream(connection);
        return new Transaction(AurusClient.parseResponseBody(responseInputStream));
    }

    private String buildParameters(Card card, double totalAmount) throws JSONException {
        JSONObject requestTokenParams = new JSONObject();
        JSONObject cardParams = new JSONObject();
        cardParams.put("name", card.getName());
        cardParams.put("number", card.getNumber());
        cardParams.put("expiry_month", card.getExpiryMonth());
        cardParams.put("expiry_year", card.getExpiryYear());
        cardParams.put("cvv", card.getCvv());
        cardParams.put("card_present", "1");
        requestTokenParams.put("merchant_identifier", publicMerchantId);
        requestTokenParams.put("language_indicator", "es");
        requestTokenParams.put("card", cardParams);
        requestTokenParams.put("amount", String.format(Locale.ENGLISH, "%.2f", totalAmount));
        requestTokenParams.put("remember_me", "0");
        requestTokenParams.put("deferred_payment", "0");
        requestTokenParams.put("token_type", "transaction-token");
        return requestTokenParams.toString();
    }
}
