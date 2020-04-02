package com.kushkipagos.android

import org.json.JSONObject

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

    fun buildJson(amount: Amount, callbackUrl: String, userType: String, documentType:String,documentNumber:String,
                  email:String,currency:String) : String {
        return buildJsonObject(amount, callbackUrl, userType,documentType,documentNumber,email,currency).toString()
    }

    fun buildJson(amount: Amount, callbackUrl: String, userType: String, documentType:String,documentNumber:String,
                  email:String,currency:String, paymentDescription: String) : String {
        return buildJsonObject(amount, callbackUrl, userType,documentType,documentNumber,email,currency,
                paymentDescription).toString()
    }
    fun buildJson(name: String, lastName: String, identification: String, documentType: String,
                  email: String,totalAmount: Double, currency: String, description: String) : String {
        return buildJsonObject(name, lastName, identification, documentType,
                email,totalAmount, currency, description).toString()
    }

    fun buildJson(name: String, lastName: String, identification: String, documentType: String,
                  email: String,totalAmount: Double, currency: String) : String {
        return buildJsonObject(name, lastName, identification, documentType,
                email,totalAmount, currency).toString()
    }

    fun buildJson(name: String, lastName: String, identification: String, documentType: String,
                  totalAmount: Double, currency: String) : String {
        return buildJsonObject(name, lastName, identification, documentType,
                totalAmount, currency).toString()
    }

    fun buildJson(transferSubscriptions: TransferSubscriptions) : String {
        return transferSubscriptions.toJsonObject().toString()
    }

    fun buildJson(askQuestionnaire: AskQuestionnaire): String {
        return askQuestionnaire.toJsonObject().toString()
    }
    fun buildJson(validateAnswers: ValidateAnswers): String {
        return validateAnswers.toJsonObject().toString()
    }

    fun buildJson(email: String, currency: String, callbackUrl: String, cardNumber: String): String {
        return buildJsonObject(email, currency, callbackUrl, cardNumber).toString()
    }

    fun buildJson(email: String, currency: String, callbackUrl: String): String {
        return buildJsonObject(email, currency, callbackUrl).toString()
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

    private fun buildJsonObject(totalAmount: Double, currency: String, returnUrl: String, email: String): JSONObject {
        return JSONObject()
                .put("totalAmount", totalAmount)
                .put("currency", currency)
                .put("returnUrl", returnUrl)
                .put("email", email)
    }
    private fun buildJsonObject(totalAmount: Double, currency: String, returnUrl: String): JSONObject {
        return JSONObject()
                .put("totalAmount", totalAmount)
                .put("currency", currency)
                .put("returnUrl", returnUrl)
    }
    private fun buildJsonObject(amount: Amount, callbackUrl: String, userType: String, documentType:String,documentNumber:String,
                                email:String,currency:String): JSONObject {
        return JSONObject()
                .put("amount", amount.toJsonObject())
                .put("callbackUrl", callbackUrl)
                .put("userType", userType)
                .put("documentType", documentType)
                .put("documentNumber", documentNumber)
                .put("email", email)
                .put("currency", currency)
    }
    private fun buildJsonObject(amount: Amount, callbackUrl: String, userType: String, documentType:String,documentNumber:String,
                                email:String,currency:String, paymentDescription: String): JSONObject {
        return JSONObject()
                .put("amount", amount.toJsonObject())
                .put("callbackUrl", callbackUrl)
                .put("userType", userType)
                .put("documentType", documentType)
                .put("documentNumber", documentNumber)
                .put("email", email)
                .put("currency", currency)
                .put("paymentDescription", paymentDescription)
    }

    private fun buildJsonObject(name: String, lastName: String, identification: String, documentType: String,
                                email: String,totalAmount: Double, currency: String, description: String): JSONObject {
        return JSONObject()
                .put("name", name)
                .put("lastName", lastName)
                .put("identification", identification)
                .put("documentType", documentType)
                .put("email", email)
                .put("totalAmount", totalAmount)
                .put("currency", currency)
                .put("description", description)
    }
    private fun buildJsonObject(name: String, lastName: String, identification: String, documentType: String,
                                email: String,totalAmount: Double, currency: String): JSONObject {
        return JSONObject()
                .put("name", name)
                .put("lastName", lastName)
                .put("identification", identification)
                .put("documentType", documentType)
                .put("email", email)
                .put("totalAmount", totalAmount)
                .put("currency", currency)
    }

    private fun buildJsonObject(name: String, lastName: String, identification: String, documentType: String
                                ,totalAmount: Double, currency: String): JSONObject {
        return JSONObject()
                .put("name", name)
                .put("lastName", lastName)
                .put("identification", identification)
                .put("documentType", documentType)
                .put("totalAmount", totalAmount)
                .put("currency", currency)
    }

    private fun buildJsonObject(transferSubscriptions: TransferSubscriptions): JSONObject {
        return JSONObject(transferSubscriptions.toJsonObject())
    }

    fun buildJsonObject(email: String, currency: String, callbackUrl: String, cardNumber: String): JSONObject {
        return JSONObject()
                .put("email", email)
                .put("currency", currency)
                .put("callbackUrl", callbackUrl)
                .put("cardNumber", cardNumber)
    }

    fun buildJsonObject(email: String, currency: String, callbackUrl: String): JSONObject {
        return JSONObject()
                .put("email", email)
                .put("currency", currency)
                .put("callbackUrl", callbackUrl)
    }
}