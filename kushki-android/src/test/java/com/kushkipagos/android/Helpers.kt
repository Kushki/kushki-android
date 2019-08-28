package com.kushkipagos.android

import org.json.JSONArray
import org.json.JSONObject

internal object Helpers {

    fun buildResponse(code: String, message: String, token: String = ""): String {
        return JSONObject()
                .put("code", code)
                .put("message", message)
                .put("token", token)
                .toString()
    }

    fun buildBankListResponse():String {
        val response = JSONArray()

        for (i in 0..3){
            var json = JSONObject()
            json.put("code",i)
            json.put("name","Banco $i")
            response.put(i,json)
        }

        return response.toString()


    }
}
