package com.kushkipagos.android

import android.content.Context
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.json.JSONException
import org.json.JSONObject
import org.junit.Test
import java.net.HttpURLConnection

class SiftTest {
    private var context: Context?
    private val validCard = Card("John Doe", "5321952125169352", "123", "12", "21")
    private val totalAmount = 10.0
    val wireMockRule = WireMockRule(8888)
    private val kushki = Kushki("e41151f380a145059b6c8f4d45002130", "USD", TestEnvironment.LOCAL)

    init {
        this.context=null
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTokenWhenCalledWithoutContext() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount)
        val responseBody = Helpers.buildResponse("000", "", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestToken(validCard, totalAmount)
        System.out.println(transaction.token)
        System.out.println(token)
        MatcherAssert.assertThat(transaction.token.length, CoreMatchers.equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTokenWhenCalledWithContext() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount)
        val responseBody = Helpers.buildResponse("000", "", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestToken(validCard, totalAmount,context,true)
        System.out.println(transaction.token)
        System.out.println(token)
        MatcherAssert.assertThat(transaction.token.length, CoreMatchers.equalTo(32))
    }

    private fun buildExpectedRequestBody(card: Card, totalAmount: Double): String {
        val expectedRequestMessage = buildRequestMessage(card, totalAmount)
        return expectedRequestMessage
    }
    private fun stubTokenApi(expectedRequestBody: String, responseBody: String, status: Int) {
        System.out.println("response---body")
        System.out.println(responseBody)
        wireMockRule.stubFor(WireMock.post(WireMock.urlEqualTo("v1/tokens"))
                .withRequestBody(WireMock.equalToJson(expectedRequestBody))
                .willReturn(WireMock.aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Public-Merchant-Id", "10000001656015280078454110039965")
                        .withBody(responseBody)))
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
}