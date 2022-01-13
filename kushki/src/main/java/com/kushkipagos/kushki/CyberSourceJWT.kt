package com.kushkipagos.kushki

import org.json.JSONException
import org.json.JSONObject

class CyberSourceJWT(responseBody:String) {
    var response:JSONObject = JSONObject()
    var jwt:String = ""
    init{
        try{
            response = JSONObject(responseBody)
            jwt = try {
                response.getString("jwt")
            } catch (jsonException: JSONException) {
                ""
            }
        }
        catch (jsonException: JSONException){
            System.out.println("JSON Error")
        }
    }
}