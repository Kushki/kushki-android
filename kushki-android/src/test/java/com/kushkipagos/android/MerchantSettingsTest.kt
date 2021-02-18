package com.kushkipagos.android

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class MerchantSettingsTest {

    val kushkiMerchantSettingsInfo = Kushki("10000002036955013614148494909956","USD",TestEnvironment.LOCAL)

    @Test
    @Throws(KushkiException::class)
    fun shouldReturnTheMerchantSettingsInfo(){
        val expectedProcessorName ="Credimatic Processor"
        val merchantSettingsInfo = kushkiMerchantSettingsInfo.requestMerchantSettings()
        System.out.println(merchantSettingsInfo.country)
        System.out.println(merchantSettingsInfo.processors)
        MatcherAssert.assertThat(merchantSettingsInfo.processor_name, CoreMatchers.equalTo(expectedProcessorName))

    }
}