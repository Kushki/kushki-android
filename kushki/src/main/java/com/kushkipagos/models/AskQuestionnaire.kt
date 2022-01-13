package com.kushkipagos.models

import org.json.JSONObject

class AskQuestionnaire(private val secureServiceId: String, private val secureService:String,
                       private val cityCode: String, private val stateCode: String, private val phone: String,
                       private val expeditionDate: String, private val merchantId:String) {

    fun toJsonObject(): JSONObject {

        val request = JSONObject()
        val confrontaInfo = JSONObject()
        val confrontaBiometrics = JSONObject()

        confrontaBiometrics
                .put("cityCode", cityCode)
                .put("stateCode", stateCode)
                .put("phone", phone)
                .put("expeditionDate",expeditionDate)
        confrontaInfo
                .put("confrontaBiometrics",confrontaBiometrics)
        request
                .put("secureServiceId",secureServiceId)
                .put("secureService",secureService)
                .put("confrontaInfo",confrontaInfo)
                .put("merchantId",merchantId)
        return request
    }
}
