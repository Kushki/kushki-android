package com.kushkipagos.android;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KushkiJsonBuilderTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private KushkiJsonBuilder kushkiJsonBuilder;

    @Before
    public void setUp() throws Exception {
        kushkiJsonBuilder = new KushkiJsonBuilder();
    }

    @Test
    public void shouldConvertJSONExceptionToAnUncheckedException() throws JSONException {
        Card evilCard = mock(Card.class);
        JSONException jsonException = new JSONException("");
        when(evilCard.toJsonObject()).thenThrow(jsonException);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectCause(is(jsonException));
        kushkiJsonBuilder.buildJson("", evilCard, (double) 1);
    }

    @Test
    public void shouldConvertJSONExceptionToAnUncheckedExceptionForSubscriptions() throws JSONException {
        Card evilCard = mock(Card.class);
        JSONException jsonException = new JSONException("");
        when(evilCard.toJsonObject()).thenThrow(jsonException);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectCause(is(jsonException));
        kushkiJsonBuilder.buildJson("", evilCard);
    }
}