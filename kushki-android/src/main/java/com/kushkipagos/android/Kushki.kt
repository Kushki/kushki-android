package com.kushkipagos.android

class Kushki public constructor(publicMerchantId: String, currency: String = "USD",
                                environment: Environment,regional:Boolean) {
    private val kushkiClient: KushkiClient
    private val kushkiJsonBuilder: KushkiJsonBuilder
    private var currency: String

    constructor(publicMerchantId: String, currency: String,
                environment: Environment) :
            this(publicMerchantId, currency,environment, false)

    init {
        this.kushkiClient = KushkiClient(environment, publicMerchantId,regional)
        this.kushkiJsonBuilder = KushkiJsonBuilder()
        this.currency = currency
    }

    @Throws(KushkiException::class)
    fun requestToken(card: Card, totalAmount: Double, months: Int=0): Transaction {
        return kushkiClient.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(card, totalAmount, this.currency,months))
    }

    @Throws(KushkiException::class)
    fun requestSubscriptionToken(card: Card): Transaction {
        return kushkiClient.post(SUBSCRIPTION_TOKENS_PATH, kushkiJsonBuilder.buildJson(card, this.currency))
    }

    companion object {

        private const val TOKENS_PATH = "tokens"
        private const val SUBSCRIPTION_TOKENS_PATH = "subscription-tokens"
    }
}