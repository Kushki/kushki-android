package com.kushkipagos.models

import org.json.JSONException
import org.json.JSONObject


class MerchantSettings(responseBody: String) {

    var country: String = ""
    var processor_name: String = ""
    var merchant_name: String = ""
    var prodAccountId:String= ""
    var prodBaconKey: String = ""
    var sandboxBaconKey: String = ""
    var sandboxAccountId:String= ""
    var processors: JSONObject = JSONObject()
    var code:String = ""
    var message: String = ""
    var is3DSecure:Boolean = false
    var sandboxEnable:Boolean = false
    var jsonResponse: JSONObject = JSONObject(responseBody)

    init {
        try {
            country = jsonResponse.getString("country")
            processor_name = jsonResponse.getString("processor_name")
            merchant_name = jsonResponse.getString("merchant_name")
            prodAccountId=jsonResponse.getString("prodAccountId")
            prodBaconKey = jsonResponse.getString("prodBaconKey")
            sandboxAccountId = jsonResponse.getString("sandboxAccountId")
            sandboxBaconKey = jsonResponse.getString("sandboxBaconKey")
            processors = jsonResponse.getJSONObject("processors")
            is3DSecure = jsonResponse.getBoolean("active_3dsecure")
            sandboxEnable = jsonResponse.getBoolean("sandboxEnable")

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