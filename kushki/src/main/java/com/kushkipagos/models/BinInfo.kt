package com.kushkipagos.models

import org.json.JSONException
import org.json.JSONObject

class BinInfo(responseBody:String) {

    var bank: String = ""
    var brand: String = ""
    var cardType: String = ""
    var code: String = ""
    var message: String = ""
    val jsonResponse: JSONObject = JSONObject(responseBody)

    init{
        try{
            bank = jsonResponse.getString("bank")
            brand = jsonResponse.getString("brand")
            cardType = jsonResponse.getString("cardType")
        }
        catch (jsonException: JSONException){
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