package com.kushkipagos.android

import org.json.JSONObject
import java.util.*

internal class KushkiJsonBuilder {

    fun buildJson(publicMerchantId: String, card: Card): String {
        return buildJsonObject(publicMerchantId, card).toString()
    }

    fun buildJson(publicMerchantId: String, card: Card, totalAmount: Double): String {
        return buildJsonObject(publicMerchantId, card, totalAmount).toString()
    }

    private fun buildJsonObject(publicMerchantId: String, card: Card, totalAmount: Double): JSONObject {
        return buildJsonObject(publicMerchantId, card)
                .put("token_type", "transaction-token")
                .put("amount", String.format(Locale.ENGLISH, "%.2f", totalAmount))
    }

    private fun buildJsonObject(publicMerchantId: String, card: Card): JSONObject {
        return JSONObject()
                .put("remember_me", "0")
                .put("deferred_payment", "0")
                .put("language_indicator", "es")
                .put("merchant_identifier", publicMerchantId)
                .put("card", card.toJsonObject())
                .put("token_type", "subscription-token")
    }
}