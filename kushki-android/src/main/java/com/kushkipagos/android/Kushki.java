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
        String requestMessage = client.buildParameters(publicMerchantId, card, totalAmount);
        try {
            return client.post("/tokens", requestMessage);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException |
                NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
            throw new KushkiException(e);
        }
    }

    public Transaction requestSubscriptionToken(Card card) throws KushkiException {
        String requestMessage = client.buildSubscriptionParameters(publicMerchantId, card);
        try {
            return client.post("/tokens", requestMessage);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException |
                InvalidKeyException | InvalidKeySpecException | BadPaddingException e) {
            throw new KushkiException(e);
        }
    }
}