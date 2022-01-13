package com.kushkipagos.tests.integration

import android.content.Context
import com.kushkipagos.kushki.*
import com.kushkipagos.models.*
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class KushkiIntegrationTest {
    private val TOKEN_LENGTH = 32
    private val QUESTIONS_LENGTH = 4
    private val INVALID_CARD_CODE = "K001"
    private val INVALID_SUBSCRIPTION_ID = "K012"
    private val INVALID_CARD_ASYNC_CODE = "CAS001"
    private val INVALID_CARD_ASYNC_CODE_MERCHANT = "CAS004"
    private val INVALID_CARD_MESSAGE = "Cuerpo de la petición inválido."
    private val INVALID_SECURY_ID_MESSAGE = "CardRule Credential not found"
    private val INVALID_CASH_CODE = "C001"
    private val INVALID_CASH_CODE_MERCHANT = "C004"
    private val INVALID_SUBSCRIPTION_CARD_ASYNC_CODE = "K001"
    private val INVALID_SUBSCRIPTION_CODE_MERCHANT = "K004"


    private val kushki: Kushki =
        Kushki("10000001641080185390111217", "USD", KushkiEnvironment.TESTING)
    private val kushkiTokenCharge: Kushki =
        Kushki("20000000106952643000", "USD", KushkiEnvironment.QA)
    private val kushkiCash: Kushki =
        Kushki("6000000000154083361249085016881", "USD", KushkiEnvironment.TESTING)
    private val kushkiCashInvalid: Kushki =
        Kushki("6000000000154083361249085016881", "CCC", KushkiEnvironment.TESTING)
    private val kushkiCashInvalidMerchant: Kushki =
        Kushki("60000000001540", "USD", KushkiEnvironment.TESTING)
    private val kushkiCardAsync: Kushki =
        Kushki("10000002667885476032150186346335", "CLP", KushkiEnvironment.TESTING)
    private val kushkiCardAsyncInvalid: Kushki =
        Kushki("10000002667885476032150186346335", "CPL", KushkiEnvironment.TESTING)
    private val kushkiCardAsyncInvalidMerchant: Kushki =
        Kushki("2000000010309", "CLP", KushkiEnvironment.TESTING)
    private val kushkiTransferSubscription: Kushki =
        Kushki("20000000107415376000", "COP", KushkiEnvironment.TESTING)
    private val kushkiBankList: Kushki =
        Kushki("20000000107415376000", "COP", KushkiEnvironment.TESTING)
    private val kushkiBinInfo: Kushki =
        Kushki("10000002036955013614148494909956", "USD", KushkiEnvironment.QA)
    private val validCard: Card = Card("Lisbeth Salander", "5321952125169352", "123", "12", "21")
    private val invalidCard: Card = Card("Lisbeth Salander", "4242424242", "123", "12", "21")
    private val kushkiSubscriptionTransfer = TransferSubscriptions(
        "892352", "1", "jose", "gonzalez",
        "123123123", "CC", "01", 12, "tes@kushkipagos.com", "USD"
    )
    private val kushkiSubscriptionCardAsync: Kushki =
        Kushki("e955d8c491674b08869f0fe6f480c63e", "CLP", KushkiEnvironment.QA)
    private val kushkiSubscriptionCardAsyncInvalidMerchant: Kushki =
        Kushki("1234567890", "CLP", KushkiEnvironment.QA)

    private val totalAmount = 10.0
    private val totalAmountCardAsync = 1000.00
    private val returnUrl = "https://return.url"
    private val name = "Alex"
    private val lastName = "SG"
    private val documentType = "NIT"
    private val identification = "17219439565"
    private val currency = "USD"
    private val appContext: Context? = null

    @Test
    @Throws(Exception::class)
    fun shouldReturnTokenWhenCalledWithValidParams() {
        val resultTransaction: Transaction =
            kushki.requestToken(validCard, totalAmount, appContext, true)
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnTokenWhenCalledWithInvalidParams() {
        val resultTransaction: Transaction =
            kushki.requestToken(invalidCard, totalAmount, appContext, true)
        assertInvalidTransactionCard(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnTokenChargeWhenCalledWithValidParams() {
        val resultTransaction: Transaction = kushkiTokenCharge.requestTokenCharge(
            validCard,
            totalAmount,
            "1584564378558000",
            true,
            appContext
        )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnTokenChargeWhenCalledWithInvalidParams() {
        val resultTransaction: Transaction =
            kushki.requestTokenCharge(invalidCard, totalAmount, "15845643785580", true, appContext)
        assertInvalidSubscriptionId(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnSubscriptionTokenWhenCalledWithValidParams() {
        val resultTransaction: Transaction = kushki.requestSubscriptionToken(validCard)
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnSubscriptionTokenWhenCalledWithInvalidParams() {
        val resultTransaction: Transaction = kushki.requestSubscriptionToken(invalidCard)
        assertInvalidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnCardAsyncTokenWhenCalledWithValidParams() {
        val resultTransaction: Transaction = kushkiCardAsync.requestCardAsyncToken(
            totalAmountCardAsync,
            returnUrl,
            "Description of the payment send from android library.",
            "mati@kushkipagos.com"
        )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnCardAsyncTokenWhenCalledWithValidParamsButIncompleted() {
        val resultTransaction: Transaction =
            kushkiCardAsync.requestCardAsyncToken(totalAmountCardAsync, returnUrl)
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnCardAsyncTokenWhenCalledWithValidParamsButIncompletedOnlyemail() {
        val resultTransaction: Transaction = kushkiCardAsync.requestCardAsyncToken(
            totalAmountCardAsync,
            returnUrl,
            "mati@kushkipagos.com"
        )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnCashTokenWhenCalledWithValidParams() {
        val resultTransaction: Transaction = kushkiCash.requestCashToken(
            name,
            lastName,
            identification,
            documentType,
            "test@test.com",
            totalAmount,
            currency,
            "Description of the payment send from android library"
        )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnCashTokenWhenCalledWithValidParamsButIncompleted() {
        val resultTransaction: Transaction = kushkiCash.requestCashToken(
            name,
            lastName,
            identification,
            documentType,
            totalAmount,
            currency
        )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnCashTokenWhenCalledWithValidParamsButIncompletedOnlyEmail() {
        val resultTransaction: Transaction = kushkiCash.requestCashToken(
            name,
            lastName,
            identification,
            documentType,
            "test@test.com",
            totalAmount,
            currency
        )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnSubscriptionTransferTokenWhenCalledWithValidParams() {
        val resultTransaction: Transaction =
            kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer)
        assertValidTransactionSubscriptionTransfer(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnCashTokenWhenCalledWithInValidParams() {
        val resultTransaction: Transaction = kushkiCashInvalid.requestCashToken(
            name,
            lastName,
            identification,
            documentType,
            totalAmount,
            "CCC"
        )
        assertInvalidCashTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnCashTokenWhenCalledWithInValidMerchant() {
        val resultTransaction: Transaction = kushkiCashInvalidMerchant.requestCashToken(
            name,
            lastName,
            identification,
            documentType,
            totalAmount,
            currency
        )
        assertInvalidCashMerchant(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnCardAsyncTokenWhenCalledWithInValidParams() {
        val resultTransaction: Transaction =
            kushkiCardAsyncInvalid.requestCardAsyncToken(totalAmountCardAsync, returnUrl)
        assertInvalidCardAsyncTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnCardAsyncTokenWhenCalledWithInValidMerchant() {
        val resultTransaction: Transaction =
            kushkiCardAsyncInvalidMerchant.requestCardAsyncToken(totalAmountCardAsync, returnUrl)
        assertInvalidCardAsyncMerchant(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNReturnBankListWhenCalledValidResponse() {
        val resultBankList: BankList = kushkiBankList.getBankList()
        assertValidBankList(resultBankList)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNReturnBinInfoWhenCalledValidResponse() {
        val resultBinInfo: BinInfo = kushkiBinInfo.getBinInfo("465775")
        assertValidBinInfo(resultBinInfo)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnAskQuestionnarieWhenCalledWithValidParams() {
        val askQuestionnaire = AskQuestionnaire(
            "234", "234",
            "Quito", "01", "092840456", "12/12/2019", "20000000107415376000"
        )
        val resultAskQuestionnarie: SecureValidation =
            kushkiTransferSubscription.requestSecureValidation(askQuestionnaire)
        assertInvalidAskQuestionnarie(resultAskQuestionnarie)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnSubscriptionCardAsyncTokenWhenCalledWithValidParams() {
        val resultTransaction: Transaction =
            kushkiSubscriptionCardAsync.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "CLP",
                "https://mytest.com",
                "4242424242424242"
            )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnSubscriptionCardAsyncTokenWhenCalledWithParamsIncompleted() {
        val resultTransaction: Transaction =
            kushkiSubscriptionCardAsync.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "CLP",
                "https://mytest.com"
            )
        assertValidTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnSubscriptionCardAsyncTokenWhenCalledWithInValidParams() {
        val resultTransaction: Transaction =
            kushkiSubscriptionCardAsync.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "USD",
                "https://mytest.com"
            )
        assertInvalidSubscriptionCardAsyncTransaction(resultTransaction)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotReturnSubscriptionCardAsyncTokenWhenCalledWithInValidMerchant() {
        val resultTransaction: Transaction =
            kushkiSubscriptionCardAsyncInvalidMerchant.requestCardSubscriptionAsyncToken(
                "test@test.com",
                "CLP",
                "https://mytest.com",
                "4242424242424242"
            )
        assertInvalidSubscriptionCardAsyncMerchant(resultTransaction)
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

    /*
    @Test
    public void shouldReturnAskQuestionnarieWhenCalledWithInvalidParams() throws Exception {
        Transaction resultTransaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer);
        AskQuestionnaire askQuestionnaire = new AskQuestionnaire(resultTransaction.getSecureId(),resultTransaction.getSecureService()
                ,"02","01","092840456","12/12/2019");
        SecureValidation resultAskQuestionnarie = kushkiTransferSubscription.requestSecureValidation(askQuestionnaire);
        assertValidAskQuestionnarie(resultAskQuestionnarie);
    }*/
    private fun assertValidTransaction(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(resultTransaction.token.length, CoreMatchers.`is`(TOKEN_LENGTH))
    }

    private fun assertValidAskQuestionnarie(resultAskQuestionnarie: SecureValidation) {
        assertThat(resultAskQuestionnarie.isSuccessful, CoreMatchers.`is`(true))
        assertThat(
            resultAskQuestionnarie.questions.length(),
            CoreMatchers.`is`(QUESTIONS_LENGTH)
        )
    }

    private fun assertInvalidAskQuestionnarie(resultAskQuestionnarie: SecureValidation) {
        assertThat(resultAskQuestionnarie.isSuccessful, CoreMatchers.`is`(false))
        assertThat(resultAskQuestionnarie.code, CoreMatchers.`is`(INVALID_SECURY_ID_MESSAGE))
        assertThat(
            resultAskQuestionnarie.message,
            CoreMatchers.`is`("El ID de comercio no corresponde a la credencial enviada")
        )
    }

    private fun assertValidTransactionSubscriptionTransfer(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(resultTransaction.token.length, CoreMatchers.`is`(TOKEN_LENGTH))
    }

    private fun assertInvalidTransaction(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(resultTransaction.code, CoreMatchers.`is`(INVALID_CARD_CODE))
    }

    private fun assertInvalidTransactionCard(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(resultTransaction.code, CoreMatchers.`is`(INVALID_CARD_CODE))
    }

    private fun assertInvalidSubscriptionId(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(resultTransaction.code, CoreMatchers.`is`(INVALID_SUBSCRIPTION_ID))
    }

    private fun assertInvalidCashTransaction(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(resultTransaction.code, CoreMatchers.`is`(INVALID_CASH_CODE))
    }

    private fun assertInvalidCashMerchant(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(
            resultTransaction.code,
            CoreMatchers.`is`(INVALID_CASH_CODE_MERCHANT)
        )
    }

    private fun assertInvalidCardAsyncTransaction(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(resultTransaction.code, CoreMatchers.`is`(INVALID_CARD_ASYNC_CODE))
    }

    private fun assertInvalidCardAsyncMerchant(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(
            resultTransaction.code,
            CoreMatchers.`is`(INVALID_CARD_ASYNC_CODE_MERCHANT)
        )
    }

    private fun assertValidBankList(resultBankList: BankList) {
        assertThat(resultBankList.banks.length(), CoreMatchers.notNullValue())
    }

    private fun assertValidBinInfo(resultBinInfo: BinInfo) {
        assertThat(resultBinInfo.bank.length, CoreMatchers.notNullValue())
    }

    private fun assertInvalidSubscriptionCardAsyncTransaction(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(
            resultTransaction.code,
            CoreMatchers.`is`(INVALID_SUBSCRIPTION_CARD_ASYNC_CODE)
        )
    }

    private fun assertInvalidSubscriptionCardAsyncMerchant(resultTransaction: Transaction) {
        MatcherAssert.assertThat(resultTransaction.isSuccessful, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(
            resultTransaction.code,
            CoreMatchers.`is`(INVALID_SUBSCRIPTION_CODE_MERCHANT)
        )
    }
}