package com.kushkipagos.android.integration;

import com.kushkipagos.android.AskQuestionnaire;
import com.kushkipagos.android.BankList;
import com.kushkipagos.android.BinInfo;
import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.SecureValidation;
import com.kushkipagos.android.Transaction;
import com.kushkipagos.android.TransferSubscriptions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {

    private static final int TOKEN_LENGTH = 32;
    private static final int QUESTIONS_LENGTH = 4;
    private static final String INVALID_CARD_CODE = "K001";
    private static final String INVALID_CARD_ASYNC_CODE = "CAS001";
    private static final String INVALID_CARD_ASYNC_CODE_MERCHANT = "CAS004";
    private static final String INVALID_CARD_MESSAGE = "Cuerpo de la petición inválido.";
    private static final String INVALID_SECURY_ID_MESSAGE = "OTP300";
    private static final String INVALID_CASH_CODE = "C001";
    private static final String INVALID_CASH_CODE_MERCHANT = "C004";
    private static final String INVALID_SUBSCRIPTION_CARD_ASYNC_CODE = "K001";
    private static final String INVALID_SUBSCRIPTION_CODE_MERCHANT = "K004";




    private final Kushki kushki = new Kushki("10000001641080185390111217", "USD", KushkiEnvironment.TESTING);
    private final Kushki kushkiCash= new Kushki("6000000000154083361249085016881", "USD", KushkiEnvironment.TESTING);
    private final Kushki kushkiCashInvalid= new Kushki("6000000000154083361249085016881", "CCC", KushkiEnvironment.TESTING);
    private final Kushki kushkiCashInvalidMerchant= new Kushki("60000000001540", "USD", KushkiEnvironment.TESTING);
    private final Kushki kushkiCardAsync = new Kushki("10000002667885476032150186346335", "CLP", KushkiEnvironment.TESTING);
    private final Kushki kushkiCardAsyncInvalid = new Kushki("10000002667885476032150186346335", "CPL", KushkiEnvironment.TESTING);
    private final Kushki kushkiCardAsyncInvalidMerchant = new Kushki("2000000010309", "CLP", KushkiEnvironment.TESTING);
    private final Kushki kushkiTransferSubscription = new Kushki("20000000102183993000", "COP", KushkiEnvironment.QA);
    private final Kushki kushkiBankList = new Kushki("20000000107415376000","COP",KushkiEnvironment.TESTING);
    private final Kushki kushkiBinInfo = new Kushki("10000002036955013614148494909956","USD",KushkiEnvironment.QA);
    private final Card validCard = new Card("Lisbeth Salander", "5321952125169352", "123", "12", "21");
    private final Card invalidCard = new Card("Lisbeth Salander", "4242424242", "123", "12", "21");
    private final TransferSubscriptions kushkiSubscriptionTransfer = new TransferSubscriptions("892352","1","jose","gonzalez",
            "123123123","CC","01",12,"tes@kushkipagos.com","USD");
    private final Kushki kushkiSubscriptionCardAsync= new Kushki("e955d8c491674b08869f0fe6f480c63e", "CLP", KushkiEnvironment.QA);
    private final Kushki kushkiSubscriptionCardAsyncInvalidMerchant= new Kushki("1234567890", "CLP", KushkiEnvironment.QA);

    private final Double totalAmount = 10.0;
    private final Double totalAmountCardAsync = 1000.00;
    private final String returnUrl = "https://return.url";
    private final String name = "Alex";
    private final String lastName = "SG";
    private final String documentType = "NIT";
    private final String identification = "17219439565";
    private final String currency = "USD";

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
        Transaction resultTransaction = kushkiCardAsync.requestCardAsyncToken(
                totalAmountCardAsync,
                returnUrl,
                "Description of the payment send from android library.",
                "mati@kushkipagos.com");
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnCardAsyncTokenWhenCalledWithValidParamsButIncompleted() throws Exception {
        Transaction resultTransaction = kushkiCardAsync.requestCardAsyncToken(totalAmountCardAsync,returnUrl);
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnCardAsyncTokenWhenCalledWithValidParamsButIncompletedOnlyemail() throws Exception {
        Transaction resultTransaction = kushkiCardAsync.requestCardAsyncToken(totalAmountCardAsync,returnUrl,"mati@kushkipagos.com");
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnCashTokenWhenCalledWithValidParams() throws Exception {
        Transaction resultTransaction = kushkiCash.requestCashToken(
                name,
                lastName,
                identification,
                documentType,
                "test@test.com",
                totalAmount,
                currency,
                "Description of the payment send from android library");
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnCashTokenWhenCalledWithValidParamsButIncompleted() throws Exception {
        Transaction resultTransaction = kushkiCash.requestCashToken(
                name,
                lastName,
                identification,
                documentType,
                totalAmount,
                currency);
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnCashTokenWhenCalledWithValidParamsButIncompletedOnlyEmail() throws Exception {
        Transaction resultTransaction = kushkiCash.requestCashToken(
                name,
                lastName,
                identification,
                documentType,
                "test@test.com",
                totalAmount,
                currency);
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnSubscriptionTransferTokenWhenCalledWithValidParams() throws Exception {
        Transaction resultTransaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer);
        assertValidTransactionSubscriptionTransfer(resultTransaction);
    }

    @Test
    public void shouldNotReturnCashTokenWhenCalledWithInValidParams() throws Exception {
        Transaction resultTransaction = kushkiCashInvalid.requestCashToken(
                name,
                lastName,
                identification,
                documentType,
                totalAmount,
                "CCC");
        assertInvalidCashTransaction(resultTransaction);
    }
    @Test
    public void shouldNotReturnCashTokenWhenCalledWithInValidMerchant() throws Exception {
        Transaction resultTransaction = kushkiCashInvalidMerchant.requestCashToken(
                name,
                lastName,
                identification,
                documentType,
                totalAmount,
                currency);
        assertInvalidCashMerchant(resultTransaction);
    }

    @Test
    public void shouldNotReturnCardAsyncTokenWhenCalledWithInValidParams() throws Exception {
        Transaction resultTransaction = kushkiCardAsyncInvalid.requestCardAsyncToken(totalAmountCardAsync,returnUrl);
        assertInvalidCardAsyncTransaction(resultTransaction);
    }
    @Test
    public void shouldNotReturnCardAsyncTokenWhenCalledWithInValidMerchant() throws Exception {
        Transaction resultTransaction = kushkiCardAsyncInvalidMerchant.requestCardAsyncToken(totalAmountCardAsync,returnUrl);
        assertInvalidCardAsyncMerchant(resultTransaction);
    }

    @Test
    public void shouldNReturnBankListWhenCalledValidResponse() throws Exception {
        BankList resultBankList = kushkiBankList.getBankList();
        assertValidBankList(resultBankList);
    }
    @Test
    public void shouldNReturnBinInfoWhenCalledValidResponse() throws Exception {
        BinInfo resultBinInfo = kushkiBinInfo.getBinInfo("465775");
        assertValidBinInfo(resultBinInfo);
    }

    @Test
    public void shouldNotReturnAskQuestionnarieWhenCalledWithValidParams() throws Exception {
        AskQuestionnaire askQuestionnaire = new AskQuestionnaire("234","234",
                "Quito","01","092840456","12/12/2019");
        SecureValidation resultAskQuestionnarie = kushkiTransferSubscription.requestSecureValidation(askQuestionnaire);
        assertInvalidAskQuestionnarie(resultAskQuestionnarie);
    }

    @Test
    public void shouldReturnSubscriptionCardAsyncTokenWhenCalledWithValidParams() throws Exception {
        Transaction resultTransaction = kushkiSubscriptionCardAsync.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "CLP",
                "https://mytest.com",
                "4242424242424242"
               );
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldReturnSubscriptionCardAsyncTokenWhenCalledWithParamsIncompleted() throws Exception {
        Transaction resultTransaction = kushkiSubscriptionCardAsync.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "CLP",
                "https://mytest.com"
                );
        assertValidTransaction(resultTransaction);
    }

    @Test
    public void shouldNotReturnSubscriptionCardAsyncTokenWhenCalledWithInValidParams() throws Exception {
        Transaction resultTransaction = kushkiSubscriptionCardAsync.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "USD",
                "https://mytest.com");
        assertInvalidSubscriptionCardAsyncTransaction(resultTransaction);
    }
    @Test
    public void shouldNotReturnSubscriptionCardAsyncTokenWhenCalledWithInValidMerchant() throws Exception {
        Transaction resultTransaction = kushkiSubscriptionCardAsyncInvalidMerchant.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "CLP",
                "https://mytest.com",
                "4242424242424242");
        assertInvalidSubscriptionCardAsyncMerchant(resultTransaction);
    }


/*
    @Test
    public void shouldReturnAskQuestionnarieWhenCalledWithInvalidParams() throws Exception {
        Transaction resultTransaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer);
        AskQuestionnaire askQuestionnaire = new AskQuestionnaire(resultTransaction.getSecureId(),resultTransaction.getSecureService()
                ,"02","01","092840456","12/12/2019");
        SecureValidation resultAskQuestionnarie = kushkiTransferSubscription.requestSecureValidation(askQuestionnaire);
        assertValidAskQuestionnarie(resultAskQuestionnarie);
    }*/

    private void assertValidTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(true));
        assertThat(resultTransaction.getToken().length(), is(TOKEN_LENGTH));
    }

    private void assertValidAskQuestionnarie(SecureValidation resultAskQuestionnarie) {
        assertThat(resultAskQuestionnarie.isSuccessful(), is(true));
        assertThat(resultAskQuestionnarie.getQuestions().length(), is(QUESTIONS_LENGTH));
    }

    private void assertInvalidAskQuestionnarie(SecureValidation resultAskQuestionnarie) {
        assertThat(resultAskQuestionnarie.isSuccessful(), is(false));
        assertThat(resultAskQuestionnarie.getCode(), is(INVALID_SECURY_ID_MESSAGE));
        assertThat(resultAskQuestionnarie.getMessage(), is("OTP expirado"));
    }

    private void assertValidTransactionSubscriptionTransfer(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(true));
        assertThat(resultTransaction.getToken().length(), is(TOKEN_LENGTH));
    }

    private void assertInvalidTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_CODE));
    }

    private void assertInvalidTransactionCard(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_CODE));
    }

    private void assertInvalidCashTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CASH_CODE));
    }
    private void assertInvalidCashMerchant(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CASH_CODE_MERCHANT));
    }
    private void assertInvalidCardAsyncTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_ASYNC_CODE));
    }
    private void assertInvalidCardAsyncMerchant(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_CARD_ASYNC_CODE_MERCHANT));
    }
    private void assertValidBankList(BankList resultBankList) {
        assertThat(resultBankList.getBanks().length(), notNullValue());
    }
    private void assertValidBinInfo(BinInfo resultBinInfo) {
        assertThat(resultBinInfo.getBank().length(), notNullValue());
    }
    private void assertInvalidSubscriptionCardAsyncTransaction(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_SUBSCRIPTION_CARD_ASYNC_CODE));
    }
    private void assertInvalidSubscriptionCardAsyncMerchant(Transaction resultTransaction) {
        assertThat(resultTransaction.isSuccessful(), is(false));
        assertThat(resultTransaction.getCode(), is(INVALID_SUBSCRIPTION_CODE_MERCHANT));
    }

}
