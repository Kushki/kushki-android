package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.Card
import com.kushkipagos.models.Transaction

abstract class AbstractRequestTokenAsyncTask : CoroutineTask<Card, Void, Transaction> {
    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("d6b3e17702e64d85b812c089e24a1ca1", "COP", KushkiEnvironment.TESTING)
    }

    @Throws(KushkiException::class)
    abstract fun requestToken(card: Card) : Transaction

    override fun doInBackground(vararg params: Card?): Transaction {
        try {
            return requestToken(params[0]!!);
        } catch (kushkiException: KushkiException) {
            throw RuntimeException(kushkiException);
        }
    }

    override fun onPostExecute(param: Transaction?) {
        val transaction = param!!
        if (transaction.isSuccessful) {
            showToast("TOKEN: ${transaction.token} SECURE_ID: ${transaction.secureId}")
        } else {
            showToast("ERROR: Code: ${transaction.code} ${transaction.message}")
        }
    }

    fun showToast(text: String){
        val toast = Toast.makeText(_context, text, Toast.LENGTH_LONG)
        toast.show()
    }
}