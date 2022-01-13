package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.Transaction

abstract class AbstractRequestCardSubscriptionAsyncTokenAsyncTask:
    CoroutineTask<String, Void, Transaction> {

    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("c308a8af32114b8c97f8ba191149df9b", "CLP", KushkiEnvironment.QA)
    }

    @Throws(KushkiException::class)
    abstract fun requestCardSubscriptionAsyncToken(email: String, currency: String, callbackUrl: String, cardNumber: String) : Transaction

    override fun doInBackground(vararg params: String?): Transaction {
        try {
            return requestCardSubscriptionAsyncToken(params[0]!!,
                params[1]!!, params[2]!!, params[3]!!
            );
        } catch (kushkiException: KushkiException) {
            throw RuntimeException(kushkiException);
        }
    }

    override fun onPostExecute(param: Transaction?) {
        val transaction = param!!
        if (transaction.isSuccessful) {
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