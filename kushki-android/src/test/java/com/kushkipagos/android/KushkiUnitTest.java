package com.kushkipagos.android;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.kushkipagos.android.Helpers.buildResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KushkiUnitTest {

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(8888);
    private final double totalAmount = 10.0;
    private final Card validCard = new Card("John Doe", "4242424242424242", "123", "12", "21");
    private final Card invalidCard = new Card("Invalid John Doe", "424242", "123", "12", "21");
    private final AurusEncryption aurusEncryption = mock(AurusEncryption.class);
    private Kushki kushki = new Kushki("10000001436354684173102102", "USD", TestEnvironment.LOCAL, aurusEncryption);

    @Test
    public void shouldReturnTokenWhenCalledWithValidParams() throws KushkiException {
        String token = RandomStringUtils.randomAlphanumeric(32);
        String expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount);
        String responseBody = buildResponse("000", "Transacción aprobada", "1800000", token);
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK);
        Transaction transaction = kushki.requestToken(validCard, totalAmount);
        assertThat(transaction.getToken(), is(token));
    }

    @Test
    public void shouldReturnErrorMessageWhenCalledWithInvalidParams() throws KushkiException {
        String errorCode = RandomStringUtils.randomNumeric(3);
        String errorMessage = RandomStringUtils.randomAlphabetic(15);
        String expectedRequestBody = buildExpectedRequestBody(invalidCard, totalAmount);
        String responseBody = buildResponse(errorCode, errorMessage);
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED);
        Transaction transaction = kushki.requestToken(invalidCard, totalAmount);
        assertThat(transaction.getToken(), is(""));
        assertThat(transaction.getCode(), is(errorCode));
        assertThat(transaction.getMessage(), is(errorMessage));
    }

    @Test
    public void shouldReturnSubscriptionTokenWhenCalledWithValidParams() throws KushkiException {
        String token = RandomStringUtils.randomAlphanumeric(32);
        String expectedRequestBody = buildExpectedSubscriptionRequestBody(validCard);
        String responseBody = buildResponse("000", "Transacción aprobada", "1800000", token);
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK);
        Transaction transaction = kushki.requestSubscriptionToken(validCard);
        assertThat(transaction.getToken(), is(token));
    }

    @Test
    public void shouldReturnErrorMessageWhenCalledWithInvalidSubscriptionParams() throws KushkiException {
        String errorCode = RandomStringUtils.randomNumeric(3);
        String errorMessage = RandomStringUtils.randomAlphabetic(15);
        String expectedRequestBody = buildExpectedSubscriptionRequestBody(invalidCard);
        String responseBody = buildResponse(errorCode, errorMessage);
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED);
        Transaction transaction = kushki.requestSubscriptionToken(invalidCard);
        assertThat(transaction.getToken(), is(""));
        assertThat(transaction.getCode(), is(errorCode));
        assertThat(transaction.getMessage(), is(errorMessage));
    }

    private void stubTokenApi(String expectedRequestBody, String responseBody, int status) {
        wireMockRule.stubFor(post(urlEqualTo("/kushki/api/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }

    private String buildExpectedRequestBody(Card card, double totalAmount) {
        String encryptedRequest = RandomStringUtils.randomAlphanumeric(50);
        String expectedRequestMessage = buildRequestMessage("10000001436354684173102102", card, totalAmount);
        try {
            when(aurusEncryption.encryptMessageChunk(expectedRequestMessage)).thenReturn(encryptedRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "{\"request\": \"" + encryptedRequest + "\"}";
    }

    private String buildExpectedSubscriptionRequestBody(Card card) {
        String encryptedRequest = RandomStringUtils.randomAlphanumeric(50);
        String expectedRequestMessage = buildSubscriptionRequestMessage("10000001436354684173102102", card);
        try {
            when(aurusEncryption.encryptMessageChunk(expectedRequestMessage)).thenReturn(encryptedRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "{\"request\": \"" + encryptedRequest + "\"}";
    }

    private String buildSubscriptionRequestMessage(String publicMerchantId, Card card) {
        try {
            JSONObject requestTokenParams = new JSONObject();
            requestTokenParams.put("merchant_identifier", publicMerchantId);
            requestTokenParams.put("card", card.toJsonObject());
            requestTokenParams.put("remember_me", "0");
            requestTokenParams.put("deferred_payment", "0");
            requestTokenParams.put("language_indicator", "es");
            requestTokenParams.put("token_type", "subscription-token");
            return requestTokenParams.toString();
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String buildRequestMessage(String publicMerchantId, Card card, double totalAmount) {
        try {
            JSONObject requestTokenParams = new JSONObject();
            requestTokenParams.put("merchant_identifier", publicMerchantId);
            requestTokenParams.put("language_indicator", "es");
            requestTokenParams.put("card", card.toJsonObject());
            requestTokenParams.put("amount", String.format(Locale.ENGLISH, "%.2f", totalAmount));
            requestTokenParams.put("remember_me", "0");
            requestTokenParams.put("deferred_payment", "0");
            requestTokenParams.put("token_type", "transaction-token");
            return requestTokenParams.toString();
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
