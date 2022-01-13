package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.CardSecureValidation
import com.kushkipagos.kushkidemo.contracts.AbstractRequestCardSecureValidationAsyncTask
import com.kushkipagos.models.OTPValidation

class RequestCardSecureValidationAsyncTask: AbstractRequestCardSecureValidationAsyncTask {
    constructor(context: Context): super(context)

    @Throws(KushkiException::class)
    override fun requestOTPValidation(otpValidation: OTPValidation): CardSecureValidation {
        return _kushki.requestSecureValidation(OTPValidation(otpValidation.toJsonObject().optString("secureServiceId"),
                                                                 otpValidation.toJsonObject().optString("otpValue")))
    }
}