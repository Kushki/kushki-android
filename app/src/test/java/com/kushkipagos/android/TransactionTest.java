package com.kushkipagos.android;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransactionTest {

    @Test
    public void shouldReturnTheTokenFromTheResponseBody() throws JSONException {
        String expectedToken = RandomStringUtils.randomAlphanumeric(32);
        String responseBody = "{" +
                "\"response_code\": \"000\"," +
                "\"response_text\": \"Transacción aprobada\"," +
                "\"transaction_token_validity\": \"1800000\"," +
                "\"transaction_token\": \"" + expectedToken + "\"" +
                "}";
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getToken(), is(expectedToken));
    }

    @Test
    public void shouldReturnTheCodeFromTheResponseBody() throws JSONException {
        String expectedCode = RandomStringUtils.randomNumeric(3);
        String responseBody = "{" +
                "\"response_code\": \"" + expectedCode + "\"," +
                "\"response_text\": \"Transacción aprobada\"," +
                "\"transaction_token_validity\": \"1800000\"," +
                "\"transaction_token\": \"s0m3v4L1Dt0k3n\"" +
                "}";
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getCode(), is(expectedCode));
    }

    @Test
    public void shouldReturnTheTextFromTheResponseBody() throws JSONException {
        String expectedText = RandomStringUtils.randomAlphanumeric(20);
        String responseBody = "{" +
                "\"response_code\": \"000\"," +
                "\"response_text\": \"" + expectedText + "\"," +
                "\"transaction_token_validity\": \"1800000\"," +
                "\"transaction_token\": \"s0m3v4L1Dt0k3n\"" +
                "}";
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getText(), is(expectedText));
    }

    @Test
    public void shouldReturnTrueWhenTransactionIsSuccessful() throws JSONException {
        String responseBody = "{" +
                "\"response_code\": \"000\"," +
                "\"response_text\": \"Transacción aprobada\"," +
                "\"transaction_token_validity\": \"1800000\"," +
                "\"transaction_token\": \"s0m3v4L1Dt0k3n\"" +
                "}";
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.isSuccessful(), equalTo(true));
    }

    @Test
    public void shouldReturnFalseWhenTransactionIsNotSuccessful() throws JSONException {
        String responseBody = "{" +
                "\"response_code\": \"211\"," +
                "\"response_text\": \"Transación no permitida\"," +
                "\"transaction_token_validity\": \"\"," +
                "\"transaction_token\": \"\"" +
                "}";
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.isSuccessful(), equalTo(false));
    }
}