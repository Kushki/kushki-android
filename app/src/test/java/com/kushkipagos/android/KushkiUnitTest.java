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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class KushkiUnitTest {

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
        Card card = new Card("John Doe", "4242424242424242", "123", "12", "21");
        Kushki kushki = new Kushki("10000001436354684173102102", "USD", KushkiEnvironment.LOCAL, aurusEncryption);
        String expectedToken = RandomStringUtils.randomAlphanumeric(32);
        String responseBody = buildSuccessfulResponse(expectedToken);
        String encryptedRequest = RandomStringUtils.randomAlphanumeric(50);
        String expectedRequestMessage = buildRequestMessage("10000001436354684173102102", card, totalAmount);
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
        assertThat(transaction.getCode(), is("000"));
    }

    @Test
    public void shouldNotReturnTokenWhenCalledWithInvalidParams() throws Exception {
        double totalAmount = 10.0;
        Card card = new Card("Invalid John Doe", "424242", "123", "12", "21");
        Kushki kushki = new Kushki("10000001436354684173102102", "USD", KushkiEnvironment.LOCAL, aurusEncryption);
        String responseBody = buildFailureResponse();
        String encryptedRequest = RandomStringUtils.randomAlphanumeric(50);
        String expectedRequestMessage = buildRequestMessage("10000001436354684173102102", card, totalAmount);
        when(aurusEncryption.encryptMessageChunk(expectedRequestMessage)).thenReturn(encryptedRequest);
        String expectedRequestBody = "{\"request\": \"" + encryptedRequest + "\"}";
        wireMockRule.stubFor(post(urlEqualTo("/kushki/api/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_BAD_REQUEST)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
        Transaction transaction = kushki.requestToken(card, totalAmount);
        assertThat(transaction.getToken(), is(""));
        assertThat(transaction.getCode(), is("017"));
    }

    private String buildFailureResponse() throws JSONException {
        return new JSONObject()
                .put("response_code", "017")
                .put("response_text", "Tarjeta no válida")
                .put("transaction_token_validity", "")
                .put("transaction_token", "")
                .toString();
    }

    // TODO: Add sad paths unit tests

    private String buildSuccessfulResponse(String expectedToken) throws JSONException {
        return new JSONObject()
                .put("response_code", "000")
                .put("response_text", "Transacción aprobada")
                .put("transaction_token_validity", "1800000")
                .put("transaction_token", expectedToken)
                .toString();
    }

    private String buildRequestMessage(String publicMerchantId, Card card, double totalAmount) throws JSONException {
        JSONObject requestTokenParams = new JSONObject();
        JSONObject cardParams = new JSONObject();
        cardParams.put("name", card.getName());
        cardParams.put("number", card.getNumber());
        cardParams.put("expiry_month", card.getExpiryMonth());
        cardParams.put("expiry_year", card.getExpiryYear());
        cardParams.put("cvv", card.getCvv());
        cardParams.put("card_present", "1");
        requestTokenParams.put("merchant_identifier", publicMerchantId);
        requestTokenParams.put("language_indicator", "es");
        requestTokenParams.put("card", cardParams);
        requestTokenParams.put("amount", String.format(Locale.ENGLISH, "%.2f", totalAmount));
        requestTokenParams.put("remember_me", "0");
        requestTokenParams.put("deferred_payment", "0");
        requestTokenParams.put("token_type", "transaction-token");
        return requestTokenParams.toString();
    }
}
