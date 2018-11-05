package com.kushkipagos.android

class Kushki internal constructor(publicMerchantId: String, currency: String,
                                  environment: Environment) {
    private val kushkiClient: KushkiClient
    private val kushkiJsonBuilder: KushkiJsonBuilder

    init {
        this.kushkiClient = KushkiClient(environment, publicMerchantId)
        this.kushkiJsonBuilder = KushkiJsonBuilder()
    }

    @Throws(KushkiException::class)
    @JvmOverloads
    fun requestToken(card: Card, totalAmount: Double,regional:Boolean = false): Transaction {
        return kushkiClient.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(card, totalAmount),regional)
    }

    @JvmOverloads
    @Throws(KushkiException::class)
    fun requestSubscriptionToken(card: Card,regional:Boolean = false): Transaction {
        return kushkiClient.post(SUBSCRIPTION_TOKENS_PATH, kushkiJsonBuilder.buildJson(card),regional)
    }

    companion object {

        private const val TOKENS_PATH = "tokens"
        private const val SUBSCRIPTION_TOKENS_PATH = "subscription-tokens"
    }
}