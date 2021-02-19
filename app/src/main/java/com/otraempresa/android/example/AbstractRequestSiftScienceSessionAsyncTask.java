package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

abstract class AbstractRequestSiftScienceSessionAsyncTask extends AsyncTask<Card, Void, Transaction> {
    protected final Kushki kushki;
    private final Context context;

    AbstractRequestSiftScienceSessionAsyncTask(Context context) {
        this.context = context;
        kushki = new Kushki("e41151f380a145059b6c8f4d45002130", "USD", KushkiEnvironment.TESTING);
    }


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
