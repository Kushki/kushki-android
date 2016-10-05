package com.kushkipagos.android.example;

import android.os.AsyncTask;
import android.widget.TextView;

import com.kushkipagos.android.Card;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.Transaction;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

class ExampleAsyncTask extends AsyncTask<String, Void, Transaction> {

    private final TextView textView;

    ExampleAsyncTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected Transaction doInBackground(String... endpoints) {
        try {
            Kushki kushki = new Kushki("10000001656015280078454110039965", "USD", KushkiEnvironment.TESTING);
            Card card = new Card("Lisbeth Salander", "4017779991118888", "123", "12", "21");
            return kushki.requestToken(card, 10.0);
        } catch (IOException | BadPaddingException | IllegalBlockSizeException | JSONException |
                NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Transaction transaction) {
        try {
            if (transaction.isSuccessful()) {
                textView.setText(transaction.getToken());
            } else {
                textView.setText("ERROR: " + transaction.getCode() + " " + transaction.getText());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
