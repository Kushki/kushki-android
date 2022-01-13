package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestTokenAsyncTask
import com.kushkipagos.models.Card
import com.kushkipagos.models.Transaction

class RequestSubscriptionTokenAsyncTask: AbstractRequestTokenAsyncTask {
    constructor(context: Context): super(context)

    @Throws(KushkiException::class)
    override fun requestToken(card: Card): Transaction {
        return _kushki.requestSubscriptionToken(card)
    }
}