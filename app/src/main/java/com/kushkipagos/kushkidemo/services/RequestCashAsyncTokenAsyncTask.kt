package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestCashTokenAsyncTask
import com.kushkipagos.models.Transaction

class RequestCashAsyncTokenAsyncTask: AbstractRequestCashTokenAsyncTask {
    constructor(context: Context): super(context)

    @Throws(KushkiException::class)
    override fun requestCashAsyncToken(name: String): Transaction {
        return _kushki.requestCashToken(name,
            "Su","1237254","CC","test@test.com",100.0,"USD")
    }
}