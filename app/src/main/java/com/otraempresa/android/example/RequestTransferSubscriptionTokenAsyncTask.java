package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.Amount;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;
import com.kushkipagos.android.Transfer;
import com.kushkipagos.android.TransferSubscriptions;

public class RequestTransferSubscriptionTokenAsyncTask extends AbstractRequestTransferSubscriptionTokenAsyncTask {

    RequestTransferSubscriptionTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestTransferToken(TransferSubscriptions transferSubscriptions) throws KushkiException {
        return kushki.transferSubscriptionTokens(new TransferSubscriptions(transferSubscriptions.toJsonObject().optString("documentNumber"),
                "C1",transferSubscriptions.toJsonObject().optString("name"),transferSubscriptions.toJsonObject().optString("lastName"),
                "CE3","DE4",transferSubscriptions.toJsonObject().optString("accountNumber"),
                        transferSubscriptions.toJsonObject().optString("expeditionDate"),"21312312312",
                        transferSubscriptions.toJsonObject().optString("documentType"),"01",12,"CO2"
                        ));
    }
}