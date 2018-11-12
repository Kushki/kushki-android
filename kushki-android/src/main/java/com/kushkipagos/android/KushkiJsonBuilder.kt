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

    private fun buildJsonObject(card: Card, totalAmount: Double, currency: String): JSONObject {
        return buildJsonObject(card, currency)
                .put("totalAmount", totalAmount)
    }

    private fun buildJsonObject(card: Card, currency: String): JSONObject {
        return JSONObject()
                .put("card", card.toJsonObject())
                .put("currency", currency)
    }
}