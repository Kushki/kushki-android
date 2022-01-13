package com.kushkipagos.kushkidemo.services

import android.content.Context
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushkidemo.contracts.AbstractRequestTransferSubscriptionTokenAsyncTask
import com.kushkipagos.models.Transaction
import com.kushkipagos.models.TransferSubscriptions

class RequestTransferSubscriptionTokenAsyncTask: AbstractRequestTransferSubscriptionTokenAsyncTask {

    constructor(context: Context) : super(context)

    @Throws(KushkiException::class)
    override fun requestTransferToken(transferSubscriptions: TransferSubscriptions): Transaction {
        return _kushki.requestTransferSubscriptionToken(
            TransferSubscriptions(
                transferSubscriptions.toJsonObject().optString("documentNumber"),
                "1",
                transferSubscriptions.toJsonObject().optString("name"),
                transferSubscriptions.toJsonObject().optString("lastName"),
                transferSubscriptions.toJsonObject().optString("accountNumber"),
                "CC",
                "01",
                12,
                "jose.gonzalez@kushkipagos.com",
                "CLP"
            )
        )
    }
}