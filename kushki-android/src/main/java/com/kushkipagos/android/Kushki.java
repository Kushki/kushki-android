package com.kushkipagos.android;

public class Kushki {

    private static final String TOKENS_PATH = "/tokens";
    private final String publicMerchantId;
    private final AurusClient client;
    private final KushkiJsonBuilder kushkiJsonBuilder;

    public Kushki(String publicMerchantId, String currency, Environment environment) {
        this(publicMerchantId, currency, environment, new AurusEncryption());
    }

    Kushki(String publicMerchantId, String currency, Environment environment, AurusEncryption aurusEncryption) {
        this.publicMerchantId = publicMerchantId;
        this.client = new AurusClient(environment, aurusEncryption);
        this.kushkiJsonBuilder = new KushkiJsonBuilder();
    }

    public Transaction requestToken(Card card, double totalAmount) throws KushkiException {
        return client.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(publicMerchantId, card, totalAmount));
    }

    public Transaction requestSubscriptionToken(Card card) throws KushkiException {
        return client.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(publicMerchantId, card));
    }
}