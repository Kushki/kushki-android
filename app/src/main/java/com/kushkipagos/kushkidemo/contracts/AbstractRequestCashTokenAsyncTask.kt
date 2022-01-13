package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.Transaction

abstract class AbstractRequestCashTokenAsyncTask: CoroutineTask<String, Void, Transaction> {

    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("c308a8af32114b8c97f8ba191149df9b", "CLP", KushkiEnvironment.TESTING)
    }

    @Throws(KushkiException::class)
    abstract fun requestCashAsyncToken(params: String) : Transaction

    override fun doInBackground(vararg params: String?): Transaction {
        try {
            return requestCashAsyncToken(params[0]!!);
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