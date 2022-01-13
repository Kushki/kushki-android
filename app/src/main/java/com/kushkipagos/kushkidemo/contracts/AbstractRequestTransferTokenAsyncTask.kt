package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.Transaction
import com.kushkipagos.models.Transfer

abstract class AbstractRequestTransferTokenAsyncTask: CoroutineTask<Transfer, Void, Transaction> {

    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("c308a8af32114b8c97f8ba191149df9b", "USD", KushkiEnvironment.QA)
    }

    @Throws(KushkiException::class)
    protected abstract fun requestTransferToken(token: Transfer): Transaction

    override fun doInBackground(vararg args: Transfer?): Transaction {
         try {
             return requestTransferToken(args[0]!!)
        } catch (kushkiException: KushkiException) {
            throw RuntimeException(kushkiException)
        }
    }

    override fun onPostExecute(param: Transaction?) {
        val transaction = param!!
        if (transaction.isSuccessful) {
            showToast(transaction.token)
        } else {
            showToast("ERROR: " + transaction.code + " " + transaction.message)
        }
    }

    fun showToast(text: String) {
        val toast = Toast.makeText(_context, text, Toast.LENGTH_SHORT)
        toast.show()
    }
}