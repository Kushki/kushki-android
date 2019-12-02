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
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String, description: String, email: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount
                , this.currency
                , remoteUrl
                , description
                , email)
        )
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String, email: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount
                , this.currency
                , remoteUrl
                , email
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount
                , this.currency
                , remoteUrl
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashToken(name: String, lastName: String, identification: String, documentType: String,
                         email: String,totalAmount: Double, currency: String, description: String): Transaction {
        return kushkiClient.post(CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                identification,
                documentType,
                email,
                totalAmount,
                currency,
                description,
                true
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashToken(name: String, lastName: String, identification: String, documentType: String,
                         email: String,totalAmount: Double, currency: String): Transaction {
        return kushkiClient.post(CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                identification,
                documentType,
                email,
                totalAmount,
                currency,
                true
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashToken(name: String, lastName: String, identification: String, documentType: String,
                         totalAmount: Double, currency: String): Transaction {
        return kushkiClient.post(CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                identification,
                documentType,
                totalAmount,
                currency,
                true
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashOutToken(name: String, lastName: String, documentNumber: String, documentType: String,
                            email: String,totalAmount: Double, currency: String, description: String): Transaction {
        return kushkiClient.post(PAYOUTS_CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                documentNumber,
                documentType,
                email,
                totalAmount,
                currency,
                description,
                false
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashOutToken(name: String, lastName: String, documentNumber: String, documentType: String,
                         email: String,totalAmount: Double, currency: String): Transaction {
        return kushkiClient.post(PAYOUTS_CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                documentNumber,
                documentType,
                email,
                totalAmount,
                currency,
                false
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashOutToken(name: String, lastName: String, identification: String, documentType: String,
                         totalAmount: Double, currency: String): Transaction {
        return kushkiClient.post(PAYOUTS_CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                identification,
                documentType,
                totalAmount,
                currency,
                false
        )
        )
    }


    @Throws(KushkiException::class)
    fun requestTransferToken(amount: Amount, callbackUrl: String, userType: String, documentType: String,
                             documentNumber: String, email: String, currency: String): Transaction {
        return kushkiClient.post(TRANSFER_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                amount, callbackUrl, userType, documentType, documentNumber, email, currency
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestTransferToken(amount: Amount, callbackUrl: String, userType: String, documentType: String,
                             documentNumber: String, email: String, currency: String, paymentDescription:String): Transaction {
        return kushkiClient.post(TRANSFER_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                amount, callbackUrl, userType, documentType, documentNumber, email, currency,paymentDescription
        )
        )
    }

    @Throws(KushkiException::class)
    fun getBankList():BankList{
        return kushkiClient.get(TRANSFER_SUBSCRIPTION_BANKLIST_PATH)
    }

    @Throws(KushkiException::class)
    fun requestTransferSubscriptionToken(transferSubscriptions: TransferSubscriptions): Transaction{
        return kushkiClient.post(TRANSFER_SUBSCRIPTION_TOKENS_PATH,kushkiJsonBuilder.buildJson(
                transferSubscriptions
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestSecureValidation(askQuestionnaire: AskQuestionnaire): SecureValidation{
        return kushkiClient.post_secure(TRANSFER_SUBSCRIPTION_SECURE_PATH,kushkiJsonBuilder.buildJson(
                askQuestionnaire
        )
        )
    }
    @Throws(KushkiException::class)
    fun requestSecureValidation(validateAnswers: ValidateAnswers): SecureValidation{
        return kushkiClient.post_secure(TRANSFER_SUBSCRIPTION_SECURE_PATH,kushkiJsonBuilder.buildJson(
                validateAnswers
        )
        )
    }

    @Throws(KushkiException::class)
    fun getBinInfo(bin:String):BinInfo{
        return kushkiClient.get_bin(CARD_BIN_PATH+bin)
    }


    companion object {
        private const val TOKENS_PATH = "v1/tokens"
        private const val SUBSCRIPTION_TOKENS_PATH = "v1/subscription-tokens"
        private const val CARD_ASYNC_TOKENS_PATH = "card-async/v1/tokens"
        private const val CASH_TOKENS_PATH = "cash/v1/tokens"
        private const val TRANSFER_TOKENS_PATH = "transfer/v1/tokens"
        private const val TRANSFER_SUBSCRIPTION_TOKENS_PATH = "transfer-subscriptions/v1/tokens"
        private const val TRANSFER_SUBSCRIPTION_BANKLIST_PATH = "transfer-subscriptions/v1/bankList"
        private const val TRANSFER_SUBSCRIPTION_SECURE_PATH = "rules/v1/secureValidation"
        private const val CARD_BIN_PATH = "card/v1/bin/"
        private const val PAYOUTS_CASH_TOKENS_PATH = "payouts/cash/v1/tokens"
    }
}