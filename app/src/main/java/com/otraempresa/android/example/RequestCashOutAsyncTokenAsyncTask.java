package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.CashOut;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

public class RequestCashOutAsyncTokenAsyncTask extends AbstractRequestCashOutTokenAsyncTask {

    RequestCashOutAsyncTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestCashOutAsyncToken(CashOut cash) throws KushkiException {
        return kushki.requestCashOutToken(cash.toJsonObject().optString("name"),
                cash.toJsonObject().optString("lastName"),cash.toJsonObject().optString("documentNumber"),"CC","test@test.com",100,"COP");
    }

}