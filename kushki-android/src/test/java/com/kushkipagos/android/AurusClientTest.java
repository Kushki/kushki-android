package com.kushkipagos.android;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class AurusClientTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldWrapIOExceptionWithKushkiException() throws Exception {
        AurusClient aurusClient = new AurusClient(TestEnvironment.INVALID, new AurusEncryption());
        expectedException.expect(KushkiException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(IOException.class));
        aurusClient.post("/this-endpoint-does-not-exist", "");
    }
}