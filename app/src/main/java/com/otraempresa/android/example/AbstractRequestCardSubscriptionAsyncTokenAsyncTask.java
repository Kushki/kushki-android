package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

abstract class AbstractRequestCardSubscriptionAsyncTokenAsyncTask extends AsyncTask<String, Void, Transaction> {

    protected final Kushki kushki;
    private final Context context;

    AbstractRequestCardSubscriptionAsyncTokenAsyncTask(Context context) {
        this.context = context;
        kushki= new Kushki("e955d8c491674b08869f0fe6f480c63e", "CLP", KushkiEnvironment.QA);
    }

    @Override
    protected Transaction doInBackground(String... params) {
        try {
            return requestCardSubscriptionAsyncToken(params[0], params[1], params[2] , params[3]);
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

    protected abstract Transaction requestCardSubscriptionAsyncToken(String email, String currency, String callbackUrl, String cardNumber) throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}