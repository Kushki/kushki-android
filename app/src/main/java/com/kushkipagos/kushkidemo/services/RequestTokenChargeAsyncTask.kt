package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestTokenChargeAsyncTask
import com.kushkipagos.models.Card
import com.kushkipagos.models.Transaction

class RequestTokenChargeAsyncTask: AbstractRequestTokenChargeAsyncTask {
    val _context: Context

    var card = Card("Patricio Moreaon", "4242424242424242", "111", "06", "22")

    constructor(context: Context): super(context){
        _context = context
    }

    @Throws(KushkiException::class)
    override fun requestTokenCharge(params: String): Transaction {
        return _kushki.requestTokenCharge(card, 10.0, params, true, _context)
    }
}