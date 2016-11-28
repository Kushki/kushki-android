package com.kushkipagos.android;

import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AurusClientTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private AurusClient aurusClient;

    @Before
    public void setUp() throws Exception {
        aurusClient = new AurusClient(TestEnvironment.INVALID, new AurusEncryption());
    }

    @Test
    public void shouldConvertJSONExceptionToAnUncheckedException() throws JSONException {
        Card evilCard = mock(Card.class);
        JSONException jsonException = new JSONException("");
        when(evilCard.toJsonObject()).thenThrow(jsonException);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectCause(is(jsonException));
        aurusClient.buildParameters("", evilCard, 1);
    }

    @Test
    public void shouldConvertJSONExceptionToAnUncheckedExceptionForSubscriptions() throws JSONException {
        Card evilCard = mock(Card.class);
        JSONException jsonException = new JSONException("");
        when(evilCard.toJsonObject()).thenThrow(jsonException);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectCause(is(jsonException));
        aurusClient.buildParameters("", evilCard);
    }

    @Test
    public void shouldWrapIOExceptionWithKushkiException() throws Exception {
        expectedException.expect(KushkiException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(IOException.class));
        aurusClient.post("/this-endpoint-does-not-exist", "");
    }
}