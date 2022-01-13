package com.kushkipagos.kushki

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.kushkipagos.auth.Secure3DS
import com.kushkipagos.auth.SiftScience
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.models.*
import com.kushkipagos.views.Security3DSActivity
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Kushki(
    publicMerchantId: String, currency: String = "USD",
    environment: Environment, regional: Boolean
) {
    private val kushkiClient: KushkiClient
    private val kushkiJsonBuilder: KushkiJsonBuilder
    private var currency: String
    private var environment: Environment

    constructor(
        publicMerchantId: String, currency: String,
        environment: Environment
    ) :
            this(publicMerchantId, currency, environment, false)

    init {
        this.kushkiClient = KushkiClient(environment, publicMerchantId, regional)
        this.kushkiJsonBuilder = KushkiJsonBuilder()
        this.currency = currency
        this.environment = environment
    }

    @Throws(KushkiException::class)
    fun requestToken(
        card: Card,
        totalAmount: Double,
        context: Context?,
        isTest: Boolean?
    ): Transaction {
        val merchantSettings = requestMerchantSettings()
        val merchantCredentials: SiftScienceObject = kushkiClient.getScienceSession(card)
        println("UserId: " + merchantCredentials.userId)
        println("SessionId: " + merchantCredentials.sessionId)
        val siftScienceAndroid = SiftScience()
        if (isTest == true) {
            if (context != null) {
                siftScienceAndroid.initSiftScience(
                    merchantSettings.sandboxAccountId,
                    merchantSettings.sandboxBaconKey,
                    merchantCredentials.userId,
                    context
                )
            }
        } else {
            if (context != null) {
                siftScienceAndroid.initSiftScience(
                    merchantSettings.prodAccountId,
                    merchantSettings.prodAccountId,
                    merchantCredentials.userId,
                    context
                )
            }
        }
        return kushkiClient.post(
            TOKENS_PATH,
            kushkiJsonBuilder.buildJson(
                card,
                totalAmount,
                this.currency,
                merchantCredentials.userId,
                merchantCredentials.sessionId
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestToken(card: Card, totalAmount: Double): Transaction {
        return kushkiClient.post(
            TOKENS_PATH,
            kushkiJsonBuilder.buildJson(card, totalAmount, this.currency)
        )
    }

    @Throws(KushkiException::class)
    suspend fun requestToken(
        card: Card,
        totalAmount: Double,
        context: Context,
        activity: Activity
    ): Transaction {

        var transaction = Transaction(
            JSONObject()
                .put("code", "001")
                .put("message", "invalid request token")
                .toString()
        )

        val secure3DS = Secure3DS()
        val merchantSettings: MerchantSettings =
            kushkiClient.get_merchant_settings(MERCHANT_SETTINGS_PATH)

        val is3DSecure: Boolean = merchantSettings.is3DSecure
        val sandbox3ds: Boolean = merchantSettings.sandboxEnable

        val actualCurrency: String = this.currency
        val isTest: Boolean = this.environment === KushkiEnvironment.QA
        if (sandbox3ds && is3DSecure) {
            val jwtResponse: CyberSourceJWT = kushkiClient.get_cybersourceJWT(AUTH_TOKEN)

            try {
                transaction = kushkiClient.post(
                    TOKENS_PATH,
                    kushkiJsonBuilder.buildJson(card, totalAmount, actualCurrency, jwtResponse.jwt)
                )
                if(transaction.security!!.authRequired == "true"){
                    show3DSMockView(context, activity, card, totalAmount, merchantSettings)
                }
            } catch (e: Exception) {
                transaction.message = "${transaction.message}  ${e.message.toString()}"
                return transaction
            }

            return transaction
        }


        if (!is3DSecure) {
            try {
                return kushkiClient.post(
                    TOKENS_PATH,
                    kushkiJsonBuilder.buildJson(card, totalAmount, actualCurrency)
                )
            } catch (e: Exception) {
                transaction.message = "${transaction.message} ${e.message.toString()}"
                return transaction
            }
        }


        val jwtResponse: CyberSourceJWT = kushkiClient.get_cybersourceJWT(AUTH_TOKEN)

        val isInit = secure3DS.init3DSecure(context, jwtResponse.jwt, isTest)

        if (isInit) {
            try {
                transaction = kushkiClient.post(
                    TOKENS_PATH,
                    kushkiJsonBuilder.buildJson(card, totalAmount, actualCurrency, jwtResponse.jwt)
                )
            } catch (e: Exception) {
                transaction.message = "${transaction.message} ${e.message.toString()}"
                return transaction
            }
        }
        if (transaction.isSecure3DS) {
            if (transaction.security!!.specificationVersion.startsWith("2.")) {
                transaction.validated3DS = secure3DS.validate(
                    activity,
                    transaction.security!!.authenticationTransactionId,
                    transaction.security!!.paReq
                )
            } else {
                transaction.validated3DS = Validated3DSResponse(
                    false,
                    "NO ACTION - Version 1.x.x cannot be validated natively"
                )
            }
        }
        return transaction
    }


    @Throws(KushkiException::class)
    fun requestTokenCharge(
        card: Card,
        totalAmount: Double,
        subscriptionId: String,
        isTest: Boolean?,
        context: Context?
    ): Transaction {
        val merchantSettings = requestMerchantSettings()
        val merchantCredentials: SiftScienceObject = kushkiClient.getScienceSession(card)

        val siftScienceAndroid = SiftScience()
        if (isTest == true) {
            if (context != null) {
                siftScienceAndroid.initSiftScience(
                    merchantSettings.sandboxAccountId,
                    merchantSettings.sandboxBaconKey,
                    merchantCredentials.userId,
                    context
                )
            }
        } else {
            if (context != null) {
                siftScienceAndroid.initSiftScience(
                    merchantSettings.prodAccountId,
                    merchantSettings.prodAccountId,
                    merchantCredentials.userId,
                    context
                )
            }
        }
        return kushkiClient.post(
            "$TOKEN_CHARGE_PATH$subscriptionId/tokens",
            kushkiJsonBuilder.buildJson(
                card,
                totalAmount,
                this.currency,
                merchantCredentials.userId,
                merchantCredentials.sessionId
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestTokenCharge(card: Card, totalAmount: Double, subscriptionId: String): Transaction {
        return kushkiClient.post(
            "$TOKEN_CHARGE_PATH$subscriptionId/tokens",
            kushkiJsonBuilder.buildJson(card, totalAmount, this.currency)
        )
    }

    @Throws(KushkiException::class)
    fun requestSubscriptionToken(card: Card): Transaction {
        return kushkiClient.post(
            SUBSCRIPTION_TOKENS_PATH,
            kushkiJsonBuilder.buildJson(card, this.currency)
        )
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(
        totalAmount: Double,
        remoteUrl: String,
        description: String,
        email: String
    ): Transaction {
        return kushkiClient.post(
            CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount, this.currency, remoteUrl, description, email
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String, email: String): Transaction {
        return kushkiClient.post(
            CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount, this.currency, remoteUrl, email
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestCardAsyncToken(totalAmount: Double, remoteUrl: String): Transaction {
        return kushkiClient.post(
            CARD_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                totalAmount, this.currency, remoteUrl
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestCashToken(
        name: String, lastName: String, identification: String, documentType: String,
        email: String, totalAmount: Double, currency: String, description: String
    ): Transaction {
        return kushkiClient.post(
            CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
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
    fun requestCashToken(
        name: String, lastName: String, identification: String, documentType: String,
        email: String, totalAmount: Double, currency: String
    ): Transaction {
        return kushkiClient.post(
            CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
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
    fun requestCashToken(
        name: String, lastName: String, identification: String, documentType: String,
        totalAmount: Double, currency: String
    ): Transaction {
        return kushkiClient.post(
            CASH_TOKENS_PATH, kushkiJsonBuilder.buildJson(
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
    fun requestTransferToken(
        amount: Amount, callbackUrl: String, userType: String, documentType: String,
        documentNumber: String, email: String, currency: String
    ): Transaction {
        return kushkiClient.post(
            TRANSFER_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                amount, callbackUrl, userType, documentType, documentNumber, email, currency
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestTransferToken(
        amount: Amount, callbackUrl: String, userType: String, documentType: String,
        documentNumber: String, email: String, currency: String, paymentDescription: String
    ): Transaction {
        return kushkiClient.post(
            TRANSFER_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                amount,
                callbackUrl,
                userType,
                documentType,
                documentNumber,
                email,
                currency,
                paymentDescription
            )
        )
    }

    @Throws(KushkiException::class)
    fun getBankList(): BankList {
        return kushkiClient.get(TRANSFER_SUBSCRIPTION_BANKLIST_PATH)
    }

    @Throws(KushkiException::class)
    fun requestTransferSubscriptionToken(transferSubscriptions: TransferSubscriptions): Transaction {
        return kushkiClient.post(
            TRANSFER_SUBSCRIPTION_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                transferSubscriptions
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestSecureValidation(askQuestionnaire: AskQuestionnaire): SecureValidation {
        return kushkiClient.post_secure(
            TRANSFER_SUBSCRIPTION_SECURE_PATH, kushkiJsonBuilder.buildJson(
                askQuestionnaire
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestSecureValidation(validateAnswers: ValidateAnswers): SecureValidation {
        return kushkiClient.post_secure(
            TRANSFER_SUBSCRIPTION_SECURE_PATH, kushkiJsonBuilder.buildJson(
                validateAnswers
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestSecureValidation(OTPValidation: OTPValidation): CardSecureValidation {
        return kushkiClient.post_card_secure(
            TRANSFER_SUBSCRIPTION_SECURE_PATH, kushkiJsonBuilder.buildJson(
                OTPValidation
            )
        )
    }

    @Throws(KushkiException::class)
    fun getBinInfo(bin: String): BinInfo {
        return kushkiClient.get_bin(CARD_BIN_PATH + bin)
    }

    @Throws(KushkiException::class)
    fun requestCardSubscriptionAsyncToken(
        email: String,
        currency: String,
        callbackUrl: String,
        cardNumber: String
    ): Transaction {
        return kushkiClient.post(
            CARD_SUBSCRIPTION_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                email, currency, callbackUrl, cardNumber
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestCardSubscriptionAsyncToken(
        email: String,
        currency: String,
        callbackUrl: String
    ): Transaction {
        return kushkiClient.post(
            CARD_SUBSCRIPTION_ASYNC_TOKENS_PATH, kushkiJsonBuilder.buildJson(
                email, currency, callbackUrl
            )
        )
    }

    @Throws(KushkiException::class)
    fun requestMerchantSettings(): MerchantSettings {
        return kushkiClient.get_merchant_settings(MERCHANT_SETTINGS_PATH)
    }

    /**
     * Class for showing 3ds mock
     * The parameters that are received are defined below
     *
     * <ul>
     *     <li>context - Context</li>
     *     <li>activity - Activity</li>
     *     <li>card- Card</li>
     *     <li>totalAmount- Double</li>
     *     <li>merchantSettings- MerchantSettings</li>
     * </ul>
     *
     * <h3>context (@context)</h3>
     * <p>this parameter is the application context</p>
     *
     * <h3>activity (@activity)</h3>
     * <p>this parameter is the activity to be showed</p>
     *
     * <h3>card (@card)</h3>
     * <p>this parameter is the card number to be showed in the mock</p>
     *
     * * <h3>totalAmount (@totalAmount)</h3>
     * <p>this parameter is the total amount to be showed in the mock</p>
     *
     * * <h3>merchantSettings (@merchantSettings)</h3>
     * <p>this parameter is for getting the merchant name to be showed in the mock</p>
     *
     */
    suspend fun show3DSMockView(
        context: Context,
        activity: Activity,
        card: Card,
        totalAmount: Double,
        merchantSettings: MerchantSettings
    ): Validated3DSResponse {
        return suspendCoroutine<Validated3DSResponse> { continuation ->
            val intent = Intent(context, Security3DSActivity::class.java)
            intent.putExtra("merchant", merchantSettings.merchant_name)
            intent.putExtra("cardNumber", card.toJsonObject().optString("number"))
            intent.putExtra("totalAmount", totalAmount)
            intent.putExtra("currency", currency)
            activity.startActivity(intent)
            Timer().schedule(5000) {
                continuation.resume(Validated3DSResponse(true, "SUCCESS", true));
            }
        }
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
        private const val TOKEN_CHARGE_PATH = "subscriptions/v1/card/"
        private const val CARD_SUBSCRIPTION_ASYNC_TOKENS_PATH = "subscriptions/v1/card-async/tokens"
        private const val MERCHANT_SETTINGS_PATH = "merchant/v1/merchant/settings"
        private const val AUTH_TOKEN = "card/v1/authToken"
    }
}