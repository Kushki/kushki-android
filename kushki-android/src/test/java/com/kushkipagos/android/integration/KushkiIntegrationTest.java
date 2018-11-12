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
    private static final String SUCCESSFUL_MESSAGE = "";
    private static final String INVALID_CARD_CODE = "K001";
    private static final String INVALID_CARD_MESSAGE = "Cuerpo de la petición inválido.";

    private final Kushki kushki = new Kushki("10000001641080185390111217", "USD", KushkiEnvironment.TESTING);
    private final Card validCard = new Card("Lisbeth Salander", "5321952125169352", "123", "12", "21");
    private final Card invalidCard = new Card("Lisbeth Salander", "4242424242", "123", "12", "21");
    private final Double totalAmount = 10.0;

    @Test
    public void shouldReturnTokenWhenCalledWithValidParams() throws Exception {
        Transaction resultTransaction = kushki.requestToken(validCard, totalAmount);
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldNotReturnTokenWhenCalledWithInvalidParams() throws Exception {
        Transaction resultTransaction = kushki.requestToken(invalidCard, totalAmount);
        assertInvalidTransactionCard(resultTransaction);
    }

    @Test
    public void shouldReturnSubscriptionTokenWhenCalledWithValidParams() throws Exception {
        Transaction resultTransaction = kushki.requestSubscriptionToken(validCard);
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldNotReturnSubscriptionTokenWhenCalledWithInvalidParams() throws Exception {
        Transaction resultTransaction = kushki.requestSubscriptionToken(invalidCard);
        assertInvalidTransaction(resultTransaction);
    }

    private void assertValidTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(true));
        assertThat(resultTransaction.getToken().length(), is(TOKEN_LENGTH));
    }

    private void assertInvalidTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_CODE));
        assertThat(resultTransaction.getMessage(), is("El cuerpo de la petición es inválido"));
    }
    private void assertInvalidTransactionCard(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_CODE));
        assertThat(resultTransaction.getMessage(), is(INVALID_CARD_MESSAGE));
    }
}
