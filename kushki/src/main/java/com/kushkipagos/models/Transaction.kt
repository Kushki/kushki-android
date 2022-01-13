package com.kushkipagos.models

import com.kushkipagos.kushki.Validated3DSResponse
import org.json.JSONException
import org.json.JSONObject

data class Security3DS (
    val acsURL: String,
    val paReq: String,
    val authenticationTransactionId: String,
    val authRequired: String,
    val specificationVersion: String
)

class Transaction(responseBody: String) {

    var code: String
    var message: String
    var token: String = ""
    var settlement: Double = 0.0
    var secureId: String
    var secureService: String
    var isSuccessful: Boolean = false
    var isSecure3DS: Boolean = false
    var security: Security3DS? = null
    var validated3DS: Validated3DSResponse? = null

    init {
        val jsonResponse = JSONObject(responseBody)
        settlement = try {
            jsonResponse.getDouble("settlement")
        } catch (jsonException: JSONException) {
            0.0
        }

        secureId = try {
            jsonResponse.getString("secureId")
        } catch (jsonException: JSONException) {
            ""
        }

        secureService = try {
            jsonResponse.getString("secureService")
        } catch (jsonException: JSONException) {
            ""
        }

        val acsURL3DS = try {
            jsonResponse.getJSONObject("security").getString("acsURL")
        } catch (jsonException: JSONException) {
            ""
        }

        val paReq3DS = try {
            jsonResponse.getJSONObject("security").getString("paReq")
        } catch (jsonException: JSONException) {
            ""
        }

        val authenticationTransactionId3DS = try {
            jsonResponse.getJSONObject("security").getString("authenticationTransactionId")
        } catch (jsonException: JSONException) {
            ""
        }

        val authRequired3DS = try {
            jsonResponse.getJSONObject("security").getString("authRequired")
        } catch (jsonException: JSONException) {
            ""
        }

        val specificationVersion3DS = try {
            jsonResponse.getJSONObject("security").getString("specificationVersion")
        } catch (jsonException: JSONException) {
            ""
        }

        isSecure3DS = !secureId.isEmpty()
                && !authRequired3DS.isEmpty()
                && !secureService.isEmpty()
                && secureService == "3dsecure"

        if (isSecure3DS)
            security = Security3DS(
                acsURL3DS,
                paReq3DS,
                authenticationTransactionId3DS,
                authRequired3DS,
                specificationVersion3DS
            )

        try {
            code = "000"
            message = ""
            token = if (jsonResponse.has("token")) jsonResponse.getString("token") else ""

            if (token != "") isSuccessful = "000" == code
            else {
                code = jsonResponse.getString("code")
                message = jsonResponse.getString("message")
            }
        } catch (jsonException: JSONException) {
            code = jsonResponse.getString("code")
            message = jsonResponse.getString("message")
        }

    }
}
