package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestCadTokenAsyncTask
import com.kushkipagos.models.Transaction

class RequestCardAsyncTokenAsyncTask: AbstractRequestCadTokenAsyncTask {

    constructor(context: Context): super(context)

    @Throws(KushkiException::class)
    override fun requestCardAsyncToken(email: String): Transaction {
        return _kushki.requestCardAsyncToken(1000.0,
            "https://return.url",email)
    }
}