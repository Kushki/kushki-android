package com.kushkipagos.android

import org.json.JSONObject

internal object Helpers {

    fun buildResponse(code: String, message: String, token: String = ""): String {
        return JSONObject()
                .put("code", code)
                .put("message", message)
                .put("token", token)
                .toString()
    }
}
