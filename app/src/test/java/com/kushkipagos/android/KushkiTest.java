package com.kushkipagos.android;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.HttpURLConnection;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class KushkiTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8888);

    @Mock
    private AurusEncryption aurusEncryption;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnTokenWhenCalledWithValidParams() throws Exception {
        double totalAmount = 10.0;
        Card card = new Card("Name", "4242424242424242", "123", "12", "21");
        Kushki kushki = new Kushki("1234567890", "USD", KushkiEnvironment.LOCAL, aurusEncryption);
        String expectedToken = RandomStringUtils.randomAlphanumeric(32);
        String responseBody = "{\"response_code\": \"000\", \"response_text\": \"Transacción aprobada\"," +
                "\"transaction_token_validity\": \"1800000\", \"transaction_token\": \"" + expectedToken + "\"}";
        String encryptedRequest = RandomStringUtils.randomAlphanumeric(50);
        String expectedRequestMessage = "";
        when(aurusEncryption.encryptMessageChunk(expectedRequestMessage)).thenReturn(encryptedRequest);
        String expectedRequestBody = "{\"request\": \"" + encryptedRequest + "\"}";
        wireMockRule.stubFor(post(urlEqualTo("/kushki/api/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
        Transaction transaction = kushki.requestToken(card, totalAmount);
        assertThat(transaction.getToken(), is(expectedToken));
    }
}
