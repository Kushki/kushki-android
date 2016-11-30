package com.kushkipagos.android

class Kushki internal constructor(private val publicMerchantId: String, currency: String,
                                  environment: Environment, aurusEncryption: AurusEncryption) {
    private val client: AurusClient
    private val kushkiJsonBuilder: KushkiJsonBuilder

    constructor(publicMerchantId: String, currency: String, environment: Environment) :
            this(publicMerchantId, currency, environment, AurusEncryption())

    init {
        this.client = AurusClient(environment, aurusEncryption)
        this.kushkiJsonBuilder = KushkiJsonBuilder()
    }

    @Throws(KushkiException::class)
    fun requestToken(card: Card, totalAmount: Double): Transaction {
        return client.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(publicMerchantId, card, totalAmount))
    }

    @Throws(KushkiException::class)
    fun requestSubscriptionToken(card: Card): Transaction {
        return client.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(publicMerchantId, card))
    }

    companion object {

        private const val TOKENS_PATH = "/tokens"
    }
}