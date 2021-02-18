package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

class RequestSiftScienceSessionAsyncTask extends AbstractRequestSiftScienceSessionAsyncTask {
    Context context;

    RequestSiftScienceSessionAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestToken(Card card) throws KushkiException {
        return kushki.requestToken(card,10,context);
    }
}
