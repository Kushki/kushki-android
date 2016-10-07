package com.kushkipagos.android;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransactionTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReturnTheTokenFromTheResponseBody() {
        String expectedToken = RandomStringUtils.randomAlphanumeric(32);
        String responseBody = Helpers.buildResponse("000", "Transacción aprobada", "1800000", expectedToken);
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getToken(), is(expectedToken));
    }

    @Test
    public void shouldReturnTheCodeFromTheResponseBody() {
        String expectedCode = RandomStringUtils.randomNumeric(3);
        String responseBody = Helpers.buildResponse(expectedCode, "Some error message");
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getCode(), is(expectedCode));
    }

    @Test
    public void shouldReturnTheTextFromTheResponseBody() {
        String expectedText = RandomStringUtils.randomAlphabetic(15);
        String responseBody = Helpers.buildResponse("123", expectedText);
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.getText(), is(expectedText));
    }

    @Test
    public void shouldReturnTrueWhenTransactionIsSuccessful() {
        String responseBody = Helpers.buildResponse("000", RandomStringUtils.randomAlphabetic(15));
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.isSuccessful(), is(true));
    }

    @Test
    public void shouldReturnFalseWhenTransactionIsNotSuccessful() {
        String responseBody = Helpers.buildResponse("211", RandomStringUtils.randomAlphabetic(15));
        Transaction transaction = new Transaction(responseBody);
        assertThat(transaction.isSuccessful(), is(false));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfBuiltWithInvalidJson() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(JSONException.class));
        new Transaction("This is invalid JSON");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfBuiltWithValidJSONWithMissingFields() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(JSONException.class));
        new Transaction("{}");
    }
}