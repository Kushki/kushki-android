package me.ivanzamora.library

import android.content.Context
import com.kushkipagos.library.Greeting
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AndroidGreetingTest {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun testExample() {
        assertTrue("Check Android is mentioned", Greeting().greeting().contains("Android"))
    }

    @Test
    fun kushkiAuthInitTest() {

        //val kushkiAuth = KushkiAuth(mockContext)
        //kushkiAuth.initAmpliFy();
        assertTrue("Check Android is mentioned", Greeting().greeting().contains("Android"))
    }
}