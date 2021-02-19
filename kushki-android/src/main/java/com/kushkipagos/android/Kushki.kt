package com.kushkipagos.android

import android.content.Context
import com.siftscience.SiftClient
import com.siftscience.model.CreateOrderFieldSet
import com.kushkipagos.library.SiftScience


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
    fun requestToken(card: Card, totalAmount: Double,context: Context?, isTest:Boolean): Transaction {
        val merchantSettings=requestMerchantSettings()
        val merchantCredentials:SiftScienceObject =kushkiClient.getScienceSession(card)
        println("Key: "+merchantSettings.   prodBaconKey)
        println("UserId: "+merchantCredentials.userId)
        println("SessionId: "+merchantCredentials.sessionId)
        val siftScienceAndroid = SiftScience()
        if(isTest){
            if (context != null) {
                siftScienceAndroid.initSiftScience(merchantSettings.sandboxAccountId, merchantSettings.sandboxBaconKey, merchantCredentials.userId, merchantCredentials.sessionId, context)
            }
        }else{
            if (context != null) {
                siftScienceAndroid.initSiftScience(merchantSettings.prodAccountId, merchantSettings.prodAccountId, merchantCredentials.userId, merchantCredentials.sessionId, context)
            }
        }

        return kushkiClient.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(card, totalAmount, this.currency,merchantCredentials.userId,merchantCredentials.sessionId))
    }

    @Throws(KushkiException::class)
    fun requestToken(card: Card, totalAmount: Double): Transaction {
        val merchantSettings=requestMerchantSettings()
        val merchantCredentials:SiftScienceObject =kushkiClient.getScienceSession(card)
        println("SandboxKey: "+merchantSettings.sandboxBaconKey)
        println("Account ID: "+merchantSettings.sandboxAccountId)
        println("UserId: "+merchantCredentials.userId)
        println("SessionId: "+merchantCredentials.sessionId)
        /*val siftScienceAndroid = SiftScience()
        siftScienceAndroid.initSiftScience(merchantSettings.sandboxAccountId, merchantSettings.sandboxBaconKey, merchantCredentials.userId, merchantCredentials.sessionId, context)
*/
        val siftJava = SiftClient(merchantSettings.sandboxBaconKey, merchantSettings.sandboxAccountId)
        siftJava.buildRequest(CreateOrderFieldSet().setUserId(merchantCredentials.userId).setSessionId(merchantCredentials.sessionId))
        return kushkiClient.post(TOKENS_PATH, kushkiJsonBuilder.buildJson(card, totalAmount, this.currency,merchantCredentials.userId,merchantCredentials.sessionId))
    }

    @Throws(KushkiException::class)
    fun requestTokenCharge(totalAmount: Double, remoteUrl: String, description: String, email: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount, this.currency, remoteUrl, description, email)
        )
    }

    @Throws(KushkiException::class)
    fun requestSubscriptionToken(card: Card): Transaction {
        return kushkiClient.post(SUBSCRIPTION_TOKENS_PATH, kushkiJsonBuilder.buildJson(card, this.currency))
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String, description: String, email: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount, this.currency, remoteUrl, description, email)
        )
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String, email: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount, this.currency, remoteUrl, email
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String): Transaction {
        return kushkiClient.post(CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount, this.currency, remoteUrl
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashToken(name: String, lastName: String, identification: String, documentType: String,
                         email: String, totalAmount: Double, currency: String, description: String): Transaction {
        return kushkiClient.post(CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                identification,
                documentType,
                email,
                totalAmount,
                currency,
                description
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashToken(name: String, lastName: String, identification: String, documentType: String,
                         email: String, totalAmount: Double, currency: String): Transaction {
        return kushkiClient.post(CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                name,
                lastName,
                identification,
                documentType,
                email,
                totalAmount,
                currency
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
                currency
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
                             documentNumber: String, email: String, currency: String, paymentDescription: String): Transaction {
        return kushkiClient.post(TRANSFER_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                amount, callbackUrl, userType, documentType, documentNumber, email, currency, paymentDescription
        )
        )
    }

    @Throws(KushkiException::class)
    fun getBankList():BankList{
        return kushkiClient.get(TRANSFER_SUBSCRIPTION_BANKLIST_PATH)
    }

    @Throws(KushkiException::class)
    fun requestTransferSubscriptionToken(transferSubscriptions: TransferSubscriptions): Transaction{
        return kushkiClient.post(TRANSFER_SUBSCRIPTION_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                transferSubscriptions
        )
        )
    }

    @Throws(KushkiException::class)
    fun requestSecureValidation(askQuestionnaire: AskQuestionnaire): SecureValidation{
        return kushkiClient.post_secure(TRANSFER_SUBSCRIPTION_SECURE_PATH, kushkiJsonBuilder.buildJson(
                askQuestionnaire
        )
        )
    }
    @Throws(KushkiException::class)
    fun requestSecureValidation(validateAnswers: ValidateAnswers): SecureValidation{
        return kushkiClient.post_secure(TRANSFER_SUBSCRIPTION_SECURE_PATH, kushkiJsonBuilder.buildJson(
                validateAnswers
        )
        )
    }

    @Throws(KushkiException::class)
    fun getBinInfo(bin: String):BinInfo{
        return kushkiClient.get_bin(CARD_BIN_PATH + bin)
    }

    @Throws(KushkiException::class)
    fun requestCardSubscriptionAsyncToken(email: String, currency: String, callbackUrl: String, cardNumber: String): Transaction {
        return kushkiClient.post(CARD_SUBSCRIPTION_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                email, currency, callbackUrl, cardNumber
        )
        )
    }
    @Throws(KushkiException::class)
    fun requestCardSubscriptionAsyncToken(email: String, currency: String, callbackUrl: String): Transaction {
        return kushkiClient.post(CARD_SUBSCRIPTION_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                email, currency, callbackUrl
        )
        )
    }
    @Throws(KushkiException:: class)
    fun requestMerchantSettings(): MerchantSettings{
        return kushkiClient.get_merchant_settings(MERCHANT_SETTINGS_PATH)
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
        private const val CARD_SUBSCRIPTION_ASYNC_TOKENS_PATH = "subscriptions/v1/card-async/tokens"
        private const val MERCHANT_SETTINGS_PATH = "merchant/v1/merchant/settings"
    }
}