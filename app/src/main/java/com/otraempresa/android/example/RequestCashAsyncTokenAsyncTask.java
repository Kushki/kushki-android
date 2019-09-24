package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

public class RequestCashAsyncTokenAsyncTask extends AbstractRequestCashTokenAsyncTask {

    RequestCashAsyncTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestCashAsyncToken(String name) throws KushkiException {
        return kushki.requestCashToken(name,
                "Su","1237254","CC","test@test.com",100,"USD");
    }

}