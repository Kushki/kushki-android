package com.kushkipagos.android.integration;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.Transaction;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {

    private static final int TOKEN_LENGTH = 32;
    private static final String SUCCESSFUL_CODE = "000";
    private static final String SUCCESSFUL_MESSAGE = "Transacción aprobada";
    private static final String INVALID_CARD_CODE = "017";
    private static final String INVALID_CARD_MESSAGE = "Tarjeta no válida";

    @Test
    public void shouldReturnTokenWhenCalledWithValidParams() throws Exception{
        Kushki kushki = new Kushki("10000001656015280078454110039965", "USD", KushkiEnvironment.TESTING);
        Card card = new Card("Lisbeth Salander", "4017779991118888", "123", "12", "21");
        Double totalAmount = 10.0;
        Transaction resultTransaction = kushki.requestToken(card, totalAmount);
        String resultCode = resultTransaction.getCode();
        String resultText = resultTransaction.getText();
        String resultCodeToken = resultTransaction.getToken();

        assertThat(resultCode, is(SUCCESSFUL_CODE));
        assertThat(resultText, is(SUCCESSFUL_MESSAGE));
        assertThat(resultCodeToken.length(), is(TOKEN_LENGTH));
    }

    @Test
    public void shouldNotReturnTokenWhenCalledWithInvalidParams() throws Exception{
        Kushki kushki = new Kushki("10000001656015280078454110039965", "USD", KushkiEnvironment.TESTING);
        Card card = new Card("Lisbeth Salander", "00000", "123", "12", "21");
        Double totalAmount = 10.0;
        Transaction resultTransaction = kushki.requestToken(card, totalAmount);
        String resultCode = resultTransaction.getCode();
        String resultText = resultTransaction.getText();
        String resultCodeToken = resultTransaction.getToken();

        assertThat(resultCode, is(INVALID_CARD_CODE));
        assertThat(resultText, is(INVALID_CARD_MESSAGE));
        assertThat(resultCodeToken, is(""));
    }
}
