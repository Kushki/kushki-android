package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

public class RequestCardSubscriptionAsyncTokenAsyncTask extends AbstractRequestCardSubscriptionAsyncTokenAsyncTask {

    RequestCardSubscriptionAsyncTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestCardSubscriptionAsyncToken(String email, String currency, String callbackUrl, String cardNumber) throws KushkiException {
        return kushki.requestCardSubscriptionAsyncToken(email,
                currency,callbackUrl, cardNumber);
    }

}