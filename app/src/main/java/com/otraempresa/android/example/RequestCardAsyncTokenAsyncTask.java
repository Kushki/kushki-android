package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

public class RequestCardAsyncTokenAsyncTask extends AbstractRequestTokenAsyncTask {

    RequestCardAsyncTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestToken(Card card) throws KushkiException {
        return kushkiCardAsync.cardAsyncTokens(1000,
                "https://return.url",
                "Description of the payment.",
                "mati@kushkipagos.com");
    }
}