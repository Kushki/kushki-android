package com.kushkipagos.android;

enum TestEnvironment implements Environment {
    LOCAL("http://localhost:8888/kushki/api/v1"),
    INVALID("This causes a MalformedURLException");

    private final String url;

    TestEnvironment(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
