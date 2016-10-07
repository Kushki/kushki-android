package com.kushkipagos.android;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collection;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.runners.Parameterized.Parameters;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class KushkiExceptionTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Mock
    private AurusEncryption aurusEncryption;
    private final Throwable expectedCause;

    public KushkiExceptionTest(Throwable expectedCause) {
        this.expectedCause = expectedCause;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Parameters
    public static Collection<Throwable[]> getTestParameters() {
        return Arrays.asList(new Throwable[][]{
                {new InvalidKeyException()},
                {new BadPaddingException()},
                {new IllegalBlockSizeException()},
                {new NoSuchAlgorithmException()},
                {new NoSuchPaddingException()},
                {new InvalidKeySpecException()}
        });
    }

    @Test
    public void shouldWrapOtherExceptionsWithKushkiException() throws Exception {
        double totalAmount = 10.0;
        Card card = new Card("Invalid John Doe", "424242", "123", "12", "21");
        Kushki kushki = new Kushki("10000001436354684173102102", "USD", TestEnvironment.INVALID, aurusEncryption);
        when(aurusEncryption.encryptMessageChunk(anyString())).thenThrow(expectedCause);
        expectedException.expect(KushkiException.class);
        expectedException.expectCause(is(expectedCause));
        kushki.requestToken(card, totalAmount);
    }
}
