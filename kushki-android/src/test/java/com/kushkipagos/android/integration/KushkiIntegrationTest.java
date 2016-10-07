package com.kushkipagos.android.integration;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.Transaction;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {

    private static final int TOKEN_LENGTH = 32;
    private static final String SUCCESSFUL_CODE = "000";
    private static final String SUCCESSFUL_MESSAGE = "Transacción aprobada";
    private static final String INVALID_CARD_CODE = "017";
    private static final String INVALID_CARD_MESSAGE = "Tarjeta no válida";

    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        kushki = new Kushki("10000001656015280078454110039965", "USD", KushkiEnvironment.TESTING);
    }

    @Test
    public void shouldReturnTokenWhenCalledWithValidParams() throws Exception {
        Card card = new Card("Lisbeth Salander", "4017779991118888", "123", "12", "21");
        Double totalAmount = 10.0;
        Transaction resultTransaction = kushki.requestToken(card, totalAmount);
        assertThat(resultTransaction.isSuccessful(), is(true));
        assertThat(resultTransaction.getCode(), is(SUCCESSFUL_CODE));
        assertThat(resultTransaction.getText(), is(SUCCESSFUL_MESSAGE));
        assertThat(resultTransaction.getToken().length(), is(TOKEN_LENGTH));
    }

    @Test
    public void shouldNotReturnTokenWhenCalledWithInvalidParams() throws Exception {
        Card card = new Card("Lisbeth Salander", "00000", "123", "12", "21");
        Double totalAmount = 10.0;
        Transaction resultTransaction = kushki.requestToken(card, totalAmount);
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_CODE));
        assertThat(resultTransaction.getText(), is(INVALID_CARD_MESSAGE));
        assertThat(resultTransaction.getToken(), is(""));
    }
}
