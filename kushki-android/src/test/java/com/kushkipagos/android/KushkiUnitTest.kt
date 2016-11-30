package com.kushkipagos.android

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kushkipagos.android.Helpers.buildResponse
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.equalTo
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
    private val validCard = Card("John Doe", "4242424242424242", "123", "12", "21")
    private val invalidCard = Card("Invalid John Doe", "424242", "123", "12", "21")
    private val aurusEncryption = mock(AurusEncryption::class.java)
    private val kushki = Kushki("10000001436354684173102102", "USD", TestEnvironment.LOCAL, aurusEncryption)

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTokenWhenCalledWithValidParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount)
        val responseBody = buildResponse("000", "Transacción aprobada", "1800000", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestToken(validCard, totalAmount)
        assertThat(transaction.token, equalTo(token))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorMessageWhenCalledWithInvalidParams() {
        val errorCode = RandomStringUtils.randomNumeric(3)
        val errorMessage = RandomStringUtils.randomAlphabetic(15)
        val expectedRequestBody = buildExpectedRequestBody(invalidCard, totalAmount)
        val responseBody = buildResponse(errorCode, errorMessage)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED)
        val transaction = kushki.requestToken(invalidCard, totalAmount)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo(errorCode))
        assertThat(transaction.message, equalTo(errorMessage))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnSubscriptionTokenWhenCalledWithValidParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedSubscriptionRequestBody(validCard)
        val responseBody = buildResponse("000", "Transacción aprobada", "1800000", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestSubscriptionToken(validCard)
        assertThat(transaction.token, equalTo(token))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorMessageWhenCalledWithInvalidSubscriptionParams() {
        val errorCode = RandomStringUtils.randomNumeric(3)
        val errorMessage = RandomStringUtils.randomAlphabetic(15)
        val expectedRequestBody = buildExpectedSubscriptionRequestBody(invalidCard)
        val responseBody = buildResponse(errorCode, errorMessage)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_PAYMENT_REQUIRED)
        val transaction = kushki.requestSubscriptionToken(invalidCard)
        assertThat(transaction.token, equalTo(""))
        assertThat(transaction.code, equalTo(errorCode))
        assertThat(transaction.message, equalTo(errorMessage))
    }

    private fun stubTokenApi(expectedRequestBody: String, responseBody: String, status: Int) {
        wireMockRule.stubFor(post(urlEqualTo("/kushki/api/v1/tokens"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)))
    }

    private fun buildExpectedRequestBody(card: Card, totalAmount: Double): String {
        val encryptedRequest = RandomStringUtils.randomAlphanumeric(50)
        val expectedRequestMessage = buildRequestMessage("10000001436354684173102102", card, totalAmount)
        try {
            `when`(aurusEncryption.encryptMessageChunk(expectedRequestMessage)).thenReturn(encryptedRequest)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        return "{\"request\": \"$encryptedRequest\"}"
    }

    private fun buildExpectedSubscriptionRequestBody(card: Card): String {
        val encryptedRequest = RandomStringUtils.randomAlphanumeric(50)
        val expectedRequestMessage = buildSubscriptionRequestMessage("10000001436354684173102102", card)
        try {
            `when`(aurusEncryption.encryptMessageChunk(expectedRequestMessage)).thenReturn(encryptedRequest)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        return "{\"request\": \"$encryptedRequest\"}"
    }

    private fun buildSubscriptionRequestMessage(publicMerchantId: String, card: Card): String {
        try {
            val requestTokenParams = JSONObject()
            requestTokenParams.put("merchant_identifier", publicMerchantId)
            requestTokenParams.put("card", card.toJsonObject())
            requestTokenParams.put("remember_me", "0")
            requestTokenParams.put("deferred_payment", "0")
            requestTokenParams.put("language_indicator", "es")
            requestTokenParams.put("token_type", "subscription-token")
            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }

    }

    private fun buildRequestMessage(publicMerchantId: String, card: Card, totalAmount: Double): String {
        try {
            val requestTokenParams = JSONObject()
            requestTokenParams.put("merchant_identifier", publicMerchantId)
            requestTokenParams.put("language_indicator", "es")
            requestTokenParams.put("card", card.toJsonObject())
            requestTokenParams.put("amount", String.format(Locale.ENGLISH, "%.2f", totalAmount))
            requestTokenParams.put("remember_me", "0")
            requestTokenParams.put("deferred_payment", "0")
            requestTokenParams.put("token_type", "transaction-token")
            return requestTokenParams.toString()
        } catch (e: JSONException) {
            throw IllegalArgumentException(e)
        }

    }
}
