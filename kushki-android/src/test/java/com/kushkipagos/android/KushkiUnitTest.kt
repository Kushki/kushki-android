package com.kushkipagos.android

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kushkipagos.android.Helpers.buildResponse
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONException
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.net.HttpURLConnection
import java.util.*

class KushkiUnitTest {

    @Rule
    @JvmField
    val wireMockRule = WireMockRule(8888)
    private val totalAmount = 10.0
    private val validCard = Card("John Doe", "5321952125169352", "123", "12", "21")
    private val invalidCard = Card("Invalid John Doe", "4242424242", "123", "12", "21")
    private val kushki = Kushki("10000002036955013614148494909956", "USD", TestEnvironment.LOCAL)
    private val kushkiSingleIP = Kushki("10000002036955013614148494909956", "USD", TestEnvironment.LOCAL,true)
    private val kushkiCardAsync = Kushki("20000000103098876000", "CLP", TestEnvironment.LOCAL_QA)
    private val kushkiCardAsyncErrorMerchant = Kushki("20000000", "CLP", TestEnvironment.LOCAL_QA)
    private val kushkiCardAsyncErrorCurrency = Kushki("20000000103098876000", "CCC", TestEnvironment.LOCAL_QA)
    private val totalAmountCardAsync = 1000.00
    private val returnUrl = "https://return.url"
    private val description = "Description test"
    private val email = "email@test.com"

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
        val transaction = kushkiCardAsync.cardAsyncTokens(totalAmountCardAsync,returnUrl, description,email)
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
        val transaction = kushkiCardAsync.cardAsyncTokens(totalAmountCardAsync,returnUrl)
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
        val transaction = kushkiCardAsyncErrorMerchant.cardAsyncTokens(totalAmountCardAsync, returnUrl, description, email)
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
        val transaction = kushkiCardAsyncErrorCurrency.cardAsyncTokens(totalAmountCardAsync, returnUrl, description, email)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo("CAS001"))
        assertThat(transaction.message, equalTo(errorMessage))
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
}
