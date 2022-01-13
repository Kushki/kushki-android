package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestTransferTokenAsyncTask
import com.kushkipagos.models.Amount
import com.kushkipagos.models.Transaction
import com.kushkipagos.models.Transfer

class RequestTransferTokenAsyncTask: AbstractRequestTransferTokenAsyncTask {
    constructor(context: Context) : super(context)

    @Throws(KushkiException::class)
    override fun requestTransferToken(token: Transfer): Transaction {
        return _kushki.requestTransferToken(
            Amount(12.2, 0.0, 1.2),
            "www.kushki.com",
            token.toJsonObject().optString("userType"),
            token.toJsonObject().optString("documentType"),
            token.toJsonObject().optString("documentNumber"),
            token.toJsonObject().optString("email"),
            "CLP"
        )
    }
}