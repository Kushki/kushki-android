package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

class RequestTransactionTokenAsyncTask extends AbstractRequestTokenAsyncTask {
    Context appContext;

    RequestTransactionTokenAsyncTask(Context context, String currency) {
        super(context, currency);
        this.appContext=context;
    }


    @Override
    protected Transaction requestToken(Card card) throws KushkiException {
        return kushki.requestToken(card, 19.99,appContext,true);
    }
}
