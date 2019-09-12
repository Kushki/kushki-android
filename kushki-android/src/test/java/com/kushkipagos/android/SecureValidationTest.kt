package com.kushkipagos.android

import com.kushkipagos.android.Helpers.buildSecureValidationResponse
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class SecureValidationTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun shouldReturnTheQuestionsFromTheResponseBody() {
        val expectedQuestionnaireCode = "2"
        val responseBody = buildSecureValidationResponse("000", "Por favor, complete las preguntas", expectedQuestionnaireCode)
        val secureValidation = SecureValidation(responseBody)
        assertThat(secureValidation.questionnaireCode, equalTo(expectedQuestionnaireCode))
        assertThat(secureValidation.questions.length(), equalTo(3))
    }

    @Test
    fun shouldReturnTheCodeFromTheResponseBody() {
        val expectedCode = "000"
        val responseBody = buildSecureValidationResponse(expectedCode, "Some error message")
        val secureValidation = SecureValidation(responseBody)
        assertThat(secureValidation.code, equalTo(expectedCode))
    }

    @Test
    fun shouldReturnTheMessageFromTheResponseBody() {
        val expectedMessage = RandomStringUtils.randomAlphabetic(15)
        val responseBody = buildSecureValidationResponse("123", expectedMessage)
        val secureValidation = SecureValidation(responseBody)
        assertThat(secureValidation.message, equalTo(expectedMessage))
    }

    @Test
    fun shouldReturnTrueWhenSecureValidationIsSuccessful() {
        val responseBody = buildSecureValidationResponse("000", RandomStringUtils.randomAlphabetic(15), "asdf")
        val secureValidation = SecureValidation(responseBody)
        assertThat(secureValidation.isSuccessful, equalTo(true))
    }

    @Test
    fun shouldReturnFalseWhenSecureValidationIsNotSuccessful() {
        val responseBody = buildSecureValidationResponse("211", "")
        val secureValidation = SecureValidation(responseBody)
        assertThat(secureValidation.isSuccessful, equalTo(false))
    }


}