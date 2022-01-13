package com.kushkipagos.auth

import android.app.Activity
import android.content.Context
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalRenderType
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalUiType
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalConfigurationParameters
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalValidateReceiver
import com.cardinalcommerce.shared.userinterfaces.UiCustomization
import com.kushkipagos.kushki.Validated3DSResponse
import org.json.JSONArray
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Secure3DS {
    private val cardinal: Cardinal = Cardinal.getInstance()

    suspend fun init3DSecure(
        context: Context,
        jwt: String,
        isTest: Boolean
    ) : Boolean {
        val cardinalConfigurationParameters = CardinalConfigurationParameters()

        if (isTest) {
            cardinalConfigurationParameters.environment = CardinalEnvironment.STAGING
        } else {
            cardinalConfigurationParameters.environment = CardinalEnvironment.PRODUCTION
        }
        cardinalConfigurationParameters.requestTimeout = 10000
        cardinalConfigurationParameters.challengeTimeout = 5

        val rTYPE = JSONArray()
        rTYPE.put(CardinalRenderType.OTP)
        rTYPE.put(CardinalRenderType.SINGLE_SELECT)
        rTYPE.put(CardinalRenderType.MULTI_SELECT)
        rTYPE.put(CardinalRenderType.OOB)
        rTYPE.put(CardinalRenderType.HTML)
        cardinalConfigurationParameters.renderType = rTYPE
        cardinalConfigurationParameters.uiType = CardinalUiType.BOTH

        val customizationObject = UiCustomization()
        cardinalConfigurationParameters.uiCustomization = customizationObject
        cardinal.configure(context, cardinalConfigurationParameters)

        return suspendCoroutine<Boolean> { cont ->
            val service = object : CardinalInitService {
                override fun onSetupCompleted(consumerSessionId: String) {
                    cont.resume(true)
                }

                override fun onValidated(validateResponse: ValidateResponse, jwt1: String?) {
                    cont.resume(false)
                }
            }

            cardinal.init(jwt, service);
        }

    }

    suspend fun validate(activity: Activity, transactionId: String, payload: String): Validated3DSResponse {
        return suspendCoroutine<Validated3DSResponse> { continuation ->
            val onValidateFinish = object : CardinalValidateReceiver {
                override fun onValidated(currentContext: Context?, validateResponse: ValidateResponse?, serverJWT: String?) {
                    if (validateResponse != null) {
                        when (validateResponse.actionCode) {
                            CardinalActionCode.SUCCESS, CardinalActionCode.NOACTION -> continuation.resume(Validated3DSResponse(true, "SUCCESS"))
                            CardinalActionCode.ERROR -> continuation.resume(Validated3DSResponse(false, "ERROR"))
                            CardinalActionCode.FAILURE -> continuation.resume(Validated3DSResponse(false, "FAILURE"))
                            CardinalActionCode.CANCEL -> continuation.resume(Validated3DSResponse(false, "CANCEL"))
                            CardinalActionCode.TIMEOUT -> continuation.resume(Validated3DSResponse(false, "TIMEOUT"))
                            else -> {}
                        }
                    }
                }
            }

            try {
                cardinal.cca_continue(transactionId, payload, activity, onValidateFinish)
            } catch (e: Exception) {
                continuation.resume(Validated3DSResponse(false, "ERROR"))
            }
        }
    }
}