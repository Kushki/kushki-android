package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.CashOut;
import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;

abstract class AbstractRequestCashOutTokenAsyncTask extends AsyncTask<CashOut, Void, Transaction> {

    protected final Kushki kushki;
    private final Context context;

    AbstractRequestCashOutTokenAsyncTask(Context context) {
        this.context = context;
        kushki= new Kushki("20000000107321580000", "COP", KushkiEnvironment.QA);
    }

    @Override
    protected Transaction doInBackground(CashOut... args) {
        try {
            return requestCashOutAsyncToken(args[0]);
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

    protected abstract Transaction requestCashOutAsyncToken(CashOut params) throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}