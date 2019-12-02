package com.kushkipagos.android

import org.json.JSONObject
import java.net.URLEncoder

class CashOut(private val name: String, private val lastName: String,private val documentNumber: String,
              private val documentType: String, private val email: String,
              private val totalAmount: Double, private val currency: String,private val description:String = "" ) {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("name", URLEncoder.encode(name, "UTF-8"))
                .put("lastName", URLEncoder.encode(lastName, "UTF-8"))
                .put("documentNumber", documentNumber)
                .put("documentType", documentType)
                .put("email", email)
                .put("totalAmount", totalAmount)
                .put("currency", currency)
                .put("description", description)

    }

}