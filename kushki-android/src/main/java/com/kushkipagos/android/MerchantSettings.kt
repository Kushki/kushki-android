package com.kushkipagos.android

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
        }catch (jsonException: JSONException){
            code = try {
                val code = JSONObject(jsonResponse)
                code.getString("code")
            } catch (jsonException: JSONException) {
                ""
            }
            message = try {

                val message = JSONObject(jsonResponse)

                message.getString("message")

            } catch (jsonException: JSONException) {
                ""
            }

        }
    }

}