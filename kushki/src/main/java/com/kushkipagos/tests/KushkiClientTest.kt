package com.kushkipagos.tests

import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.KushkiClient
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import java.io.IOException

class KushkiClientTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun shouldWrapIOExceptionWithKushkiException() {
        val kushkiClient = KushkiClient(TestEnvironment.INVALID, "10000001656015280078454110039965")
        expectedException.expect(KushkiException::class.java)
        expectedException.expectCause(CoreMatchers.instanceOf<Throwable>(IOException::class.java))
        kushkiClient.post("/this-endpoint-does-not-exist", "")
        kushkiClient.get("/this-endpoint-does-not-exist")
        kushkiClient.post_secure("/this-endpoint-does-not-exist","")
    }



}