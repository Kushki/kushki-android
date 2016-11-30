package com.kushkipagos.android

import org.json.JSONException
import org.json.JSONObject

class Transaction(responseBody: String) {

    val code: String
    val message: String
    val token: String
    val isSuccessful: Boolean

    init {
        try {
            val jsonResponse = JSONObject(responseBody)
            code = jsonResponse.getString("response_code")
            message = jsonResponse.getString("response_text")
            token = jsonResponse.getString("transaction_token")
            isSuccessful = "000" == code
        } catch (jsonException: JSONException) {
            throw IllegalArgumentException(jsonException)
        }

    }
}
