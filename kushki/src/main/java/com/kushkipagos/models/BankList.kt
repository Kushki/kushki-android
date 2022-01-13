package com.kushkipagos.models

import org.json.JSONArray
import org.json.JSONException

class BankList(responseBody:String) {

    var banks: JSONArray = JSONArray()
    init{
        try{
            banks = JSONArray(responseBody)
        }
        catch (jsonException: JSONException){
            println("JSON Error")
        }
    }

}