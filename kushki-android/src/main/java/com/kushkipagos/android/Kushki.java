package com.kushkipagos.android;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Kushki {

    private final String publicMerchantId;
    private final AurusClient client;

    public Kushki(String publicMerchantId, String currency, Environment environment) {
        this(publicMerchantId, currency, environment, new AurusEncryption());
    }

    Kushki(String publicMerchantId, String currency, Environment environment, AurusEncryption aurusEncryption) {
        this.publicMerchantId = publicMerchantId;
        this.client = new AurusClient(environment, aurusEncryption);
    }

    public Transaction requestToken(Card card, double totalAmount) throws KushkiException {
        return request(client.buildParameters(publicMerchantId, card, totalAmount));
    }

    public Transaction requestSubscriptionToken(Card card) throws KushkiException {
        return request(client.buildParameters(publicMerchantId, card));
    }

    private Transaction request(String requestMessage) throws KushkiException {
        try {
            return client.post("/tokens", requestMessage);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException |
                InvalidKeyException | InvalidKeySpecException | BadPaddingException e) {
            throw new KushkiException(e);
        }
    }
}