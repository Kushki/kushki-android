package com.kushkipagos.android;

public class Kushki {
    private final String publicMerchantId;
    private final String currency;
    private final KushkiEnvironment environment;

    public Kushki(String publicMerchantId, String currency, KushkiEnvironment environment) {

        this.publicMerchantId = publicMerchantId;
        this.currency = currency;
        this.environment = environment;
    }

    public Transaction requestToken(Card card, double totalAmount) {
        return new Transaction();
    }
}
