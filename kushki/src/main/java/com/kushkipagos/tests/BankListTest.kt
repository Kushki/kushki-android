package com.kushkipagos.tests

import com.kushkipagos.models.BankList
import com.kushkipagos.tests.Helpers.buildBankListResponse
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BankListTest {



    @Test
    fun shouldReturnTheTokenFromTheResponseBody() {
        val responseBody = buildBankListResponse()
        val bankList = BankList(responseBody)
        assertThat(bankList.banks.length(), notNullValue())
    }


}