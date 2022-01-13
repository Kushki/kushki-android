package com.kushkipagos.kushki

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SecureValidation (responseBody: String)  {

    var questions: JSONArray = JSONArray()
    var code: String
    var message: String
    var questionnaireCode: String = ""
    var isSuccessful: Boolean = false
    val jsonResponse: JSONObject = JSONObject(responseBody)

    init {

        questionnaireCode = try {
            jsonResponse.getString("questionnaireCode")
        } catch (jsonException: JSONException) {
            ""
        }
        try {
            code = "000"
            message = ""
            questions = jsonResponse.getJSONArray("questions")
            if(questionnaireCode!= "") isSuccessful = "000" == code
            else {
                code = jsonResponse.getString("code")
                message = jsonResponse.getString("message")
            }

        } catch (jsonException: JSONException){
            code = jsonResponse.getString("code")
            message = jsonResponse.getString("message")
        }

    }

}