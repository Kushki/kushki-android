package com.kushkipagos.android

import org.json.JSONException
import org.json.JSONObject

class Transaction(responseBody: String) {

    var code: String
    var message: String
    var token: String = ""
    var isSuccessful: Boolean = false
    val jsonResponse: JSONObject = JSONObject(responseBody)

    init {
        try {
            code = "000"
            message = ""
            token = jsonResponse.getString("token")
            if (token != "") isSuccessful = "000" == code
            else {
                code = jsonResponse.getString("code")
                message = jsonResponse.getString("message")
            }
        } catch (jsonException: JSONException) {
            code = jsonResponse.getString("code")
            message = jsonResponse.getString("message")
        }

    }
}
