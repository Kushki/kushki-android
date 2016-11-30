package com.kushkipagos.android

import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import java.io.IOException

class AurusClientTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun shouldWrapIOExceptionWithKushkiException() {
        val aurusClient = AurusClient(TestEnvironment.INVALID, AurusEncryption())
        expectedException.expect(KushkiException::class.java)
        expectedException.expectCause(CoreMatchers.instanceOf<Throwable>(IOException::class.java))
        aurusClient.post("/this-endpoint-does-not-exist", "")
    }
}