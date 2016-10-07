package com.kushkipagos.android;

public enum KushkiEnvironment implements Environment {
    TESTING("https://uat.aurusinc.com/kushki/api/v1"),
    STAGING("https://staging.aurusinc.com/kushki/api/v1"),
    PRODUCTION("https://p1.kushkipagos.com/kushki/api/v1");

    private final String url;

    KushkiEnvironment(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
