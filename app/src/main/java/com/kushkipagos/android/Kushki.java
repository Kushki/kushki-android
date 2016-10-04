package com.kushkipagos.android;

import android.support.annotation.VisibleForTesting;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
        String encryptedRequest = aurusEncryption.encryptMessageChunk(AurusClient.buildParameters(publicMerchantId, card, totalAmount));
        HttpURLConnection connection = AurusClient.prepareConnection(environment.getUrl() + "/tokens", encryptedRequest);
        InputStream responseInputStream = AurusClient.getResponseInputStream(connection);
        return new Transaction(AurusClient.parseResponseBody(responseInputStream));
    }
}
