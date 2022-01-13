package com.kushkipagos.models

import org.json.JSONException
import org.json.JSONObject

class   SiftScienceObject(responseBody:String) {
    var userId: String =""
    var sessionId:String=""
    var code: String = ""
    var message: String = ""
    val jsonResponse: JSONObject=JSONObject(responseBody)

    init{
        try{
            userId=jsonResponse.getString("userId")
            sessionId=jsonResponse.getString("sessionId")
        }catch (jsonException: JSONException){
            code = try {
                jsonResponse.getString("code")
            } catch (jsonException: JSONException) {
                ""
            }
            message = try {
                jsonResponse.getString("message")
            } catch (jsonException: JSONException) {
                ""
            }
        }
    }
}