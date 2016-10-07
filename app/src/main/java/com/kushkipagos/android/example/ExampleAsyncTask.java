package com.kushkipagos.android.example;

import android.os.AsyncTask;
import android.widget.TextView;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

class ExampleAsyncTask extends AsyncTask<String, Void, Transaction> {

    private final TextView textView;

    ExampleAsyncTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected Transaction doInBackground(String... args) {
        Kushki kushki = new Kushki("10000001656015280078454110039965", "USD", KushkiEnvironment.TESTING);
        Card card = new Card("Lisbeth Salander", "4017779991118888", "123", "12", "21");
        try {
            return kushki.requestToken(card, 10.0);
        } catch (KushkiException kushkiException) {
            kushkiException.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Transaction transaction) {
        if (transaction.isSuccessful()) {
            textView.setText(transaction.getToken());
        } else {
            textView.setText("ERROR: " + transaction.getCode() + " " + transaction.getText());
        }
    }
}
