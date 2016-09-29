package com.kushkipagos.android;

public enum KushkiEnvironment {
    LOCAL("https://localhost:9443/kushki/api/v1"),
    TESTING("https://uat.aurusinc.com/kushki/api/v1"),
    STAGING("https://staging.aurusinc.com/kushki/api/v1"),
    PRODUCTION("https://p1.kushkipagos.com/kushki/api/v1");

    private String url;

    KushkiEnvironment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
