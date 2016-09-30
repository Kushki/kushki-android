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
        String responseBody = "{" +
                "\"response_code\": \"000\"," +
                "\"response_text\": \"Transacción aprobada\"," +
                "\"transaction_token_validity\": \"1800000\"," +
                "\"transaction_token\": \"" + expectedToken + "\"" +
                "}";
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getToken(), is(expectedToken));
    }
}