package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

class RequestTokenChargeAsyncTask extends AbstractRequestTokenChargeAsyncTask {
    Context appContext;
    Card card = new Card("Patricio Moreaon","4242424242424242","111","06","22");

    RequestTokenChargeAsyncTask(Context context) {
        super(context);
        this.appContext=context;
    }

    @Override
    protected Transaction requestTokenCharge(String params) throws KushkiException {
        return kushki.requestTokenCharge(card,10,params,true,appContext);
    }

}
