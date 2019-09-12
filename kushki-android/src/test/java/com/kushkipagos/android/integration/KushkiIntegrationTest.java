package com.kushkipagos.android.integration;

import com.kushkipagos.android.BankList;
import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.Transaction;
import com.kushkipagos.android.TransferSubscriptions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {

    private static final int TOKEN_LENGTH = 32;
    private static final String INVALID_CARD_CODE = "K001";
    private static final String INVALID_CARD_ASYNC_CODE = "CAS001";
    private static final String INVALID_CARD_ASYNC_CODE_MERCHANT = "CAS004";
    private static final String INVALID_CARD_MESSAGE = "Cuerpo de la petición inválido.";

    private final Kushki kushki = new Kushki("10000001641080185390111217", "USD", KushkiEnvironment.TESTING);
    private final Kushki kushkiCardAsync = new Kushki("20000000103098876000", "CLP", KushkiEnvironment.QA);
    private final Kushki kushkiCardAsyncInvalid = new Kushki("20000000103098876000", "CPL", KushkiEnvironment.QA);
    private final Kushki kushkiCardAsyncInvalidMerchant = new Kushki("2000000010309", "CLP", KushkiEnvironment.QA);
    private final Kushki kushkiTransferSubscription = new Kushki("20000000102183993000", "COP", KushkiEnvironment.QA);
    private final Kushki kushkiBankList = new Kushki("20000000100323955000","COP",KushkiEnvironment.QA);
    private final Card validCard = new Card("Lisbeth Salander", "5321952125169352", "123", "12", "21");
    private final Card invalidCard = new Card("Lisbeth Salander", "4242424242", "123", "12", "21");
    private final TransferSubscriptions kushkiSubscriptionTransfer = new TransferSubscriptions("12312312","1","jose","gonzalez",
            "123123123","CC","01",12,"tes@kushkipagos.com","USD");
    private final Double totalAmount = 10.0;
    private final Double totalAmountCardAsync = 1000.00;
    private final String returnUrl = "https://return.url";

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

    @Test
    public void shouldReturnCardAsyncTokenWhenCalledWithValidParams() throws Exception {
        Transaction resultTransaction = kushkiCardAsync.cardAsyncTokens(
                totalAmountCardAsync,
                returnUrl,
                "Description of the payment send from android library.",
                "mati@kushkipagos.com");
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnCardAsyncTokenWhenCalledWithValidParamsButIncompleted() throws Exception {
        Transaction resultTransaction = kushkiCardAsync.cardAsyncTokens(totalAmountCardAsync,returnUrl);
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnCardAsyncTokenWhenCalledWithValidParamsButIncompletedOnlyemail() throws Exception {
        Transaction resultTransaction = kushkiCardAsync.cardAsyncTokens(totalAmountCardAsync,returnUrl,"mati@kushkipagos.com");
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnSubscriptionTransferTokenWhenCalledWithValidParams() throws Exception {
        Transaction resultTransaction = kushkiTransferSubscription.transferSubscriptionTokens(kushkiSubscriptionTransfer);
        assertValidTransactionSubscriptionTransfer(resultTransaction);
    }

    @Test
    public void shouldNotReturnCardAsyncTokenWhenCalledWithInValidParams() throws Exception {
        Transaction resultTransaction = kushkiCardAsyncInvalid.cardAsyncTokens(totalAmountCardAsync,returnUrl);
        assertInvalidCardAsyncTransaction(resultTransaction);
    }
    @Test
    public void shouldNotReturnCardAsyncTokenWhenCalledWithInValidMerchant() throws Exception {
        Transaction resultTransaction = kushkiCardAsyncInvalidMerchant.cardAsyncTokens(totalAmountCardAsync,returnUrl);
        assertInvalidCardAsyncMerchant(resultTransaction);
    }

    @Test
    public void shouldNReturnBankListWhenCalledValidResponse() throws Exception {
        BankList resultBankList = kushkiBankList.bankListSubscriptionTransfer();
        assertValidBankList(resultBankList);
    }


    private void assertValidTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(true));
        assertThat(resultTransaction.getToken().length(), is(TOKEN_LENGTH));
    }

    private void assertValidTransactionSubscriptionTransfer(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(true));
        assertThat(resultTransaction.getToken().length(), is(TOKEN_LENGTH));
    }

    private void assertInvalidTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_CODE));
        assertThat(resultTransaction.getMessage(), is("Cuerpo de la petición inválido."));
    }

    private void assertInvalidTransactionCard(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_CODE));
        assertThat(resultTransaction.getMessage(), is(INVALID_CARD_MESSAGE));
    }

    private void assertInvalidCardAsyncTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_ASYNC_CODE));
        assertThat(resultTransaction.getMessage(), is("Cuerpo de la petición inválido."));
    }
    private void assertInvalidCardAsyncMerchant(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_ASYNC_CODE_MERCHANT));
        assertThat(resultTransaction.getMessage(), is("ID de comercio o credencial no válido"));
    }
    private void assertValidBankList(BankList resultBankList) {
        assertThat(resultBankList.getBanks().length(), notNullValue());

    }


}
