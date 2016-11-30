package com.kushkipagos.android

import org.json.JSONObject

class Card(private val name: String, private val number: String, private val cvv: String,
           private val expiryMonth: String, private val expiryYear: String) {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("name", name)
                .put("number", number)
                .put("expiry_month", expiryMonth)
                .put("expiry_year", expiryYear)
                .put("cvv", cvv)
                .put("card_present", "1")
    }
}
