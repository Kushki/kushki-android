package com.kushkipagos.android;


import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class KushkiTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().httpsPort(9443));

    @Test
    public void shouldReturnTokenWhenCalledWithValidParams() throws Exception {
        double totalAmount = 10.0;
        Card card = new Card("Name", "4242424242424242", "123", "12", "21");
        Kushki kushki = new Kushki("1234567890", "USD", KushkiEnvironment.LOCAL);
        Transaction transaction = kushki.requestToken(card, totalAmount);
        String expectedToken = RandomStringUtils.randomAlphanumeric(32);
        String responseBody = "{\"response_code\": \"000\", \"response_text\": \"Transacción aprobada\"," +
                "\"transaction_token_validity\": \"1800000\", \"transaction_token\": \"" + expectedToken + "\"}";
        wireMockRule.stubFor(post(urlEqualTo("/kushki/api/v1/tokens"))
                        .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
        assertThat(transaction.getToken(), is(expectedToken));
    }
}
