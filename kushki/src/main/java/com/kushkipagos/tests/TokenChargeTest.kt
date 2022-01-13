package com.kushkipagos.tests

import android.content.Context
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.models.Card
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.json.JSONException
import org.json.JSONObject
import org.junit.Test
import java.net.HttpURLConnection

class TokenChargeTest {
    private var appContext: Context? = null
    private val validCard = Card("John Doe", "5321952125169352", "123", "12", "21")
    private val totalAmount = 10.0
    val wireMockRule = WireMockRule(8888)
    private val kushki = Kushki("20000000106952643000", "USD", TestEnvironment.LOCAL_QA)

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTokenChargeWhenCalled() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount)
        val responseBody = Helpers.buildResponse("000", "", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestTokenCharge(validCard, totalAmount,"1584564378558000",true,appContext)
        MatcherAssert.assertThat(transaction.token.length, CoreMatchers.equalTo(32))
    }

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnErrorWhenCalledWithInvalidParams() {
        val token = RandomStringUtils.randomAlphanumeric(32)
        val expectedRequestBody = buildExpectedRequestBody(validCard, totalAmount)
        val responseBody = Helpers.buildResponse("000", "", token)
        stubTokenApi(expectedRequestBody, responseBody, HttpURLConnection.HTTP_OK)
        val transaction = kushki.requestTokenCharge(validCard, totalAmount,"15845643785580" ,true,appContext)
        MatcherAssert.assertThat(transaction.message, CoreMatchers.equalTo("ID de suscripción no válida."))
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