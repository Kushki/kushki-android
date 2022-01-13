package com.kushkipagos.kushkidemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.kushkipagos.views.Security3DSActivity
import com.kushkipagos.kushkidemo.classes.Web3DSView
import com.kushkipagos.kushkidemo.services.*
import com.kushkipagos.models.*
import com.kushkipagos.models.TransferSubscriptions
import kotlinx.coroutines.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.ExecutionException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        val emailText = findViewById<View>(R.id.emailText) as EditText
        val userType = findViewById<View>(R.id.user_type) as Spinner
        val documentType = findViewById<View>(R.id.document_type) as Spinner
        val documentType2 = findViewById<View>(R.id.document_type2) as Spinner
        val reference = findViewById<View>(R.id.reference) as EditText
        val email = findViewById<View>(R.id.email) as EditText
        val description = findViewById<View>(R.id.description) as EditText
        val documentNumber_2 = findViewById<View>(R.id.documentNumber_2) as EditText
        val firstName = findViewById<View>(R.id.firstName) as EditText
        val lastName = findViewById<View>(R.id.lastName) as EditText
        val accountNumber = findViewById<View>(R.id.accountNumber) as EditText
        val name = findViewById<View>(R.id.name) as EditText
        val lastNameCash = findViewById<View>(R.id.lastNameCash) as EditText
        val identification = findViewById<View>(R.id.identification) as EditText
        val emailSubscriptionAsync = findViewById<View>(R.id.emailSubscriptionAsync) as EditText
        val currency = findViewById<View>(R.id.currency) as EditText
        val callbackUrl = findViewById<View>(R.id.callbackUrl) as EditText
        val cardNumber = findViewById<View>(R.id.cardNumber) as EditText
        val subscriptionId = findViewById<EditText>(R.id.subscriptionIdInput)
        val tokenChargeButton = findViewById<View>(R.id.SendTokenChargeButton) as Button
        tokenChargeButton.setOnClickListener {
            RequestTokenChargeAsyncTask(applicationContext).execute(
                subscriptionId.text.toString()
            )
        }
        val siftTokenButton = findViewById<View>(R.id.SendSiftTokenBtn) as Button
        siftTokenButton.setOnClickListener {
            RequestSiftScienceSessionAsyncTask(applicationContext).execute(
                buildSiftCard()
            )
        }
        val merchantSettingsInfo = findViewById<View>(R.id.getMerchantSettingsInfo) as Button
        merchantSettingsInfo.setOnClickListener { RequestMerchantSettingsTask(applicationContext).execute() }

        val transactionButton = findViewById<View>(R.id.transactionButton) as Button
        val textValidation3DS = findViewById<View>(R.id.textValidation3DS) as TextView
        val requestTransactionToken = RequestTransactionTokenAsyncTask(applicationContext, this)
        val requestSecureValidation = RequestCardSecureValidationAsyncTask(applicationContext)

        transactionButton.setOnClickListener {
            GlobalScope.launch {
                textValidation3DS.text = "Cargando..."
                val transactionResult = requestTransactionToken.requestToken(buildCard())

                withContext(Dispatchers.Main) {
                    if (transactionResult.validated3DS != null) {
                        if (transactionResult.security!!.specificationVersion.startsWith("2.")) {
                            if (transactionResult.validated3DS!!.validated) {
                                if (transactionResult.validated3DS!!.sandbox) {
                                    withContext(Dispatchers.Main) {
                                        textValidation3DS.text = "Sandbox Information: \nValidation 3DS: ${true} \nToken: ${transactionResult.token}"
                                    }
                                } else {
                                    GlobalScope.launch {
                                        val secureValidation =
                                            requestSecureValidation.requestOTPValidation(
                                                OTPValidation(transactionResult.secureId, "")
                                            )
                                        withContext(Dispatchers.Main) {
                                            textValidation3DS.text =
                                                "Validation 3DS: ${secureValidation.is3DSValidationOk} \nToken: ${transactionResult.token}"
                                        }
                                    }
                                }

                            } else {
                                textValidation3DS.text =
                                    "Validation 3DS: ${transactionResult.validated3DS!!.validated} ${transactionResult.validated3DS!!.message}"
                            }
                        } else {
                            textValidation3DS.text = transactionResult.validated3DS!!.message
                            initWebView(transactionResult.secureId, textValidation3DS)
                            loadWebUrl(
                                webView,
                                transactionResult.security!!.paReq,
                                transactionResult.security!!.acsURL
                            )
                        }
                    } else {
                        textValidation3DS.text =
                            "Token request (not 3DS): ${transactionResult.message} ${transactionResult.token}"
                    }
                }
            }
        }
        val subscriptionButton = findViewById<View>(R.id.sendSubscriptionButton) as Button
        subscriptionButton.setOnClickListener {
            RequestSubscriptionTokenAsyncTask(applicationContext).execute(
                buildCard()
            )
        }
        val cardAsyncButton = findViewById<View>(R.id.sendCardAsyncButton) as Button
        cardAsyncButton.setOnClickListener {
            RequestCardAsyncTokenAsyncTask(applicationContext).execute(
                emailText.text.toString()
            )
        }
        val cashButton = findViewById<View>(R.id.sendCashButton) as Button
        cashButton.setOnClickListener {
            RequestCashAsyncTokenAsyncTask(applicationContext).execute(
                name.text.toString(), lastNameCash.text.toString(), identification.text.toString()
            )
        }
        val transferButton = findViewById<View>(R.id.sendTransferTokenButton) as Button
        transferButton.setOnClickListener {
            RequestTransferTokenAsyncTask(applicationContext).execute(
                Transfer(
                    Amount(12.2, 0.0, 1.2),
                    "www.kushki.com",
                    mapUser(userType.selectedItem.toString())!!,
                    documentType.selectedItem.toString(),
                    reference.text.toString(),
                    email.text.toString(),
                    "CLP",
                    description.text.toString()
                )
            )
        }
        val transferSubscriptionButton =
            findViewById<View>(R.id.sendTransferSubscriptionTokenButton) as Button
        transferSubscriptionButton.setOnClickListener {
            RequestTransferSubscriptionTokenAsyncTask(applicationContext).execute(
                TransferSubscriptions(
                    documentNumber_2.text.toString(),
                    "1",
                    firstName.text.toString(),
                    lastName.text.toString(),
                    accountNumber.text.toString(),
                    documentType2.selectedItem.toString(),
                    "123",
                    12,
                    email.text.toString(),
                    "CLP"
                )
            )
        }
        val SecureValidationInfoButton =
            findViewById<View>(R.id.sendSecureValidationButton) as Button
        SecureValidationInfoButton.setOnClickListener {
            try {
                RequestSecureValidationInfoAsyncTask(applicationContext).execute(
                    buildRequestSecure()
                )
            } catch (e: InterruptedException) {
                println(e)
            } catch (e: ExecutionException) {
                println(e)
            }
        }
        val cardSubscriptionAsyncButton =
            findViewById<View>(R.id.SendCardSubscriptionAsyncTokenButton) as Button
        cardSubscriptionAsyncButton.setOnClickListener {
            RequestCardSubscriptionAsyncTokenAsyncTask(applicationContext).execute(
                emailSubscriptionAsync.text.toString(),
                currency.text.toString(),
                callbackUrl.text.toString(),
                cardNumber.text.toString()
            )
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun loadWebUrl(webView: WebView, payload: String, acsUrl: String) {
        val termUrl = "https://www.youtube.com" // CUSTOM_TERM_URL
        val postData = "PaReq=" + URLEncoder.encode(
            payload,
            "UTF-8"
        ) + "&TermUrl=" + URLEncoder.encode(termUrl, "UTF-8");
        webView.postUrl(acsUrl, postData.toByteArray());
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun initWebView(secureId: String, textView: TextView) {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.defaultTextEncodingName = "utf-8"
        webView.webViewClient = Web3DSView(applicationContext, secureId, textView)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun buildRequestSecure(): AskQuestionnaire {
        val documentType2 = findViewById<View>(R.id.document_type2) as Spinner
        val documentNumber_2 = findViewById<View>(R.id.documentNumber_2) as EditText
        val firstName = findViewById<View>(R.id.firstName) as EditText
        val lastName = findViewById<View>(R.id.lastName) as EditText
        val accountNumber = findViewById<View>(R.id.accountNumber) as EditText
        val email = findViewById<View>(R.id.email) as EditText
        val expeditionDocumentDate = findViewById<View>(R.id.expeditionDocumentDate) as EditText
        val cityCode = findViewById<View>(R.id.cityCode) as EditText
        val stateCode = findViewById<View>(R.id.stateCode) as EditText
        val phone = findViewById<View>(R.id.phone) as EditText
        val transferSubscription = RequestTransferSubscriptionTokenAsyncTask(
            applicationContext
        )

        transferSubscription.execute(
            TransferSubscriptions(
                documentNumber_2.text.toString(),
                "1",
                firstName.text.toString(),
                lastName.text.toString(),
                accountNumber.text.toString(),
                documentType2.selectedItem.toString(),
                "01",
                12,
                email.text.toString(),
                "CLP"
            )
        )
        val subscription = transferSubscription._result!!

        val secureService: String = subscription.secureService
        val secureId: String = subscription.secureId
        return AskQuestionnaire(
            secureId, secureService, "1", stateCode.text.toString(),
            phone.text.toString(), "15/12/1958", "20000000107415376000"
        )
    }

    private fun buildCard(): Card {
        val nameText = findViewById<View>(R.id.nameText) as EditText
        val numberText = findViewById<View>(R.id.numberText) as EditText
        val monthText = findViewById<View>(R.id.monthText) as EditText
        val yearText = findViewById<View>(R.id.yearText) as EditText
        val cvvText = findViewById<View>(R.id.cvvText) as EditText
        return Card(
            nameText.text.toString(), numberText.text.toString(),
            cvvText.text.toString(), monthText.text.toString(), yearText.text.toString()
        )
    }

    private fun buildSiftCard(): Card? {
        val cardNameSift = findViewById<EditText>(R.id.cardHolderInput)
        val cardNumberSift = findViewById<EditText>(R.id.cardNumberInput)
        val cardCvvSift = findViewById<EditText>(R.id.cardCvvInput)
        val cardExpiryYearSift = findViewById<EditText>(R.id.cardYearInput)
        val cardExpiryMonthSift = findViewById<EditText>(R.id.cardMonthInput)
        return Card(
            cardNameSift.text.toString(),
            cardNumberSift.text.toString(),
            cardCvvSift.text.toString(),
            cardExpiryMonthSift.text.toString(),
            cardExpiryYearSift.text.toString()
        )
    }

    private fun mapUser(usertType: String): String {
        return if (usertType == "Natural") "0" else "1"
    }
}