package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;
import com.kushkipagos.android.TransferSubscriptions;

public class RequestTransferSubscriptionTokenAsyncTask extends AbstractRequestTransferSubscriptionTokenAsyncTask {

    RequestTransferSubscriptionTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestTransferToken(TransferSubscriptions transferSubscriptions) throws KushkiException {
        return kushki.requestTransferSubscriptionToken(new TransferSubscriptions(transferSubscriptions.toJsonObject().optString("documentNumber"),
                "1",transferSubscriptions.toJsonObject().optString("name"),transferSubscriptions.toJsonObject().optString("lastName"),
                transferSubscriptions.toJsonObject().optString("accountNumber"), "CC",
                "01",12,"jose.gonzalez@kushkipagos.com",
                        "CLP"
        ));
    }
}