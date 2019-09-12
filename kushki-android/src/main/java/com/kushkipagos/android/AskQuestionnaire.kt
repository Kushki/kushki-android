package com.kushkipagos.android

import org.json.JSONObject

class AskQuestionnaire(private val secureServiceId: String, private val secureService:String,
                       private val cityCode: String, private val stateCode: String, private val phone: String,
                       private val expeditionDocumentDate: String) {

    fun toJsonObject(): JSONObject {

        val request = JSONObject()
        val confrontaInfo = JSONObject()
        val confrontaBiometrics = JSONObject()

        confrontaBiometrics
                .put("cityCode", cityCode)
                .put("stateCode", stateCode)
                .put("phone", phone)
                .put("expeditionDocumentDate",expeditionDocumentDate)
        confrontaInfo
                .put("confrontaBiometrics",confrontaBiometrics)
        request
                .put("secureServiceId",secureServiceId)
                .put("secureService",secureService)
                .put("confrontaInfo",confrontaInfo)
        return request
    }
}
