package com.kushkipagos.android;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.HttpURLConnection;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.kushkipagos.android.Helpers.buildResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class KushkiUnitTest {

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(8888);
    @Mock
    private AurusEncryption aurusEncryption;
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        kushki = new Kushki("10000001436354684173102102", "USD", TestEnvironment.LOCAL, aurusEncryption);
    }

    @Test
    public void shouldReturnTokenWhenCalledWithValidParams() throws Exception {
        double totalAmount = 10.0;
        Card card = new Card("John Doe", "4242424242424242", "123", "12", "21");
        String token = RandomStringUtils.randomAlphanumeric(32);
        String expectedRequestBody = buildExpectedRequestBody(card, totalAmount);
        wireMockRule.stubFor(post(urlEqualTo("/kushki/api/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildResponse("000", "Transacción aprobada", "1800000", token))));
        Transaction transaction = kushki.requestToken(card, totalAmount);
        assertThat(transaction.getToken(), is(token));
    }

    @Test
    public void shouldReturnErrorMessageWhenCalledWithInvalidParams() throws Exception {
        double totalAmount = 10.0;
        Card card = new Card("Invalid John Doe", "424242", "123", "12", "21");
        String errorCode = RandomStringUtils.randomNumeric(3);
        String errorMessage = RandomStringUtils.randomAlphabetic(15);
        String expectedRequestBody = buildExpectedRequestBody(card, totalAmount);
        wireMockRule.stubFor(post(urlEqualTo("/kushki/api/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_PAYMENT_REQUIRED)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildResponse(errorCode, errorMessage))));
        Transaction transaction = kushki.requestToken(card, totalAmount);
        assertThat(transaction.getToken(), is(""));
        assertThat(transaction.getCode(), is(errorCode));
        assertThat(transaction.getText(), is(errorMessage));
    }

    private String buildExpectedRequestBody(Card card, double totalAmount) throws Exception {
        String encryptedRequest = RandomStringUtils.randomAlphanumeric(50);
        String expectedRequestMessage = buildRequestMessage("10000001436354684173102102", card, totalAmount);
        when(aurusEncryption.encryptMessageChunk(expectedRequestMessage)).thenReturn(encryptedRequest);
        return "{\"request\": \"" + encryptedRequest + "\"}";
    }

    private String buildRequestMessage(String publicMerchantId, Card card, double totalAmount) throws JSONException {
        JSONObject requestTokenParams = new JSONObject();
        requestTokenParams.put("merchant_identifier", publicMerchantId);
        requestTokenParams.put("language_indicator", "es");
        requestTokenParams.put("card", card.toJsonObject());
        requestTokenParams.put("amount", String.format(Locale.ENGLISH, "%.2f", totalAmount));
        requestTokenParams.put("remember_me", "0");
        requestTokenParams.put("deferred_payment", "0");
        requestTokenParams.put("token_type", "transaction-token");
        return requestTokenParams.toString();
    }
}
