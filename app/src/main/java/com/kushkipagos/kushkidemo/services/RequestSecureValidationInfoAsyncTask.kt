package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.SecureValidation
import com.kushkipagos.kushkidemo.contracts.AbstractRequestSecureValidationInfoAsyncTask
import com.kushkipagos.models.AskQuestionnaire

class RequestSecureValidationInfoAsyncTask: AbstractRequestSecureValidationInfoAsyncTask {
    constructor(context: Context): super(context)

    @Throws(KushkiException::class)
    override fun requestAskQuestionnaire(askQuestionnaire: AskQuestionnaire): SecureValidation {
        return _kushki.requestSecureValidation(AskQuestionnaire(askQuestionnaire.toJsonObject().optString("secureServiceId"),
            askQuestionnaire.toJsonObject().optString("secureService"),
            askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("cityCode"),
            askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("stateCode"),
            askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("phone"),
            askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("expeditionDate"),
            askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("merchantId")
        ))
    }
}