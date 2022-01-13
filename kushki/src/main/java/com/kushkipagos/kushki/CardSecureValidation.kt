package com.kushkipagos.kushki

import org.json.JSONException
import org.json.JSONObject

class CardSecureValidation (responseBody: String)  {

    var code: String
    var message: String
    var isSuccessful: Boolean = false
    val jsonResponse: JSONObject = JSONObject(responseBody)
    var is3DSValidationOk: Boolean = false

    init {

        try {
            code = "000"
            message = ""

            code = jsonResponse.getString("code")
            message = jsonResponse.getString("message")
            isSuccessful = SecureValidationResponse.CODE_OTP.code == code
            is3DSValidationOk = message == SecureValidationResponse.MESSAGE_3DS.code && code == SecureValidationResponse.CODE_3DS.code

        } catch (jsonException: JSONException){
            code = jsonResponse.getString("code")
            message = jsonResponse.getString("message")
        }

    }

}