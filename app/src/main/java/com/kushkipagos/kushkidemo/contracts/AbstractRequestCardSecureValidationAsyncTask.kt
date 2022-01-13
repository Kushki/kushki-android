package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.CardSecureValidation
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.OTPValidation

abstract class AbstractRequestCardSecureValidationAsyncTask:
    CoroutineTask<OTPValidation, Void, CardSecureValidation> {
    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("c308a8af32114b8c97f8ba191149df9b", "USD", KushkiEnvironment.QA)
    }

    @Throws(KushkiException::class)
    abstract fun requestOTPValidation(otpValidation: OTPValidation) : CardSecureValidation

    override fun doInBackground(vararg params: OTPValidation?): CardSecureValidation {
        try {
            return requestOTPValidation(params[0]!!);
        } catch (kushkiException: KushkiException) {
            throw RuntimeException(kushkiException);
        }
    }

    override fun onPostExecute(param: CardSecureValidation?) {
        val transaction = param!!
        if (transaction.isSuccessful || transaction.is3DSValidationOk) {
            showToast("${transaction.code} ${transaction.message}");
        } else {
            showToast("ERROR: Code: ${transaction.code} Message: ${transaction.message}");
        }
    }

    fun showToast(text: String){
        val toast = Toast.makeText(_context, text, Toast.LENGTH_LONG)
        toast.show()
    }
}