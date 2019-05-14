package com.kushkipagos.android

import org.json.JSONObject
import java.util.*

internal class KushkiJsonBuilder {

    fun buildJson(card: Card, currency: String): String {
        return buildJsonObject(card, currency).toString()
    }

    fun buildJson(card: Card, totalAmount: Double, currency: String, months: Int): String {
        return buildJsonObject(card, totalAmount, currency, months).toString()
    }

    private fun buildJsonObject(card: Card, totalAmount: Double, currency: String, months: Int): JSONObject {
        var response =  buildJsonObject(card, currency)

        if(month!== null && month>0) {
            response..put("months", months)
        }

        return  response

    }

    private fun buildJsonObject(card: Card, currency: String): JSONObject {
        return JSONObject()
                .put("card", card.toJsonObject())
                .put("currency", currency)
    }
}