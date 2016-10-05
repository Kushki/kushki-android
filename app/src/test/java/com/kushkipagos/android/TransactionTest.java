package com.kushkipagos.android;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransactionTest {

    @Test
    public void shouldReturnTheTokenFromTheResponseBody() throws JSONException {
        String expectedToken = RandomStringUtils.randomAlphanumeric(32);
        String responseBody = Helpers.buildResponse("000", "Transacción aprobada", "1800000", expectedToken);
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getToken(), is(expectedToken));
    }

    @Test
    public void shouldReturnTheCodeFromTheResponseBody() throws JSONException {
        String expectedCode = RandomStringUtils.randomNumeric(3);
        String responseBody = Helpers.buildResponse(expectedCode, "Some error message");
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getCode(), is(expectedCode));
    }

    @Test
    public void shouldReturnTheTextFromTheResponseBody() throws JSONException {
        String expectedText = RandomStringUtils.randomAlphabetic(15);
        String responseBody = Helpers.buildResponse("123", expectedText);
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getText(), is(expectedText));
    }

    @Test
    public void shouldReturnTrueWhenTransactionIsSuccessful() throws JSONException {
        String responseBody = Helpers.buildResponse("000", RandomStringUtils.randomAlphabetic(15));
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.isSuccessful(), is(true));
    }

    @Test
    public void shouldReturnFalseWhenTransactionIsNotSuccessful() throws JSONException {
        String responseBody = Helpers.buildResponse("211", RandomStringUtils.randomAlphabetic(15));
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.isSuccessful(), is(false));
    }
}