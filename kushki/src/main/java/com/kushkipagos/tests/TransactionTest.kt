package com.kushkipagos.tests

import com.kushkipagos.models.Transaction
import com.kushkipagos.tests.Helpers.buildResponse
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONException
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class TransactionTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun shouldReturnTheTokenFromTheResponseBody() {
        val expectedToken = RandomStringUtils.randomAlphanumeric(32)
        val responseBody = buildResponse("000", "Transacción aprobada", expectedToken)
        val transaction = Transaction(responseBody)
        assertThat(transaction.token, equalTo(expectedToken))
    }

    @Test
    fun shouldReturnTheCodeFromTheResponseBody() {
        val expectedCode = "000"
        val responseBody = buildResponse(expectedCode, "Some error message")
        val transaction = Transaction(responseBody)
        assertThat(transaction.code, equalTo(expectedCode))
    }

    @Test
    fun shouldReturnTheMessageFromTheResponseBody() {
        val expectedMessage = RandomStringUtils.randomAlphabetic(15)
        val responseBody = buildResponse("123", expectedMessage)
        val transaction = Transaction(responseBody)
        assertThat(transaction.message, equalTo(expectedMessage))
    }

    @Test
    fun shouldReturnTrueWhenTransactionIsSuccessful() {
        val responseBody = buildResponse("000", RandomStringUtils.randomAlphabetic(15), "asdf")
        val transaction = Transaction(responseBody)
        assertThat(transaction.isSuccessful, equalTo(true))
    }

    @Test
    fun shouldReturnFalseWhenTransactionIsNotSuccessful() {
        val responseBody = buildResponse("211", "")
        val transaction = Transaction(responseBody)
        assertThat(transaction.isSuccessful, equalTo(false))
    }

    @Ignore
    @Test
    fun shouldThrowIllegalArgumentExceptionIfBuiltWithInvalidJson() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectCause(instanceOf<Throwable>(JSONException::class.java))
        Transaction("")
    }

    @Ignore
    @Test
    fun shouldThrowIllegalArgumentExceptionIfBuiltWithValidJSONWithMissingFields() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectCause(instanceOf<Throwable>(JSONException::class.java))
        Transaction("123")
    }
}