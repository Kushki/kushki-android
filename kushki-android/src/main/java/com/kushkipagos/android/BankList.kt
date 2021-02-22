package com.kushkipagos.android

import org.json.JSONArray
import org.json.JSONException

class BankList(responseBody:String) {

    var banks: JSONArray = JSONArray()
    init{
        try{
            banks = JSONArray(responseBody)
        }
        catch (jsonException: JSONException){
            System.out.println("JSON Error")
        }
    }

}