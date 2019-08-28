package com.kushkipagos.android

class Kushki(publicMerchantId: String, currency: String = "USD",
             environment: Environment, regional: Boolean) {
    private val kushkiClient: KushkiClient
    private val kushkiJsonBuilder: KushkiJsonBuilder
    private var currency: String

    constructor(publicMerchantId: String, currency: String,
                environment: Environment) :
            this(publicMerchantId, currency, environment, false)

    init {
        this.kushkiClient = KushkiClient(environment, publicMerchantId, regional)
        this.kushkiJsonBuilder = KushkiJsonBuilder()
        this.currency = currency
    }

    @Throws(KushkiException::class)
    fun requestToken(card: Card, totalAmount: Double): Transaction {
        return kushkiClient.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(card, totalAmount, this.currency))
    }

    @Throws(KushkiException::class)
    fun requestSubscriptionToken(card: Card): Transaction {
        return kushkiClient.post(SUBSCRIPTION_TOKENS_PATH, kushkiJsonBuilder.buildJson(card, this.currency))
    }

    @Throws(KushkiException::class)
    fun cardAsyncTokens(totalAmount: Double, remoteUrl: String, description: String, email: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount
                , this.currency
                , remoteUrl
                , description
                , email)
        )
    }

    @Throws(KushkiException::class)
    fun cardAsyncTokens(totalAmount: Double, remoteUrl: String, email: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount
                , this.currency
                , remoteUrl
                , email
        )
        )
    }

    @Throws(KushkiException::class)
    fun cardAsyncTokens(totalAmount: Double, remoteUrl: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount
                , this.currency
                , remoteUrl
        )
        )
    }

    @Throws(KushkiException::class)
    fun transferTokens(amount: Amount, callbackUrl: String, userType: String, documentType: String,
                       documentNumber: String, email: String, currency: String): Transaction {
        return kushkiClient.post(TRANSFER_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                amount, callbackUrl, userType, documentType, documentNumber, email, currency
        )
        )
    }

    @Throws(KushkiException::class)
    fun transferTokens(amount: Amount, callbackUrl: String, userType: String, documentType: String,
                       documentNumber: String, email: String, currency: String, paymentDescription:String): Transaction {
        return kushkiClient.post(TRANSFER_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                amount, callbackUrl, userType, documentType, documentNumber, email, currency,paymentDescription
        )
        )
    }

    @Throws(KushkiException::class)
    fun bankListSubscriptionTransfer():BankList{
        return kushkiClient.get(SUBSCRIPTION_TRANSFER_BANKLIST)
    }

    companion object {

        private const val TOKENS_PATH = "v1/tokens"
        private const val SUBSCRIPTION_TOKENS_PATH = "v1/subscription-tokens"
        private const val CARD_ASYNC_TOKENS_PATH = "card-async/v1/tokens"
        private const val TRANSFER_TOKENS_PATH = "transfer/v1/tokens"
        private const val SUBSCRIPTION_TRANSFER_BANKLIST = "transfer-subscriptions/v1/bankList"


    }
}