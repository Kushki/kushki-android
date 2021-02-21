package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

abstract class AbstractRequestTokenChargeAsyncTask extends AsyncTask<String, Void, Transaction> {

    protected final Kushki kushki;
    private final Context context;

    AbstractRequestTokenChargeAsyncTask(Context context) {
        this.context = context;
        kushki = new Kushki("20000000106952643000", "USD", KushkiEnvironment.QA);
    }

    @Override
    protected Transaction doInBackground(String... params) {
        try {
            return requestTokenCharge(params[0]);
        } catch (KushkiException kushkiException) {
            throw new RuntimeException(kushkiException);
        }
    }

    @Override
    protected void onPostExecute(Transaction transaction) {
        if (transaction.isSuccessful()) {
            showToast(transaction.getToken());
            Log.i("Token: ",transaction.getToken());
        } else {
            showToast("ERROR: " + transaction.getCode() + " " + transaction.getMessage());
        }
    }

    protected abstract Transaction requestTokenCharge(String params) throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}