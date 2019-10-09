package com.kushkipagos.android

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kushkipagos.android.Helpers.buildBankListResponse
import com.kushkipagos.android.Helpers.buildBinInfoResponse
import com.kushkipagos.android.Helpers.buildResponse
import com.kushkipagos.android.Helpers.buildSecureValidationResponse
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection

class KushkiUnitTest {

    @Rule
    @JvmField
    val wireMockRule = WireMockRule(8888)
    private val totalAmount = 10.0
    private val validCard = Card("John Doe", "5321952125169352", "123", "12", "21")
    private val invalidCard = Card("Invalid John Doe", "4242424242", "123", "12", "21")
    private val kushki = Kushki("10000002036955013614148494909956", "USD", TestEnvironment.LOCAL)
    private val kushkiTransferSubscription = Kushki("20000000107415376000", "COP", TestEnvironment.LOCAL)
    private val kushkiSingleIP = Kushki("10000002036955013614148494909956", "USD", TestEnvironment.LOCAL,true)
    private val kushkiCardAsync = Kushki("10000002667885476032150186346335", "CLP", TestEnvironment.LOCAL)
    private val kushkiCardAsyncErrorMerchant = Kushki("20000000", "CLP", TestEnvironment.LOCAL)
    private val kushkiCardAsyncErrorCurrency = Kushki("10000002667885476032150186346335", "CCC", TestEnvironment.LOCAL)
    private val kushkiBankList = Kushki("20000000107415376000","COP",TestEnvironment.LOCAL)
    private val kushkiBinInfo = Kushki("10000002036955013614148494909956","USD",TestEnvironment.LOCAL)
    private val totalAmountCardAsync = 1000.00
    private val kushkiSubscriptionTransfer = TransferSubscriptions("892352","1","TOBAR","",
            "123123123","CC","01",12,"tes@kushkipagos.com","USD")
    private val returnUrl = "https://return.url"
    private val description = "Description test"
    private val email = "email@test.com"
    private val amount = Amount(10.0,0.0,1.2)
    private val callbackUrl = "www.kushkipagos.com"
    private val userType = "0"
    private val documentType = "NIT"
    private val documentNumber = "12312312313"
    private val currency = "CLP"
    private val paymentDescription = "Test JD"
    private val cityCode = "01"
    private val stateCode = "02"
    private val phone = "00987654321"
    private val expeditionDate = "2017-05-09"
    private val answers = JSONArray("""
        [
            {
                "id": "5",
                "answer": "20121352"
            },
            {
                "id": "8",
                "answer": "20121356"
            },
            {
                "id": "12",
                "answer": "20121359"
            },
            {
                "id": "19",
                "answer": "20121363"
            }
        ]
    """)
    private val answersInvalid = JSONArray("""
        [
            {
                "id": "id",
                "answer": "1"
            },
            {
                "id": "id",
                "answer": "1"
            },
            {
                "id": "id",
                "answer": "1"
            }
        ]
    """)


    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTokenWhenCalledWithValidParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount)
        val responseBody = buildResponse("000", "", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestToken(validCard, totalAmount)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.token.length, equalTo(32))
    }



    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorMessageWhenCalledWithInvalidParams() {
        val errorCode = RandomStringUtils.randomNumeric(3)
        val errorMessage = "Cuerpo de la petición inválido."
        val expectedRequestBody = buildExpectedRequestBody(invalidCard, totalAmount)
        val responseBody = buildResponse(errorCode, errorMessage)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED)
        val transaction = kushki.requestToken(invalidCard, totalAmount)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo("K001"))
        assertThat(transaction.message, equalTo(errorMessage))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnOKMessageWhenCalledWithSingleIP() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount)
        val responseBody = buildResponse("000", "", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiSingleIP.requestToken(validCard, totalAmount)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.message, equalTo(""))
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnSubscriptionTokenWhenCalledWithValidParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedSubscriptionRequestBody(validCard)
        val responseBody = buildResponse("000", "", token)
        stubSubscriptionTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestSubscriptionToken(validCard)
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorMessageWhenCalledWithInvalidSubscriptionParams() {
        val errorCode = RandomStringUtils.randomNumeric(3)
        val errorMessage = "Cuerpo de la petición inválido."
        val expectedRequestBody = buildExpectedSubscriptionRequestBody(invalidCard)
        val responseBody = buildResponse(errorCode, errorMessage)
        stubSubscriptionTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED)
        val transaction = kushki.requestSubscriptionToken(invalidCard)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo("K001"))
        assertThat(transaction.message, equalTo(errorMessage))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnCardAsyncTokenWhenCalledWithValidParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestCardAsyncBody(totalAmountCardAsync,returnUrl, description, email)
        val responseBody = buildResponse("000", "", token)
        stubCardAsyncTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiCardAsync.requestCardAsyncToken(totalAmountCardAsync,returnUrl, description,email)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnCardAsyncTokenWhenCalledWithIncompleteParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestCardAsyncBodyIncomplete(totalAmountCardAsync,returnUrl)
        val responseBody = buildResponse("000", "", token)
        stubCardAsyncTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiCardAsync.requestCardAsyncToken(totalAmountCardAsync,returnUrl)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnCardAsyncTokenWhenCalledWithIncompleteParamsOnlyEmail() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestCardAsyncBodyOnlyEmail(totalAmountCardAsync,returnUrl,email)
        val responseBody = buildResponse("000", "", token)
        stubCardAsyncTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiCardAsync.requestCardAsyncToken(totalAmountCardAsync,returnUrl)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTransferSubscriptionTokenWhenCalledWithCompleteParamsOnlyEmail() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildRequestTransferSubscriptionMessage(kushkiSubscriptionTransfer)
        val responseBody = buildResponse("000", "", token)
        stubTransferSubscriptionTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorMessageWhenCalledWithInvalidMerchant() {
        val errorCode = RandomStringUtils.randomNumeric(3)
        val errorMessage = "ID de comercio o credencial no válido"
        val expectedRequestBody = buildExpectedRequestCardAsyncBody(totalAmountCardAsync, returnUrl, description, email)
        val responseBody = buildResponse(errorCode, errorMessage)
        stubCardAsyncTokenApiErrorMerchant(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED)
        val transaction = kushkiCardAsyncErrorMerchant.requestCardAsyncToken(totalAmountCardAsync, returnUrl, description, email)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo("CAS004"))
        assertThat(transaction.message, equalTo(errorMessage))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorMessageWhenCalledWithInvalidCurrency() {
        val errorCode = RandomStringUtils.randomNumeric(3)
        val errorMessage = "Cuerpo de la petición inválido."
        val expectedRequestBody = buildExpectedRequestCardAsyncBody(totalAmountCardAsync, returnUrl, description, email)
        val responseBody = buildResponse(errorCode, errorMessage)
        stubCardAsyncTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED)
        val transaction = kushkiCardAsyncErrorCurrency.requestCardAsyncToken(totalAmountCardAsync, returnUrl, description, email)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo("CAS001"))
        assertThat(transaction.message, equalTo(errorMessage))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTransferTokenWhenCalledWithValidParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestTransferBody()
        val responseBody = buildResponse("000", "", token)
        stubTransferTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestTransferToken(amount, callbackUrl, userType, documentType,
                documentNumber,email,currency)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTransferTokenWhenCalledWithValidCompletedParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestTransferBody(paymentDescription)
        val responseBody = buildResponse("000", "", token)
        stubTransferTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestTransferToken(amount, callbackUrl, userType, documentType,
                documentNumber,email,currency,paymentDescription)
        System.out.println(transaction.token)
        System.out.println(token)
        assertThat(transaction.token.length, equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTransferTokenWhenCalledWithInvalidParams() {
        val errorCode = RandomStringUtils.randomNumeric(3)
        val errorMessage = "Cuerpo de la petición inválido."
        val expectedRequestBody = buildExpectedRequestTransferBody(paymentDescription,"test")
        val responseBody = buildResponse(errorCode, errorMessage)
        stubTransferTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED)
        val transaction = kushki.requestTransferToken(amount, callbackUrl, "test", documentType,
                documentNumber,email,currency)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo("T001"))
        assertThat(transaction.message, equalTo(errorMessage))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnBankListWhenCalledWithValidResponse() {
        val responseBody = buildBankListResponse()
        stubBankListApi(responseBody, HttpURLConnection.HTTP_OK)
        val banklist = kushkiBankList.getBankList()
        System.out.println(banklist.banks)
        System.out.println(banklist.banks[3])
        assertThat(banklist.banks, notNullValue())
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnBinInfoWhenCalledWithValidResponse() {
        val responseBody = buildBinInfoResponse()
        stubBinInfoApi(responseBody, HttpURLConnection.HTTP_OK)
        val binInfo = kushkiBinInfo.getBinInfo("465775")
        System.out.println(binInfo.bank)
        System.out.println(binInfo.brand)
        System.out.println(binInfo.cardType)
        assertThat(binInfo.bank, notNullValue())
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnAskQuestionnaireWhenCalledWithCompleteParams() {
        val expectedRequestBody = buildRequestTransferSubscriptionMessage(kushkiSubscriptionTransfer)
        val responseBody = buildSecureValidationResponse("000","", "02")
        stubSecureValidationApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer)
        val secureInfo = AskQuestionnaire(transaction.secureId,transaction.secureService,cityCode,stateCode,phone,expeditionDate)
        val secureValidation = kushkiTransferSubscription.requestSecureValidation(secureInfo)
        assertThat(secureValidation.questions.length(), equalTo(4))
        System.out.println(secureValidation.questions.
                getJSONObject(0).
                getJSONArray("options").
                getJSONObject(0).
                get("text"))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnOTPExpiradoMessageWhenCalledWithInvalidSucureServiceId() {
        val errorCode = "OTP300"
        val errorMessage = "OTP expirado"
        val expectedRequestBody = buildRequestTransferSubscriptionMessage(kushkiSubscriptionTransfer)
        val responseBody = buildSecureValidationResponse(errorCode,errorMessage, "")
        stubSecureValidationApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val askQuestionnaire = AskQuestionnaire("InvalidId","confronta",cityCode,stateCode,phone,expeditionDate)
        val secureValidation = kushkiTransferSubscription.requestSecureValidation(askQuestionnaire)
        assertThat(secureValidation.questions.length(), equalTo(0))
        assertThat(secureValidation.code, equalTo("OTP300"))
        assertThat(secureValidation.message, equalTo("OTP expirado"))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorMessageWhenCalledWithInvalidConfrontaBiometrics() {
        val errorCode = "TR006"
        val errorMessage = "Cuerpo de petición inválido"
        val expectedRequestBody = buildRequestTransferSubscriptionMessage(kushkiSubscriptionTransfer)
        val responseBody = buildSecureValidationResponse(errorCode,errorMessage, "")
        stubSecureValidationApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer)
        val askQuestionnaire = AskQuestionnaire(transaction.secureId,transaction.secureService,"","","","")
        val secureValidation = kushkiTransferSubscription.requestSecureValidation(askQuestionnaire)
        assertThat(secureValidation.questions.length(), equalTo(0))
        assertThat(secureValidation.code, equalTo("TR006"))
        assertThat(secureValidation.message, equalTo("Cuerpo de petición inválido"))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnOkMessageWhenCalledWithValidAnswers() {
        val expectedRequestBody = buildRequestTransferSubscriptionMessage(kushkiSubscriptionTransfer)
        val responseBody = buildSecureValidationResponse("000","", "02")
        stubSecureValidationApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer)
        val askQuestionnaire = AskQuestionnaire(transaction.secureId,transaction.secureService,cityCode,stateCode,phone,expeditionDate)
        var secureValidation = kushkiTransferSubscription.requestSecureValidation(askQuestionnaire)
        val validateAnswers = ValidateAnswers(transaction.secureId,transaction.secureService,"14080263",answers)
        secureValidation = kushkiTransferSubscription.requestSecureValidation(validateAnswers)
        assertThat(secureValidation.message, equalTo("ok"))
        assertThat(secureValidation.code, equalTo("BIO000"))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnInvalidUserMessageWhenCalledWithInvalidAnswers() {
        val expectedRequestBody = buildRequestTransferSubscriptionMessage(kushkiSubscriptionTransfer)
        val responseBody = buildSecureValidationResponse("000","", "02")
        stubSecureValidationApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushkiTransferSubscription.requestTransferSubscriptionToken(kushkiSubscriptionTransfer)
        val askQuestionnaire = AskQuestionnaire(transaction.secureId,transaction.secureService,cityCode,stateCode,phone,expeditionDate)
        var secureValidation = kushkiTransferSubscription.requestSecureValidation(askQuestionnaire)
        val validateAnswers = ValidateAnswers(transaction.secureId,transaction.secureService,"3123",answersInvalid)
        secureValidation = kushkiTransferSubscription.requestSecureValidation(validateAnswers)
        assertThat(secureValidation.code, equalTo("BIO100"))
    }

    private fun stubTokenApi(expectedRequestBody: String, responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(post(urlEqualTo("v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "10000001656015280078454110039965")
                        .withBody(responseBody)))
    }

    private fun stubSubscriptionTokenApi(expectedRequestBody: String, responseBody: String, status: Int) {
        wireMockRule.stubFor(post(urlEqualTo("v1/subscription-tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "10000001656015280078454110039965")
                        .withBody(responseBody)))
    }

    private fun stubTransferTokenApi(expectedRequestBody: String, responseBody: String, status: Int) {
        wireMockRule.stubFor(post(urlEqualTo("transfer/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "200000001030988")
                        .withBody(responseBody)))
    }

    private fun stubCardAsyncTokenApi(expectedRequestBody: String, responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(post(urlEqualTo("card-async/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "20000000103098876000")
                        .withBody(responseBody)))
    }

    private fun stubTransferSubscriptionTokenApi(expectedRequestBody: String, responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(post(urlEqualTo("v1/transfer-subscription-tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "20000000103098876000")
                        .withBody(responseBody)))
    }

    private fun stubCardAsyncTokenApiErrorMerchant(expectedRequestBody: String, responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(post(urlEqualTo("card-async/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "200000001030988")
                        .withBody(responseBody)))
    }

    private fun stubBankListApi(responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(get(urlEqualTo("transfer-subscriptions/v1/bankList"))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "20000000100323955000")
                        .withBody(responseBody)))
    }

    private fun stubBinInfoApi(responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(get(urlEqualTo("card/v1/bin"))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "20000000100323955000")
                        .withBody(responseBody)))
    }

    private fun stubSecureValidationApi(expectedRequestBody: String, responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(post(urlEqualTo("rules/v1/secureValidation"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "20000000102183993000")
                        .withBody(responseBody)))
    }

    private fun buildExpectedRequestBody(card: Card, totalAmount: Double): String {
            val expectedRequestMessage = buildRequestMessage(card, totalAmount)
        return expectedRequestMessage
    }

    private fun buildExpectedSubscriptionRequestBody(card: Card): String {
        val expectedRequestMessage = buildSubscriptionRequestMessage("10000001436354684173102102", card)

        return expectedRequestMessage
    }

    private fun buildSubscriptionRequestMessage(publicMerchantId: String, card: Card): String {
        try {
            val requestTokenParams = JSONObject()
            requestTokenParams.put("merchant_identifier", publicMerchantId)
            requestTokenParams.put("card", card.toJsonObject())
            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }

    }

    private fun buildRequestMessage(card: Card, totalAmount: Double): String {
        try {
            val requestTokenParams = JSONObject()

            requestTokenParams.put("card", card.toJsonObject())
            requestTokenParams.put("totalAmount", totalAmount)
            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }
    }

    private fun buildExpectedRequestTransferBody():String{
        val expectedRequestMessage = buildRequestTransferMessage(amount, callbackUrl, userType, documentType,
                documentNumber,email,currency
        )
        return expectedRequestMessage
    }

    private fun buildExpectedRequestTransferBody(paymentDescription:String,documentTypeAux: String="CC"):String{
        val expectedRequestMessage = buildRequestTransferMessage(amount, callbackUrl, userType, documentTypeAux,
                documentNumber,email,currency,paymentDescription
        )
        return expectedRequestMessage
    }
    private fun buildRequestTransferMessage(amount: Amount, callbackUrl: String, userType: String, documentType:String,documentNumber:String,
                                            email:String,currency:String): String {
        try {
            val requestTokenParams = JSONObject()
            requestTokenParams.put("amount", amount.toJsonObject())
            requestTokenParams.put("callbackUrl", callbackUrl)
            requestTokenParams.put("userType", userType)
            requestTokenParams.put("documentType", documentType)
            requestTokenParams.put("documentNumber", documentNumber)
            requestTokenParams.put("email", email)
            requestTokenParams.put("currency", currency)
            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }

    }
    private fun buildRequestTransferMessage(amount: Amount, callbackUrl: String, userType: String, documentType:String,documentNumber:String,
                                            email:String,currency:String,paymentDescription:String): String {
        try {
            val requestTokenParams = JSONObject()
            requestTokenParams.put("amount", amount.toJsonObject())
            requestTokenParams.put("callbackUrl", callbackUrl)
            requestTokenParams.put("userType", userType)
            requestTokenParams.put("documentType", documentType)
            requestTokenParams.put("documentNumber", documentNumber)
            requestTokenParams.put("email", email)
            requestTokenParams.put("currency", currency)
            requestTokenParams.put("paymentDescription", paymentDescription)
            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }

    }

    private fun buildExpectedRequestCardAsyncBody(totalAmount: Double, returnUrl: String, description: String, email: String ): String {
        val expectedRequestMessage = buildRequestCardAsyncMessage(totalAmount, returnUrl, description, email)
        return expectedRequestMessage
    }

    private fun buildRequestCardAsyncMessage(totalAmount: Double, returnUrl: String, description: String, email: String): String {
        try {
            val requestTokenParams = JSONObject()

            requestTokenParams.put("totalAmount", totalAmount)
            requestTokenParams.put("returnUrl", returnUrl)
            requestTokenParams.put("description", description)
            requestTokenParams.put("email", email)

            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }
    }

    private fun buildRequestTransferSubscriptionMessage(transferSubscriptions: TransferSubscriptions): String {
        try {
            val requestTokenParams = transferSubscriptions.toJsonObject()

            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }
    }


    private fun buildExpectedRequestCardAsyncBodyIncomplete(totalAmount: Double, returnUrl: String ): String {
        val expectedRequestMessage = buildRequestCardAsyncMessageWithIncompleteParameters(totalAmount, returnUrl)
        return expectedRequestMessage
    }

    private fun buildRequestCardAsyncMessageWithIncompleteParameters(totalAmount: Double, returnUrl: String): String {
        try {
            val requestTokenParams = JSONObject()

            requestTokenParams.put("totalAmount", totalAmount)
            requestTokenParams.put("returnUrl", returnUrl)

            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }
    }

    private fun buildExpectedRequestCardAsyncBodyOnlyEmail(totalAmount: Double, returnUrl: String, email: String ): String {
        val expectedRequestMessage = buildRequestCardAsyncMessageWithIncompleteOnlyEmail(totalAmount, returnUrl, email)
        return expectedRequestMessage
    }

    private fun buildRequestCardAsyncMessageWithIncompleteOnlyEmail(totalAmount: Double, returnUrl: String, email: String): String {
        try {
            val requestTokenParams = JSONObject()

            requestTokenParams.put("totalAmount", totalAmount)
            requestTokenParams.put("returnUrl", returnUrl)
            requestTokenParams.put("email", email)

            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }
    }
}
