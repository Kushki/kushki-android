package com.kushkipagos.android

import org.json.JSONObject

class TransferSubscriptions(private val documentNumber: String, private val bankCode: String, private val name: String,
                            private val lastName: String, private val cityCode: String,private val stateCode: String,
                            private val accountNumber: String, private val expeditionDate:String,
                            private val phone:String, private val documentType:String,
                            private val accountType:String , private val totalAmount: Number,
                            private val cuestionaryCode:String, private val email:String, private val currency:String
                            ) {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("documentNumber", documentNumber)
                .put("bankCode", bankCode)
                .put("name", name)
                .put("lastName", lastName)
                .put("cityCode", cityCode)
                .put("stateCode",stateCode)
                .put("accountNumber",accountNumber)
                .put("expeditionDate",expeditionDate)
                .put("phone",phone)
                .put("documentType",documentType)
                .put("accountType",accountType)
                .put("totalAmount",totalAmount)
                .put("cuestionaryCode",cuestionaryCode)
                .put("email",email)
                .put("currency",currency)
    }
}
