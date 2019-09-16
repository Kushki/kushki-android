package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

public class RequestCardAsyncTokenAsyncTask extends AbstractRequestCadTokenAsyncTask {

    RequestCardAsyncTokenAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Transaction requestCardAsyncToken(String email) throws KushkiException {
        return kushkiCardAsync.requestCardAsyncToken(1000,
                "https://return.url",email);
    }

}