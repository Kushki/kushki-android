package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestSiftScienceSessionAsyncTask
import com.kushkipagos.models.Card
import com.kushkipagos.models.Transaction

class RequestSiftScienceSessionAsyncTask: AbstractRequestSiftScienceSessionAsyncTask {
    val _context: Context
    constructor(context: Context): super(context){
        _context = context
    }

    @Throws(KushkiException::class)
    override fun requestToken(card: Card): Transaction {
        return _kushki.requestToken(card, 10.0, _context, true)
    }
}