package com.kushkipagos.android

import org.json.JSONObject
import java.util.*

internal class KushkiJsonBuilder {

    fun buildJson(card: Card): String {
        return buildJsonObject(card).toString()
    }

    fun buildJson(card: Card, totalAmount: Double): String {
        return buildJsonObject(card, totalAmount).toString()
    }

    private fun buildJsonObject(card: Card, totalAmount: Double): JSONObject {
        return buildJsonObject(card)
                .put("totalAmount", totalAmount)
    }

    private fun buildJsonObject(card: Card): JSONObject {
        return JSONObject()
                .put("card", card.toJsonObject())
    }
}