package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestMerchantSettingsTask
import com.kushkipagos.models.MerchantSettings

class RequestMerchantSettingsTask: AbstractRequestMerchantSettingsTask {
    constructor(context: Context): super(context)

    @Throws(KushkiException::class)
    override fun requestMerchantSettings(): MerchantSettings {
        return _kushki.requestMerchantSettings()
    }
}