package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestCardSubscriptionAsyncTokenAsyncTask
import com.kushkipagos.models.Transaction

class RequestCardSubscriptionAsyncTokenAsyncTask:
    AbstractRequestCardSubscriptionAsyncTokenAsyncTask {
    constructor(context: Context): super(context)

    @Throws(KushkiException::class)
    override fun requestCardSubscriptionAsyncToken(
        email: String,
        currency: String,
        callbackUrl: String,
        cardNumber: String
    ): Transaction {
        return _kushki.requestCardSubscriptionAsyncToken(email,
            currency,callbackUrl, cardNumber)
    }
}