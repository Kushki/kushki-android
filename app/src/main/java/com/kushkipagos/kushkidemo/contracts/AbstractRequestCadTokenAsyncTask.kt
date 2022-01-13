package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.Transaction

abstract class AbstractRequestCadTokenAsyncTask: CoroutineTask<String, Void, Transaction> {
    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("1195b05f49b44aae83fafc9c8de01b88", "COP", KushkiEnvironment.QA)
    }

    @Throws(KushkiException::class)
    abstract fun requestCardAsyncToken(params: String) : Transaction

    override fun doInBackground(vararg params: String?): Transaction {
        try {
            return requestCardAsyncToken(params[0]!!);
        } catch (kushkiException: KushkiException) {
            throw RuntimeException(kushkiException);
        }
    }

    override fun onPostExecute(param: Transaction?) {
        val transaction = param!!
        if (transaction.isSuccessful) {
            showToast(transaction.token);
        } else {
            showToast("ERROR: Code: ${transaction.code} Message: ${transaction.message}");
        }
    }

    fun showToast(text: String){
        val toast = Toast.makeText(_context, text, Toast.LENGTH_LONG)
        toast.show()
    }
}