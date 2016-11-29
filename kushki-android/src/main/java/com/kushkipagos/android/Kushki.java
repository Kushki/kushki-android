package com.kushkipagos.android;

public class Kushki {

    private static final String TOKENS_PATH = "/tokens";
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
        return client.post(TOKENS_PATH, client.buildParameters(publicMerchantId, card, totalAmount));
    }

    public Transaction requestSubscriptionToken(Card card) throws KushkiException {
        return client.post(TOKENS_PATH, client.buildParameters(publicMerchantId, card));
    }
}