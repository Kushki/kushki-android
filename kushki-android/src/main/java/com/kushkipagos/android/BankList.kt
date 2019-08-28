package com.kushkipagos.android

import org.json.JSONException
import org.json.JSONArray

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