package com.kushkipagos.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

class ExampleAsyncTask extends AsyncTask<Card, Void, Transaction> {

    private final Context context;

    ExampleAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Transaction doInBackground(Card... args) {
        Kushki kushki = new Kushki("10000001656015280078454110039965", "USD", KushkiEnvironment.TESTING);
        try {
            return kushki.requestToken(args[0], 19.99);
        } catch (KushkiException kushkiException) {
            kushkiException.printStackTrace();
            return null;
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

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
