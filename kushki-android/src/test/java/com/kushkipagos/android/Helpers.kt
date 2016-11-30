package com.kushkipagos.android

import org.json.JSONObject

internal object Helpers {

    fun buildResponse(code: String, message: String, tokenValidity: String = "",
                      token: String = ""): String {
        return JSONObject()
                .put("response_code", code)
                .put("response_text", message)
                .put("transaction_token_validity", tokenValidity)
                .put("transaction_token", token)
                .toString()
    }
}
