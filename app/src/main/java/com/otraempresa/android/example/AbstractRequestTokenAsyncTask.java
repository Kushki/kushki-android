package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

abstract class AbstractRequestTokenAsyncTask extends AsyncTask<Card, Void, Transaction> {

    protected final Kushki kushki;
    protected final Kushki kushkiCardAsync;
    private final Context context;

    AbstractRequestTokenAsyncTask(Context context) {
        this.context = context;
        kushki = new Kushki("10000001656015280078454110039965", "USD", KushkiEnvironment.TESTING);
        kushkiCardAsync = new Kushki("20000000103098876000", "CLP", KushkiEnvironment.QA);
    }

    @Override
    protected Transaction doInBackground(Card... args) {
        try {
            return requestToken(args[0]);
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

    protected abstract Transaction requestToken(Card card) throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}