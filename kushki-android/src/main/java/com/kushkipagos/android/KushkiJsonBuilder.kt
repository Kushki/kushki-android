package com.kushkipagos.android

import org.json.JSONObject
import java.util.*

internal class KushkiJsonBuilder {

    fun buildJson(card: Card, currency: String): String {
        return buildJsonObject(card, currency).toString()
    }

    fun buildJson(card: Card, totalAmount: Double, currency: String): String {
        return buildJsonObject(card, totalAmount, currency).toString()
    }

    fun buildJson(totalAmount: Double, currency: String, returnUrl: String, description: String, email: String) : String {
        return buildJsonObject(totalAmount, currency, returnUrl, description, email).toString()
    }

    fun buildJson(totalAmount: Double, currency: String, returnUrl: String, description: String) : String {
        return buildJsonObject(totalAmount, currency, returnUrl, description).toString()
    }

    fun buildJson(totalAmount: Double, currency: String, returnUrl: String) : String {
        return buildJsonObject(totalAmount, currency, returnUrl).toString()
    }


    private fun buildJsonObject(card: Card, totalAmount: Double, currency: String): JSONObject {
        return buildJsonObject(card, currency)
                .put("totalAmount", totalAmount)
    }

    private fun buildJsonObject(card: Card, currency: String): JSONObject {
        return JSONObject()
                .put("card", card.toJsonObject())
                .put("currency", currency)
    }

    private fun buildJsonObject(totalAmount: Double, currency: String, returnUrl: String, description: String, email: String): JSONObject {
        return JSONObject()
                .put("totalAmount", totalAmount)
                .put("currency", currency)
                .put("returnUrl", returnUrl)
                .put("description", description)
                .put("email", email)
    }

    private fun buildJsonObject(totalAmount: Double, currency: String, returnUrl: String, description: String): JSONObject {
        return JSONObject()
                .put("totalAmount", totalAmount)
                .put("currency", currency)
                .put("returnUrl", returnUrl)
                .put("description", description)
    }
    private fun buildJsonObject(totalAmount: Double, currency: String, returnUrl: String): JSONObject {
        return JSONObject()
                .put("totalAmount", totalAmount)
                .put("currency", currency)
                .put("returnUrl", returnUrl)
    }
}