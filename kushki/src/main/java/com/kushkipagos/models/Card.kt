package com.kushkipagos.models

import org.json.JSONObject
import java.net.URLEncoder

class Card(private val name: String, private val number: String, private val cvv: String,
           private val expiryMonth: String, private val expiryYear: String) {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("name", URLEncoder.encode(name, "UTF-8"))
                .put("number", number)
                .put("expiryMonth", expiryMonth)
                .put("expiryYear", expiryYear)
                .put("cvv", cvv)
    }
}
