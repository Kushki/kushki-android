package com.kushkipagos.android;

import android.support.annotation.VisibleForTesting;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Kushki {

    private final String publicMerchantId;
    private final AurusClient client;

    // TODO: Check what to do about throwing all those exceptions
    public Kushki(String publicMerchantId, String currency, KushkiEnvironment environment) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this(publicMerchantId, currency, environment, new AurusEncryption());
    }

    @VisibleForTesting
    Kushki(String publicMerchantId, String currency, KushkiEnvironment environment, AurusEncryption aurusEncryption) {
        this.publicMerchantId = publicMerchantId;
        this.client = new AurusClient(environment, aurusEncryption);
    }

    public Transaction requestToken(Card card, double totalAmount) throws IOException, JSONException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String requestMessage = client.buildParameters(publicMerchantId, card, totalAmount);
        return client.post("/tokens", requestMessage);
    }
}
