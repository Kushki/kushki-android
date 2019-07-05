package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

abstract class AbstractRequestCadTokenAsyncTask extends AsyncTask<String, Void, Transaction> {

    protected final Kushki kushkiCardAsync;
    private final Context context;

    AbstractRequestCadTokenAsyncTask(Context context) {
        this.context = context;
        kushkiCardAsync = new Kushki("20000000103098876000", "CLP", KushkiEnvironment.QA);
    }

    @Override
    protected Transaction doInBackground(String... params) {
        try {
            return requestCardAsyncToken(params[0]);
        } catch (KushkiException kushkiException) {
            throw new RuntimeException(kushkiException);
        }
    }

    @Override
    protected void onPostExecute(Transaction transaction) {
        if (transaction.isSuccessful()) {
            showToast(transaction.getToken());
        } else {
            showToast("ERROR: " + transaction.getCode() + " " + transaction.getMessage());
        }
    }

    protected abstract Transaction requestCardAsyncToken(String params) throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}