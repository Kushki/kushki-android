package com.kushkipagos.android

import org.json.JSONObject

class Transfer(private val amount: Amount, private val callbackUrl: String,private val userType: String,
               private val documentType: String, private val documentNumber: String,
               private val email: String, private val currency: String,private val paymentDescription:String = "") {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("amount", amount.toJsonObject())
                .put("callbackUrl", callbackUrl)
                .put("userType", userType)
                .put("documentType",documentType)
                .put("documentNumber", documentNumber)
                .put("email", email)
                .put("currency",currency)
                .put("paymentDescription",paymentDescription)
    }
}


