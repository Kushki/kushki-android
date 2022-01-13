package com.kushkipagos.kushkidemo.classes

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.kushkipagos.kushkidemo.services.RequestCardSecureValidationAsyncTask
import com.kushkipagos.models.OTPValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Web3DSView(val context: Context, val secureId: String, val textView: TextView): WebViewClient() {
    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        if (url != null && url.contains("https://www.youtube.com")) {
            val payload = url.replace("https://www.youtube.com", "")
            if (!payload.isEmpty()) {
                val requestSecureValidation = RequestCardSecureValidationAsyncTask(context)
                GlobalScope.launch {
                    val secureValidationResponse = requestSecureValidation.requestOTPValidation(OTPValidation(secureId, ""))
                    withContext(Dispatchers.Main) {
                        textView.text = "Validation 3DS: ${secureValidationResponse.is3DSValidationOk}"
                    }
                }

            } else {
                System.out.println("not validated")
            }
        }
    }
}