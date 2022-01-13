package com.kushkipagos.models

import org.json.JSONArray
import org.json.JSONObject

class ValidateAnswers(private val secureServiceId: String, private val secureService:String,
                      private val questionnaireCode: String, private val answers: JSONArray ) {

    fun toJsonObject(): JSONObject {

        val request = JSONObject()
        val confrontaInfo = JSONObject()

        confrontaInfo
                .put("questionnaireCode",questionnaireCode)
                .put("answers",answers)

        return request
                .put("secureServiceId",secureServiceId)
                .put("secureService",secureService)
                .put("confrontaInfo",confrontaInfo)
    }
}
