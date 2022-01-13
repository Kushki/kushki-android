package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.MerchantSettings

abstract class AbstractRequestMerchantSettingsTask: CoroutineTask<String, Void, MerchantSettings> {
    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("c308a8af32114b8c97f8ba191149df9b", "USD", KushkiEnvironment.QA)
    }

    @Throws(KushkiException::class)
    abstract fun requestMerchantSettings() : MerchantSettings

    override fun doInBackground(vararg params: String?): MerchantSettings {
        try {
            return requestMerchantSettings();
        } catch (kushkiException: KushkiException) {
            throw RuntimeException(kushkiException);
        }
    }

    override fun onPostExecute(param: MerchantSettings?) {
        val merchantSettings = param!!
        if (merchantSettings.merchant_name == "") {
            showToast("Code: ${merchantSettings.code} Message: ${merchantSettings.message}");
        } else {
            showToast(
                """
                    Merchant name: ${merchantSettings.merchant_name}
                    Country: ${merchantSettings.country}
                    Processor name: ${merchantSettings.processor_name}
                    prodBaconKey: ${merchantSettings.prodBaconKey}
                    sandboxBaconKey: ${merchantSettings.sandboxBaconKey}
                    processors: ${merchantSettings.processors}
                    """.trimIndent()
            )
        }
    }

    fun showToast(text: String){
        val toast = Toast.makeText(_context, text, Toast.LENGTH_LONG)
        toast.show()
    }
}