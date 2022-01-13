package com.kushkipagos.models

import org.json.JSONObject

class TransferSubscriptions(private val documentNumber: String, private val bankCode: String, private val name: String,
                            private val lastName: String, private val accountNumber: String,
                            private val documentType:String,
                            private val accountType:String , private val totalAmount: Number,
                            private val email:String, private val currency:String
                            ) {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("documentNumber", documentNumber)
                .put("bankCode", bankCode)
                .put("name", name)
                .put("lastName", lastName)
                .put("accountNumber",accountNumber)
                .put("documentType",documentType)
                .put("accountType",accountType)
                .put("totalAmount",totalAmount)
                .put("email",email)
                .put("currency",currency)
    }
}
