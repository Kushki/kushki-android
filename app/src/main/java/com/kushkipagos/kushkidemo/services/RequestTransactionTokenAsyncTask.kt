package com.kushkipagos.kushkidemo.services

import android.app.Activity
import android.content.Context
import com.kushkipagos.kushkidemo.contracts.AbstractRequestTokenAsyncTask
import com.kushkipagos.models.Card
import com.kushkipagos.models.Transaction
import kotlinx.coroutines.runBlocking

class RequestTransactionTokenAsyncTask : AbstractRequestTokenAsyncTask {

    val _context: Context
    val _activity: Activity

    constructor(context: Context, activity: Activity) : super(context) {
        _context = context
        _activity = activity
    }

    override fun requestToken(card: Card): Transaction {
        return runBlocking { _kushki.requestToken(card,111111.9, _context, _activity) }
    }

}