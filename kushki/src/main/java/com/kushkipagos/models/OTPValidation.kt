package com.kushkipagos.models
import org.json.JSONObject

class OTPValidation(private val secureServiceId: String, private val otpValue:String) {


    fun toJsonObject(): JSONObject {

        val request = JSONObject()

        return request
                .put("secureServiceId",secureServiceId)
                .put("otpValue",otpValue)
    }
}