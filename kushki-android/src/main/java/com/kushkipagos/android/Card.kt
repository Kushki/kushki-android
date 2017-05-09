package com.kushkipagos.android

import org.json.JSONObject

class Card(private val name: String, private val number: String, private val cvv: String,
           private val expiryMonth: String, private val expiryYear: String) {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("name", name)
                .put("number", number)
                .put("expiryMonth", expiryMonth)
                .put("expiryYear", expiryYear)
                .put("cvv", cvv)
    }
}
