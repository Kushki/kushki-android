package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.Amount;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;
import com.kushkipagos.android.Transfer;

public class RequestTransferTokenAsyncTask extends AbstractRequestTransferTokenAsyncTask {

    RequestTransferTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestTransferToken(Transfer token) throws KushkiException {
        return kushki.transferTokens(new Amount(12.2,0.0,1.2),"www.kushki.com",
                token.toJsonObject().optString("userType"),token.toJsonObject().optString("documentType"),
                token.toJsonObject().optString("documentNumber"),token.toJsonObject().optString("email"),
                "CLP");

    }

}